package com.team846.frc2015.components;
import java.util.Arrays;

import com.team846.frc2015.componentData.CarriageExtenderData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.control.RunningSum;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.AsyncPrinter;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;

public class Elevator extends Component implements Configurable {

	private final ElevatorData elevatorData;
	private RunningSum errorSum;

	private int topSoftLimit;
	private int bottomSoftLimit;
	
	private final CANTalon motorA;
	private final CANTalon motorB;
	
	private final AnalogInput elevatorPot;
	
	private final int[] elevatorSetpoints;
	
	private int errorThreshold;
	private boolean freezePosition;
	
	private double positionGain;
	private int collectorOutThreshold;
	private final CollectorArmData armData;
	private final CarriageExtenderData extenderData;
	
	private final Timer stallTimer = new Timer();
	private int stallPosition = 0;
	
	private int error = 0;
	
	private int atPositionCounter = 0;

	public Elevator() {
		super(DriverStationConfig.DigitalIns.NO_DS_DI);
		
		motorA = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_A"));
		motorB = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_B"));
		
		elevatorPot = SensorFactory.getAnalogInput(
				ConfigPortMappings.Instance().get("Analog/ELEVATOR_POT"));
		
		elevatorData = ElevatorData.get();
		armData = CollectorArmData.get();
		extenderData = CarriageExtenderData.get();
		
		positionGain = 0;
		
		elevatorSetpoints = new int[ElevatorData.ElevatorSetpoint.values().length];
		Arrays.fill(elevatorSetpoints, 0);
		
//		motorA.setVoltageRampRate(100.0);
//		motorB.setVoltageRampRate(100.0);
		
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() {

		int currentPosition = elevatorPot.getAverageValue();
		//AsyncPrinter.warn("Current Pos: " + currentPosition);
		elevatorData.setCurrentPosition(currentPosition);
		
		DashboardLogger.getInstance().logInt("elevator-pot", currentPosition);
//		AsyncPrinter.println("Position: " + currentPosition);
		
		if(elevatorData.getControlMode() == ElevatorControlMode.VELOCITY)
		{
			sendOutput(elevatorData.getSpeed());
		}
		else if(elevatorData.getControlMode() == ElevatorControlMode.POSITION
				|| elevatorData.getControlMode() == ElevatorControlMode.SETPOINT)
		{
			int desiredPos = elevatorData.getDesiredPosition();
			if (elevatorData.getControlMode() == ElevatorControlMode.SETPOINT)
			{
				if (elevatorData.getDesiredSetpoint() == ElevatorSetpoint.NONE)
				{
					sendOutput(0.0);
					return;
				}
//				AsyncPrinter.println("Setpoint: " + elevatorData.getDesiredSetpoint().toString());
				desiredPos = elevatorSetpoints[elevatorData.getDesiredSetpoint().ordinal()];
//				AsyncPrinter.println("Setpoint: " + desiredPos);
			}
			if (desiredPos <= topSoftLimit || desiredPos >= bottomSoftLimit)
			{
				AsyncPrinter.error("Setpoint out of bounds");
				sendOutput(0.0);
				return;
			}
			int posErr = desiredPos - currentPosition;
			error = posErr;
			
			double speed = Math.abs(posErr) < errorThreshold ? 0.0 : posErr * positionGain;

			if (speed != 0.0)
				AsyncPrinter.println("Elevator error: " + error + " threshold: " + errorThreshold);
			
			if(speed == 0.0 && elevatorData.getControlMode() == ElevatorControlMode.SETPOINT)
			{
				atPositionCounter--;
				if (atPositionCounter < 0)
					elevatorData.setCurrentPosition(elevatorData.getDesiredSetpoint());
				else
					elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
			}
			else
			{
				atPositionCounter = 4;
				elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
			}
			sendOutput(speed);
		}
	}

	private void sendOutput(double value)
	{
		int currentPosition = elevatorPot.getAverageValue();
		if(currentPosition <= topSoftLimit)
		{
			if (value < 0)
				value = 0;
		}
		else if(currentPosition >= bottomSoftLimit)
		{
			if (value > 0)
				value = 0;
		}
//		else if(currentPosition >= collectorOutThreshold && 
//				Math.abs(extenderData.getCurrentPosition()) > 0.1 &&
//				armData.getCurrentPosition() == ArmPosition.EXTEND)
//		{
//				if (value > 0)
//					value = 0;
//		}
		if (value > 0.1)
		{
			stallTimer.start();
			if (stallTimer.get() > 3.0 && Math.abs(currentPosition - stallPosition) < 100)
			{
				value = 0;
				AsyncPrinter.error("Elevator stalling (timer: " + stallTimer.get() + ", output: " + value);
			}
		}
		else
		{
			stallTimer.stop();
			stallTimer.reset();
			stallPosition = currentPosition;
		}
		motorA.set(value);
		motorB.set(value);
		motorA.enableBrakeMode(false);
		motorB.enableBrakeMode(false);
	}
	
	@Override
	protected void UpdateDisabled() {
		motorA.set(0.0);
		motorB.set(0.0);
		motorA.enableBrakeMode(false);
		motorB.enableBrakeMode(false);
		AsyncPrinter.println("Elevator Position: " + elevatorPot.getAverageValue());
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {
	}

	@Override
	public void Configure() {
		positionGain = GetConfig("positionGain", 0.01);
		
		errorThreshold = GetConfig("errorThreshold", 15);
		
		// Setpoints
		topSoftLimit = GetConfig("topLimit", 100);
		
		// Everything is offset from topSoftLimit
		bottomSoftLimit = topSoftLimit + GetConfig("bottomLimit", 10);
		
		collectorOutThreshold = topSoftLimit + GetConfig("collectorInterlock", 2000);
				
		elevatorSetpoints[ElevatorSetpoint.STEP.ordinal()]= topSoftLimit + GetConfig("step", 1800);
		elevatorSetpoints[ElevatorSetpoint.TOTE_1.ordinal()] = topSoftLimit + GetConfig("tote1", 50);
		elevatorSetpoints[ElevatorSetpoint.TOTE_2.ordinal()]= topSoftLimit + GetConfig("tote2", 60);
		elevatorSetpoints[ElevatorSetpoint.TOTE_3.ordinal()]= topSoftLimit + GetConfig("tote3", 80);
		elevatorSetpoints[ElevatorSetpoint.TOTE_4.ordinal()]= topSoftLimit + GetConfig("tote4", 80);
		
		elevatorSetpoints[ElevatorSetpoint.HOME_TOTE.ordinal()] = topSoftLimit + GetConfig("home_tote", 2000);
		elevatorSetpoints[ElevatorSetpoint.HOME_UPRIGHT_CONTAINER.ordinal()] = topSoftLimit + GetConfig("home_upright_container", 2000);
		elevatorSetpoints[ElevatorSetpoint.HOME_SIDEWAYS_CONTAINER.ordinal()] = topSoftLimit + GetConfig("home_sideways_container", 2200);
		
		elevatorSetpoints[ElevatorSetpoint.COLLECT_TOTE.ordinal()] = topSoftLimit + GetConfig("collect_tote", 2000);
		elevatorSetpoints[ElevatorSetpoint.COLLECT_UPRIGHT_CONTAINER.ordinal()] = topSoftLimit + GetConfig("collect_upright_container", 2050);
		elevatorSetpoints[ElevatorSetpoint.COLLECT_SIDEWAYS_CONTAINER.ordinal()] = topSoftLimit + GetConfig("collect_sideways_container", 1800);
		elevatorSetpoints[ElevatorSetpoint.COLLECT_ADDITIONAL.ordinal()] = topSoftLimit + GetConfig("collect_additional", 1600);

		elevatorSetpoints[ElevatorSetpoint.GRAB_TOTE.ordinal()]= topSoftLimit + GetConfig("grab_tote", 2100);
		elevatorSetpoints[ElevatorSetpoint.GRAB_SIDEWAYS_CONTAINER.ordinal()]= topSoftLimit + GetConfig("grab_sideways_container", 2400);
		
		elevatorSetpoints[ElevatorSetpoint.HUMAN_LOAD_PREPARE.ordinal()]= topSoftLimit + GetConfig("human_load_prepare", 1200);
		elevatorSetpoints[ElevatorSetpoint.HUMAN_LOAD_GRAB.ordinal()]= topSoftLimit + GetConfig("human_load_grab", 1800);
		
		elevatorSetpoints[ElevatorSetpoint.SWEEP_CONTAINER.ordinal()]= topSoftLimit + GetConfig("sweep_container", 1700);
	}
	
	
}
