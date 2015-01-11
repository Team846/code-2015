package components;

import java.util.ArrayList;

import driverstation.GameState;
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
		System.out.println("Created component: " + name);
	}
	
	public static void CreateComponents()
	{
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
			if (true/*digitalIn == -1 || DriverStation.getInstance().getDigitalIn(digitalIn)*/) //TODO: find digital in replacement
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

	String GetName()
	{
		return name;
	}

	int GetDigitalIn()
	{
		return digitalIn;
	}

}
