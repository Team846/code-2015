package com.team846.frc2015.automation;

import com.team846.frc2015.componentData.CarriageHooksData;
import com.team846.frc2015.componentData.CollectorArmData;
import com.team846.frc2015.componentData.CollectorRollersData;
import com.team846.frc2015.componentData.ElevatorData;
import com.team846.frc2015.componentData.CollectorArmData.Position;
import com.team846.frc2015.componentData.CollectorRollersData.Direction;
import com.team846.frc2015.componentData.ElevatorData.ControlMode;
import com.team846.frc2015.componentData.ElevatorData.Setpoint;
import com.team846.frc2015.config.ConfigPortMappings;
import com.team846.frc2015.sensors.SensorFactory;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

public class Collect extends Automation
{
	private AnalogInput proximitySensor = SensorFactory.GetAnalogInput(
			ConfigPortMappings.Instance().get("Analog/COLLECTOR_SENSOR"));
	
	private CollectorRollersData rollersData = CollectorRollersData.get();
	private CollectorArmData armData = CollectorArmData.get();
	private ElevatorData elevatorData = ElevatorData.get();
	private CarriageHooksData hooksData = CarriageHooksData.get();
	
	private int collectThreshold = 5;
	
	private boolean dropCarriage;

	private boolean enableBackHooks;
	
	public Collect()
	{
		this(false, false);
	}
	
	public Collect(boolean dropCarriage)
	{
		this(dropCarriage, false);
	}
	
	public Collect(boolean dropCarriage, boolean enableBackHooks)
	{
		super("Collect", false, true, true); //TODO:verify order
		this.dropCarriage = dropCarriage;
		this.enableBackHooks = enableBackHooks;
	}

	@Override
	public void AllocateResources()
	{
		if(dropCarriage)
			AllocateResource(ControlResource.ELEVATOR);
		if(enableBackHooks)
			AllocateResource(ControlResource.CARRIAGE_HOOKS);
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
	}

	@Override
	protected boolean Start()
	{
		return true;
	}

	@Override
	protected boolean Abort()
	{
		rollersData.setRunning(false);
		armData.setDesiredPosition(Position.STOWED);
		return true;
	}

	@Override
	protected boolean Run()
	{
		if(proximitySensor.getAverageValue() < collectThreshold)
		{
			rollersData.setRunning(false);
			return true;
		}
		
		if(dropCarriage)
		{
			elevatorData.setControlMode(ControlMode.SETPOINT);
			elevatorData.setSetpoint(Setpoint.GROUND);
		}
		
		if(enableBackHooks)
		{
			hooksData.setBackHooksState(CarriageHooksData.Position.ENABLED);
		}
		
		armData.setDesiredPosition(Position.EXTEND);
		rollersData.setRunning(true);
        rollersData.setDirection(Direction.INTAKE);
		rollersData.setSpeed(1.0);
			
		return false;
	}
}