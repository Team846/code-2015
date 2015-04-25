package com.team846.frc2015.components;

import java.util.ArrayList;

import com.team846.frc2015.config.DriverStationConfig;
import com.team846.frc2015.driverstation.GameState;
import com.team846.frc2015.utils.AsyncPrinter;
import com.team846.robot.RobotState;

public abstract class Component 
{
	
	private static final ArrayList<Component> component_list = new ArrayList<Component>();
	private final String name;
	private final int digitalIn;
	private boolean lastEnabled;
	
	abstract protected void UpdateEnabled();
	
	
	abstract protected void UpdateDisabled();
	
	
	abstract protected void OnEnabled();
	
	
	abstract protected void OnDisabled();
	
	/**Constructs base component with associated 
	 * NOTE: As of 2015, digitalIns are not supported
	 * All components must be e
	 * @param name name of the component
	 * @param driverStationDigitalIn driverstation in
	 */
    Component(String name, int driverStationDigitalIn)
	{
    	if(name == null)
    	{
    		String simpleClassName = this.getClass().getName();
    		this.name = simpleClassName.substring(simpleClassName.lastIndexOf('.')+1);
    	}
    	else
    		this.name = name;
    	
		digitalIn = driverStationDigitalIn;
		lastEnabled = false;
		AsyncPrinter.info("Created component: " + name);
	}
    
    Component(int driverStationDigitalIn)
    {
    	this(null,driverStationDigitalIn);
    }
	
	public static void CreateComponents()
	{
		component_list.add(new Drivetrain());
		component_list.add(new CollectorRollers());
		component_list.add(new CollectorArms());
		component_list.add(new CarriageExtender());
		component_list.add(new CarriageHooks());
		component_list.add(new Elevator());
		component_list.add(new ContainerArm());
	}
	

	public static void UpdateAll()
	{
		for (Component c : component_list)
		{
			c.Update();
		}
	}
	
	void Update()
	{
		if (RobotState.Instance().GameMode() != GameState.DISABLED)
		{
			if (digitalIn == DriverStationConfig.DigitalIns.NO_DS_DI )
			{
				if (!lastEnabled)
					OnEnabled();
				UpdateEnabled();
				lastEnabled = true;
			}
			else
			{
				if (lastEnabled)
					OnDisabled();
				UpdateDisabled();
				lastEnabled = false;
			}
		}
		else
		{
			if (lastEnabled)
				OnDisabled();
			UpdateDisabled();
			lastEnabled = false;
		}
	}

	public String GetName()
	{
		return name;
	}

	public int GetDigitalIn()
	{
		return digitalIn;
	}

}
