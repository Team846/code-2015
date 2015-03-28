package com.team846.frc2015.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.RobotConfig;
import com.team846.frc2015.utils.AsyncPrinter;

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

		double loadStartTime = System.currentTimeMillis();
//		try
//		{
			loadRoutine(FindRoutine((int)Math.round(SmartDashboard.getNumber("DB/Slider 0")))); //Rounds value to int
		//} catch (FileNotFoundException e)
		//{
		//	e.printStackTrace();
		//}
		double totalTime = System.currentTimeMillis() - loadStartTime;
		AsyncPrinter.info("Parsed routine in " + totalTime + "ms");

		ConfigRuntime.ConfigureAll();

		return super.Start();
	}

	public void AllocateResources()
	{
		AllocateResource(ControlResource.DRIVE);
		AllocateResource(ControlResource.TURN);
		AllocateResource(ControlResource.STRAFE);
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

		while (in != null && in.hasNext())
		{
			String line = in.nextLine();

			String[] parallelSplit = line.split("\\|\\|"); // splits at "||" for parallel routines
			ArrayList<Automation> parallelRoutines = new ArrayList<Automation>(
					parallelSplit.length);

            for (String aParallelSplit : parallelSplit) {
                String automation = aParallelSplit.trim();

                String[] commandSplit = automation.split("\\("); // splits at "(" separating command name + args

                String command = commandSplit[0];

                String args = commandSplit[1].substring(0,
                        commandSplit[1].length() - 1); //getting rid of last paren
                String[] argsSplit = args.split(",");

                for (int argsI = 0; argsI < argsSplit.length; argsI++)
                    argsSplit[argsI] = argsSplit[argsI].trim();

//				try
//				{
                switch (command) {
                    case "test":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new Elevate(Integer.parseInt(argsSplit[0])));
                                break;
                        }
                        break;

                    case "elevate":
                        switch (argsSplit.length) {
                            case 1:
                                parallelRoutines.add(new Elevate(Integer.parseInt(argsSplit[0])));
                                break;
                        }
                        break;

                    case "extendCarriage":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new ExtendCarriage());
                                break;
                            case 1:
                                parallelRoutines.add(new ExtendCarriage(Double.parseDouble(argsSplit[0])));
                                break;
                        }
                        break;

                    case "loadTote":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new LoadTote(true));
                                break;
                        }
                        break;

                    case "loadUprightContainer":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new LoadUprightContainer(true));
                                break;
                        }
                        break;

                    case "loadSidewaysContainer":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new LoadSidewaysContainer(true));
                                break;
                        }

                    case "releaseStack":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new ReleaseStack());
                                break;
                        }
                        break;
                    case "drive":
                        switch (argsSplit.length) {
                            case 1:
                                parallelRoutines.add(new Drive(Double.parseDouble(argsSplit[0])));
                                break;
                            case 2:
                                parallelRoutines.add(new Drive(Double.parseDouble(argsSplit[0])
                                        , Double.parseDouble(argsSplit[1])));
                                break;
                            case 3:
                                parallelRoutines.add(new Drive(Double.parseDouble(argsSplit[0]),
                                        Double.parseDouble(argsSplit[1]),
                                        Double.parseDouble(argsSplit[2])));
                                break;
                            case 4:
                                parallelRoutines.add(new Drive(Double.parseDouble(argsSplit[0]),
                                        Double.parseDouble(argsSplit[1]),
                                        Double.parseDouble(argsSplit[2]),
                                        Boolean.parseBoolean(argsSplit[3])));
                                break;
                        }
                        break;
                    case "turn":
                        switch (argsSplit.length) {
                            case 0:
                                parallelRoutines.add(new Turn());
                                break;
                            case 3:
                                parallelRoutines.add(new Turn(Double.parseDouble(argsSplit[0]),
                                        Double.parseDouble(argsSplit[1]),
                                        Double.parseDouble(argsSplit[2])));
                                break;
                        }
                        break;
                    case "pause":
                        switch (argsSplit.length) {
                            case 1:
                                parallelRoutines.add(new Pause(Double.parseDouble(argsSplit[0])));
                                break;
                        }
                        break;
//                    case "strafe":
//                        switch (argsSplit.length) {
//                            case 1:
//                                parallelRoutines.add(new Strafe(Double.parseDouble(argsSplit[0])));
//                                break;
//                            case 2:
//                                parallelRoutines.add(new Strafe(Double.parseDouble(argsSplit[0]),
//                                        Double.parseDouble(argsSplit[1])));
//                                break;
//                        }
//                        break;
                    case "load+elevate":
                        switch (argsSplit.length) {
                            case 1:
                                Sequential routine = new Sequential("LoadElevate");
                                routine.AddAutomation(new LoadTote(true));
                                routine.AddAutomation(new Elevate(Integer.parseInt(argsSplit[0])));
                                parallelRoutines.add(routine);
                        }
                        break;
                    case "loadAdditional+elevate":
                        switch (argsSplit.length) {
                            case 1:
                                Sequential routine = new Sequential("LoadAdditionalElevate");
                                routine.AddAutomation(new LoadAdditional(true));
                                routine.AddAutomation(new Elevate(Integer.parseInt(argsSplit[0])));
                                parallelRoutines.add(routine);
                        }
                        break;
                    case "drive+sweep":
                        Parallel routine = new Parallel("DriveSweep", true);
                        switch (argsSplit.length) {
                            case 2:
                                routine.AddAutomation(new Drive(Double.parseDouble(argsSplit[0])));
                                if (argsSplit[1].equals("left"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.LEFT));
                                else if (argsSplit[1].equals("right"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.RIGHT));
                                break;
                            case 3:
                                routine.AddAutomation(new Drive(Double.parseDouble(argsSplit[0])
                                        , Double.parseDouble(argsSplit[1])));
                                if (argsSplit[2].equals("left"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.LEFT));
                                else if (argsSplit[2].equals("right"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.RIGHT));
                                break;
                            case 4:
                                routine.AddAutomation(new Drive(Double.parseDouble(argsSplit[0]),
                                        Double.parseDouble(argsSplit[1]),
                                        Double.parseDouble(argsSplit[2])));
                                if (argsSplit[3].equals("left"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.LEFT));
                                else if (argsSplit[3].equals("right"))
                                    routine.AddAutomation(new Sweep(Sweep.Direction.RIGHT));
                                break;
                            case 5:
                                routine.AddAutomation(new Drive(Double.parseDouble(argsSplit[0]),
                                        Double.parseDouble(argsSplit[1]),
                                        Double.parseDouble(argsSplit[2]),
                                        Boolean.parseBoolean(argsSplit[3])));
                                AsyncPrinter.error("sweep+arg: " + argsSplit[4]);
//							if (argsSplit[4] == "left")
//							{
//
//								AsyncPrinter.error("creatinglef+arg: " + argsSplit[4]);
                                routine.AddAutomation(new Sweep(Sweep.Direction.LEFT));
//							}
//							else if (argsSplit[4] == "right")
//								routine.AddAutomation(new Sweep(Sweep.Direction.RIGHT));
                                break;
                        }
                        parallelRoutines.add(routine);
                        break;
                }

//				} catch (Exception e)
//				{
//					AsyncPrinter.error( e.toString());
//				}
            }
			
			if (parallelRoutines.size() == 0)
			{
				AsyncPrinter.warn("No routine found");
			}
			else if (parallelRoutines.size() == 1)
			{
				System.out.println("routine: " + parallelRoutines.get(0).GetName());
				AddAutomation(parallelRoutines.get(0));
			}
			else
			{
				AsyncPrinter.println(parallelRoutines.toString());
				AddAutomation(new Parallel("routineParallel", parallelRoutines));
			}
		}
		in.close();
	}

	private File FindRoutine(int routineNumber)// throws FileNotFoundException
	{
		File folder = new File(RobotConfig.AUTO_FOLDER_PATH);
		File[] routines = folder.listFiles();
        if (routines != null) {
            for (File routine: routines) {
                if (routine.getName().endsWith(routineNumber + ".routine")) {
                    System.out.println("Reading file: " + routine);
                    return routine;
                }
            }
        }

        return null;//throw new FileNotFoundException("Unable to load routine with id: " + routineNumber);
	}

}
