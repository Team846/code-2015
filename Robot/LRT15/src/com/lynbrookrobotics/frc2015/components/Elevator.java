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

	private int topLimit;
	private int bottomLimit;
	
	private CANTalon motorA;
	private CANTalon motorB;
	
	private AnalogInput elevatorPot;
	
	private int[] elevatorSetpoints;
	
	double Kp;

	public Elevator() {
		super("Elevator", DriverStationConfig.DigitalIns.NO_DS_DI);
		
		motorA = new CANTalon(
				ConfigPortMappings.Instance().Get("CAN/ELEVATOR_MOTOR_A"));
		motorB = new CANTalon(
				ConfigPortMappings.Instance().Get("CAN/ELEVATOR_MOTOR_B"));
		
		elevatorPot = SensorFactory.GetAnalogInput(
				ConfigPortMappings.Instance().Get("Analog/ELEVATOR_POT"));
		
		elevatorData = ElevatorData.get();
		
		Kp = 1.0;
		
		elevatorSetpoints = new int[]{10,20,30,40,50};
		
		ConfigRuntime.Register(this);
	}

	@Override
	protected void UpdateEnabled() {

		int curPos = elevatorPot.getAverageValue();
		
		if(curPos >= topLimit || curPos <= bottomLimit)
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
			float posErr = elevatorSetpoints[elevatorData.getDesiredSetpoint().ordinal()] - curPos;
			double speed = posErr * Kp;
			motorA.set(speed);
			motorB.set(speed);
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
		GetConfig("topLimit", 100);
		GetConfig("bottomLimit", 10);
		
		GetConfig("Kp", 1.0);
	}


}
