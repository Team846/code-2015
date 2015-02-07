package com.lynbrookrobotics.frc2015.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang.NotImplementedException;

import com.lynbrookrobotics.frc2015.config.ConfigRuntime;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends Sequential {

	private double autonomousStartTime = 0.0;

	Autonomous(String name, boolean queueIfBlocked, boolean restartable) {
		super(name, queueIfBlocked, restartable);
		// TODO Auto-generated constructor stub
	}
	
	public boolean Start()
	{
		autonomousStartTime  = Timer.getFPGATimestamp();
		
		ClearSequence();
		
		LoadRoutine(FindRoutine(1)); //TODO: Configurable routine
		
		ConfigRuntime.ConfigureAll();
		
		return ((Sequential)this).Start();
	}
	
	public void AllocateResources()
	{
		
	}
	
	private void LoadRoutine(String path)
	{
		Scanner in = null;
		try {
			 in = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int lineNumber = 1;
		while(in.hasNext())
		{
			String line = in.nextLine();
			line = line.substring(0, line.indexOf('#'));
			
			if(line.length() == 0)
				continue;
			
			ArrayList<String> routineList = new ArrayList<String>();
			int pos;
			
			while(line.contains("),"))
			{
				pos = line.indexOf("),");
				//TODO:erase line
			}
			routineList.add(line);
			
			
		}
		
	}
	
	private String FindRoutine(int routineNumber)
	{
		throw new NotImplementedException("Customizable routine not ");
	}
	

}
