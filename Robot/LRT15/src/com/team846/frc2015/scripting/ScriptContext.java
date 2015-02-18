package com.team846.frc2015.scripting;

public interface ScriptContext {
	
	boolean load(String script);
	boolean loadFromFile(String location);
	
	ScriptExecutionState step();
}
