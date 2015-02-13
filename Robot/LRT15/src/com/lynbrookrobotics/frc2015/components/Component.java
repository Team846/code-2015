package com.lynbrookrobotics.frc2015.components;

import java.util.ArrayList;

import com.lynbrookrobotics.frc2015.config.DriverStationConfig;
import com.lynbrookrobotics.frc2015.config.RobotConfig;
import com.lynbrookrobotics.frc2015.driverstation.GameState;
import com.lynbrookrobotics.frc2015.log.AsyncPrinter;

import team846.robot.RobotState;
import edu.wpi.first.wpilibj.DriverStation;

public abstract class Component 
{
	
	private static ArrayList<Component> component_list = new ArrayList<Component>();
	private String name;
	private int digitalIn;
	private boolean lastEnabled;
	
	abstract protected void UpdateEnabled();
	
	
	abstract protected void UpdateDisabled();
	
	
	abstract protected void OnEnabled();
	
	
	abstract protected void OnDisabled();
	
	public Component(String name, int driverStationDigitalIn)
	{
		this.name = name;
		digitalIn = driverStationDigitalIn;
		lastEnabled = false;
		AsyncPrinter.println("Created component: " + name);
	}
	
	public static void CreateComponents()
	{
		component_list.add(new Drivetrain());
		component_list.add(new CollectorArms());
		component_list.add(new CollectorRollers());
		component_list.add(new CarriageExtender());
		component_list.add(new CarriageHooks());
		component_list.add(new Elevator());
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
			if (digitalIn == DriverStationConfig.DigitalIns.NO_DS_DI /*|| DriverStation.getInstance().getDigitalIn(digitalIn)*/) //TODO: find digital in replacement
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
