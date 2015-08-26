package com.team846.frc2015.automation;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.team846.frc2015.config.ConfigRuntime;
import com.team846.frc2015.config.RobotConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Autonomous extends Sequential {

	public Autonomous()
	{
		super("Autonomous");
	}

    public ArrayList<Automation> loadRoutine(String fileName) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
        	System.out.println("haiFOO");
            AutonomousFunctions jsShim = new AutonomousFunctions();
            engine.put("auto", jsShim);

            // TODO: wut is this dumbness... find a fix to not use eval
            engine.eval("var sweep = { direction : com.team846.frc2015.automation.Sweep.Direction } ");
            engine.eval(new FileReader(System.getProperty("user.home") + "/" + fileName + ".routine.js"));
            return jsShim.popRoutine();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) {
        Autonomous self = new Autonomous();
        System.out.println(self.loadRoutine("clubs/frc/foo"));
    }

	public boolean Start() {
		ClearSequence();

        int routineNumber = (int) Math.round(SmartDashboard.getNumber("DB/Slider 0"));
        AddAutomation(loadRoutine("auto/" + routineNumber + ".routine.js"));
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
