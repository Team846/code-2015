package com.team846.frc2015.components;
import com.team846.frc2015.actuators.LRTSpeedController;
import com.team846.frc2015.actuators.LRTTalon;
import com.team846.frc2015.componentData.ComponentData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.ElevatorData.ElevatorControlMode;
import com.team846.frc2015.componentData.ElevatorData.ElevatorSetpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.Configurable;
import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.control.PID;
import com.team846.frc2015.dashboard.DashboardLogger;
import com.team846.frc2015.dashboard.IntegerLog;
import com.team846.frc2015.log.AsyncPrinter;
import com.team846.frc2015.sensors.SensorFactory;
import com.team846.frc2015.utils.MathUtils;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Elevator extends Component implements Configurable {

	private ElevatorData elevatorData;

	private int topSoftLimit;
	private int bottomSoftLimit;
	
	private CANTalon motorA;
	private CANTalon motorB;
	
	private AnalogInput elevatorPot;
	
	private int[] elevatorSetpoints;
	
	private int errorThreshold;
	
	double positionGain;

	public Elevator() {
		super("Elevator", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		motorA = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_A"));
		motorB = new CANTalon(
				ConfigPortMappings.Instance().get("CAN/ELEVATOR_MOTOR_B"));
		
		elevatorPot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().get("Analog/ELEVATOR_POT"));
		
		elevatorData = ElevatorData.get();
		
		positionGain = 0;
		
		elevatorSetpoints = new int[]{0,0,0,0,0};
		
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() {

		int currentPosition = elevatorPot.getAverageValue();
		elevatorData.setCurrentPosition(currentPosition);
		
		DashboardLogger.getInstance().log(new IntegerLog("elevator-pot", currentPosition));
		
		if(currentPosition >= topSoftLimit || currentPosition <= bottomSoftLimit)
		{
			AsyncPrinter.error("Elevator in invalid state! Disable and fix");
			motorA.set(0.0);
			motorB.set(0.0);
			return;
		}
		if(elevatorData.getControlMode() == ElevatorControlMode.SPEED)
		{
			motorA.set(elevatorData.getSpeed());
			motorB.set(elevatorData.getSpeed());
		}
		else if(elevatorData.getControlMode() == ElevatorControlMode.POSITION)
		{
			int desiredPos = (int) Rescale(elevatorData.getDesiredPosition(), 0, 1, bottomSoftLimit, topSoftLimit);
			int posErr = desiredPos - currentPosition;
			double speed = Math.abs(posErr) < errorThreshold ? 0.0 : posErr / 1023.0;
			motorA.set(speed * positionGain);
			motorB.set(speed * positionGain);
		}
		else
		{
			double posErr = elevatorSetpoints[elevatorData.getDesiredSetpoint().ordinal()] - currentPosition;
			double speed = Math.abs(posErr) < errorThreshold ? 0.0 : posErr / 1023.0;
			if(speed == 0.0)
				elevatorData.setCurrentPosition(elevatorData.getDesiredSetpoint());
			else
				elevatorData.setCurrentPosition(ElevatorSetpoint.NONE);
			motorA.set(speed * positionGain);
			motorB.set(speed * positionGain);
		}
	
	}

	@Override
	protected void UpdateDisabled() {
		motorA.set(0.0);
		motorB.set(0.0);
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {
	}

	@Override
	public void Configure() {
		topSoftLimit = GetConfig("topLimit", 100);
		topSoftLimit = GetConfig("bottomLimit", 10);
		
		positionGain = GetConfig("positionGain", 1.0);
		
		errorThreshold = GetConfig("errorThreshold", 15);
		
		elevatorSetpoints[ElevatorSetpoint.GROUND.ordinal()] = GetConfig("ground", 10);
		elevatorSetpoints[ElevatorSetpoint.HOME.ordinal()] = GetConfig("home", 10);
		elevatorSetpoints[ElevatorSetpoint.NONE.ordinal()] = GetConfig("ground", 0);
		elevatorSetpoints[ElevatorSetpoint.TOTE_1.ordinal()] = GetConfig("tote1", 50);
		elevatorSetpoints[ElevatorSetpoint.TOTE_2.ordinal()]= GetConfig("tote2", 60);
		elevatorSetpoints[ElevatorSetpoint.TOTE_3.ordinal()]= GetConfig("tote3", 80);
		elevatorSetpoints[ElevatorSetpoint.TOTE_4.ordinal()]= GetConfig("tote4", 80);
	}
	
	//TODO: put into math util
	private static double Rescale(double d, double min0, double max0, double min1, double max1)
	{
		if (max0 == min0)
			return min1;
		d = MathUtils.clamp(d, min0, max0);
		return (d - min0) * (max1 - min1) / (max0 - min0) + min1;
	}
}
