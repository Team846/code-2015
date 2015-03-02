package com.team846.frc2015.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.lang.NotImplementedException;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.log.AsyncPrinter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous extends Sequential
{

	private double autonomousStartTime = 0.0;

	public Autonomous()
	{
		super("Autonomous");
		autonomousStartTime = 0.0;
	}

	public boolean Start()
	{
		autonomousStartTime = Timer.getFPGATimestamp();

		ClearSequence();

		try
		{
			loadRoutine(FindRoutine((int)SmartDashboard.getNumber("DB/Slider 0"))); // WARNING: Rounds the current value DOWN
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		// TODO: Configurable routine

		ConfigRuntime.ConfigureAll();

		return super.Start();
	}

	public void AllocateResources()
	{
		AllocateResource(ControlResource.DRIVE);
		AllocateResource(ControlResource.TURN);
		AllocateResource(ControlResource.ELEVATOR);
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
	}

	private void loadRoutine(File file)
	{
		AsyncPrinter.println("Loading autonomous file:" + file.getName());
		Scanner in = null;
		try
		{
			in = new Scanner(file);
		} catch (FileNotFoundException e)
		{
			AsyncPrinter.error("Cannot open autonomous file: " + file);
			e.printStackTrace();
		}

		while (in.hasNext())
		{
			String line = in.nextLine();

			String[] parallelSplit = line.split("\\|\\|"); // splits at "||" for parallel routines
			ArrayList<Automation> parallelRoutines = new ArrayList<Automation>(
					parallelSplit.length);

			for (int i = 0; i < parallelSplit.length; i++)
			{
				String automation = parallelSplit[i].trim();

				String[] commandSplit = automation.split("\\("); // splits at "(" separating command name + args

				String command = commandSplit[0];

				String args = commandSplit[1].substring(0,
						commandSplit[1].length() - 1); //getting rid of last paren
				String[] argsSplit = args.split(",");

				for (int argsI = 0; argsI < argsSplit.length; argsI++)
					argsSplit[argsI] = argsSplit[argsI].trim();

				try
				{
					switch (command)
					{

					case "elevate":
						switch (argsSplit.length)
						{
						case 1:
							parallelRoutines.set(i, new Elevate(Integer.parseInt(argsSplit[0])));
							break;
						}
						break;

					case "extendCarriage":
						switch (argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new ExtendCarriage());
							break;
						case 1:
							parallelRoutines.set(i, new ExtendCarriage(Double.parseDouble(argsSplit[0])));
							break;
						}
						break;

					case "loadTote":
						switch (argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new LoadTote());
							break;
						}
						break;

					case "loadUprightContainer":
						switch (argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new LoadUprightContainer());
							break;
						}
						break;
						
					case "loadSidewaysContainer":
						switch(argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new LoadSidewaysContainer());
							break;
						}

					case "releaseStack":
						switch (argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new ReleaseStack());
							break;
						}
						break;
					case "drive":
						switch (argsSplit.length)
						{
						case 1:
							parallelRoutines.set(i, new Drive(Double.parseDouble(argsSplit[0])));
							break;
						case 2:
							parallelRoutines.set(i, new Drive(Double.parseDouble(argsSplit[0])
									,Double.parseDouble(argsSplit[1])));
							break;
						case 3:
							parallelRoutines.set(i, new Drive(Double.parseDouble(argsSplit[0]),
									Double.parseDouble(argsSplit[1]),
									Double.parseDouble(argsSplit[2])));
							break;
						case 4:
							parallelRoutines.set(i, new Drive(Double.parseDouble(argsSplit[0]),
									Double.parseDouble(argsSplit[1]),
									Double.parseDouble(argsSplit[2]),
									Boolean.parseBoolean(argsSplit[3])));
							break;
						}
						break;
					case "turn":
						switch (argsSplit.length)
						{
						case 0:
							parallelRoutines.set(i, new Turn());
							break;
						case 3:
							parallelRoutines.set(i, new Turn(Double.parseDouble(argsSplit[0]),
									Double.parseDouble(argsSplit[1]),
									Double.parseDouble(argsSplit[2])));
							break;
						}
						break;
					case "pause":
						switch (argsSplit.length)
						{
						case 1:
							parallelRoutines.set(i, new Pause(Double.parseDouble(argsSplit[0])));
							break;
						}
						break;
					}
				} catch (Exception e)
				{
					AsyncPrinter.error(e.getMessage());
				}
			}

			if (parallelRoutines.size() == 1)
			{
				AddAutomation(parallelRoutines.get(0));
			} else
			{
				AddAutomation(new Parallel("routineParallel", parallelRoutines));
			}
		}
	}

	private boolean parseBoolean(String string) throws Exception
	{
		if (string == "true")
		{
			return true;
		} else if (string == "false")
		{
			return false;
		} else
		{
			throw new Exception("unable to parse boolean: " + string);
		}
	}

	// private void LoadRoutine(String path)
	// {
	// Scanner in = null;
	// try {
	// in = new Scanner(new File(path));
	// } catch (FileNotFoundException e) {
	// AsyncPrinter.error("Cannot open autonomous file path: " + path);
	// e.printStackTrace();
	// }
	// int lineNumber = 1;
	// while(in.hasNext())
	// {
	// String line = in.nextLine();
	// line = line.substring(0, line.indexOf('#'));
	// line = line.replaceAll("\\s+", "");
	// if(line.length() == 0)
	// continue;
	//
	// ArrayList<String> routineList = new ArrayList<String>();
	// int pos;
	//
	// while(line.contains("),"))
	// {
	// pos = line.indexOf("),");
	// routineList.add(line.substring(0, pos + 1));
	// line = line.substring(pos + 2); //TODO: uncertain
	// }
	// routineList.add(line);
	// ArrayList<Automation> parallelRoutines = new ArrayList<Automation>();
	// for(String s : routineList)
	// {
	// Automation current;
	// String command, args;
	// ArrayList<String> argList = new ArrayList<String>();
	// String temp;
	// command = s.substring(0, s.indexOf('('));
	// args = s.substring( s.indexOf('(') + 1, s.lastIndexOf(')'));
	// while(args.contains(","))
	// {
	// argList.add(args.substring(0, args.indexOf(',')));
	// args = args.substring(args.indexOf(',')+1);
	// }
	// boolean failed = false;
	//
	// //COMMAND CASES
	// if(true) //TODO:add automation cases
	// ;
	// else
	// {
	// AsyncPrinter.warn("Unknwon routine " + command + " on line " + lineNumber
	// + " ignoring");
	// continue;
	// }
	// if(failed)
	// {
	// AsyncPrinter.warn("Incorrect number of args for " + command + " on line "
	// + lineNumber + " ignoring");
	// continue;
	// }
	// if(parallelRoutines.size() > 1)
	// {
	// AddAutomation(new Parallel("AutonomousParllel", parallelRoutines));
	// }
	// else if(parallelRoutines.size() == 1)
	// {
	// AddAutomation(parallelRoutines.get(0));
	// }
	// lineNumber++;
	//
	// }
	// AsyncPrinter.info("Done loading auto routine at " + path);
	// in.close();
	//
	//
	// }
	//
	// }

	private File FindRoutine(int routineNumber) throws FileNotFoundException
	{
		File folder = new File("/home/lvuser/config/auto/");
		File[] routines = folder.listFiles();
		for (File routine: routines) {
			if (routine.getName().endsWith(routineNumber + ".routine")) {
				return routine;
			}
		}
		
		throw new FileNotFoundException("Unable to load routine with id: " + routineNumber);
	}

}
