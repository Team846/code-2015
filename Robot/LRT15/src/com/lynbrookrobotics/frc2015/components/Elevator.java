package com.lynbrookrobotics.frc2015.components;
import com.lynbrookrobotics.frc2015.actuators.LRTSpeedController;
import com.lynbrookrobotics.frc2015.actuators.LRTTalon;
import com.lynbrookrobotics.frc2015.componentData.ComponentData;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.ControlMode;
import com.lynbrookrobotics.frc2015.componentData.ElevatorData.Setpoint;
import com.lynbrookrobotics.frc2015.config.ConfigPortMappings;
import com.lynbrookrobotics.frc2015.config.ConfigRuntime;
import com.lynbrookrobotics.frc2015.config.Configurable;
import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.control.PID;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;
import com.lynbrookrobotics.frc2015.sensors.SensorFactory;

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
		
		if(currentPosition >= topSoftLimit || currentPosition <= bottomSoftLimit)
		{
			AsyncPrinter.error("Elevator in invalid state! Disable and fix");
			motorA.set(0.0);
			motorB.set(0.0);
			return;
		}
		if(elevatorData.getControlMode() == ControlMode.SPEED)
			
		{
			motorA.set(elevatorData.getSpeed());
			motorB.set(elevatorData.getSpeed());
		}
		else
		{
			float posErr = elevatorSetpoints[elevatorData.getDesiredSetpoint().ordinal()] - currentPosition;
			double speed = Math.abs(posErr) < errorThreshold ? 0.0 : posErr;
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
		
		errorThreshold = GetConfig("errorThreshold", 5);
		
		elevatorSetpoints[Setpoint.GROUND.ordinal()] = GetConfig("ground", 10);
		elevatorSetpoints[Setpoint.TOTE_1.ordinal()] = GetConfig("tote1", 50);
		elevatorSetpoints[Setpoint.TOTE_2.ordinal()]= GetConfig("tote2", 60);
		elevatorSetpoints[Setpoint.TOTE_3.ordinal()]= GetConfig("tote3", 80);
		elevatorSetpoints[Setpoint.TOTE_4.ordinal()]= GetConfig("tote4", 80);
	}


}
