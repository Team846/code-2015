package com.team846.frc2015.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang.NotImplementedException;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.log.AsyncPrinter;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends Sequential {

	private double autonomousStartTime = 0.0;

	Autonomous() {
		super("Autonomous");
		autonomousStartTime=0.0;
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
		AllocateResource(ControlResource.DRIVE);
		AllocateResource(ControlResource.TURN);
		AllocateResource(ControlResource.ELEVATOR);
		AllocateResource(ControlResource.COLLECTOR_ARMS);
		AllocateResource(ControlResource.COLLECTOR_ROLLERS);
		AllocateResource(ControlResource.CARRIAGE_EXTENDER);
		AllocateResource(ControlResource.CARRIAGE_HOOKS);
	}
	
	private void loadRoutine(String path) {
		Scanner in = null;
		try {
			 in = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			AsyncPrinter.error("Cannot open autonomous file path: " + path);
			e.printStackTrace();
		}
		
		while (in.hasNext()) {
			String line = in.nextLine();
			String[] parallelSplit = line.split("||");
			Automation[] parallelRoutines = new Automation[parallelSplit.length];
			for (int i = 0; i < parallelSplit.length; i++) {
				String automation = parallelSplit[i].trim();
				String[] commandSplit = automation.split("(");
				String args = commandSplit[1].substring(0, commandSplit[1].length() - 1);
				String[] argsSplit = args.split(",");
			}
		}
	}
	
	private void LoadRoutine(String path)
	{
		Scanner in = null;
		try {
			 in = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			AsyncPrinter.error("Cannot open autonomous file path: " + path);
			e.printStackTrace();
		}
		int lineNumber = 1;
		while(in.hasNext())
		{
			String line = in.nextLine();
			line = line.substring(0, line.indexOf('#'));
			line = line.replaceAll("\\s+", "");
			if(line.length() == 0)
				continue;
			
			ArrayList<String> routineList = new ArrayList<String>();
			int pos;
			
			while(line.contains("),"))
			{
				pos = line.indexOf("),");
				routineList.add(line.substring(0, pos + 1));
				line = line.substring(pos + 2); //TODO: uncertain
			}
			routineList.add(line);
			ArrayList<Automation> parallelRoutines = new ArrayList<Automation>();
			for(String s : routineList)
			{
				Automation current;
				String command, args;
				ArrayList<String> argList = new ArrayList<String>();
				String temp;
				command = s.substring(0, s.indexOf('('));
				args = s.substring( s.indexOf('(') + 1, s.lastIndexOf(')'));
				while(args.contains(","))
				{
					argList.add(args.substring(0, args.indexOf(',')));
					args = args.substring(args.indexOf(',')+1);
				}
				boolean failed = false;
				
				//COMMAND CASES
				if(true) //TODO:add automation cases
					;
				else
				{
					AsyncPrinter.warn("Unknwon routine " + command + " on line " + lineNumber + " ignoring");
					continue;
				}
				if(failed)
				{
					AsyncPrinter.warn("Incorrect number of args for " + command + " on line " + lineNumber + " ignoring");					
					continue;
				}
				if(parallelRoutines.size() > 1)
				{
					AddAutomation(new Parallel("AutonomousParllel", parallelRoutines));
				}
				else if(parallelRoutines.size() == 1)
				{
					AddAutomation(parallelRoutines.get(0));
				}
				lineNumber++;
				
			}
			AsyncPrinter.info("Done loading auto routine at " + path);
			in.close();
			
			
		}
		
	}
	
	private String FindRoutine(int routineNumber)
	{
		throw new NotImplementedException("Customizable routine not ");
	}
	

}