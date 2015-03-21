package com.team846.frc2015.automation;

import com.team846.frc2015.config.ConfigRuntime;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import com.team846.frc2015.scripting.LuaContext;
import com.team846.frc2015.scripting.ScriptExecutionState;

public class LuaScript extends Automation {

	private LuaContext mLua;
	private boolean mAllocateError;
	
	private Automation mCurrentAutomation;
	
	public LuaScript(String file) {
		super("LuaScript", RoutineOption.REQUIRES_ABORT_CYCLES, RoutineOption.QUEUE_IF_BLOCKED);
		
		mLua = new LuaContext();
		mLua.loadFromFile(file);
		
		mAllocateError = false;
	}

	@Override
	public void AllocateResources() {
		Varargs resources = mLua.exec("allocateResources", LuaValue.NONE);
		
		if(!resources.istable(1)) {
			mAllocateError = true;
			return;
		}
		
		LuaTable resourcesTable = (LuaTable)resources;
		
		for(int i = 1; i <= resourcesTable.length(); i++) {
			String resource = resourcesTable.get(i).toString();
			
			switch(resource) {
			case "DRIVE":
				AllocateResource(ControlResource.DRIVE);
				break;
			case "TURN":
				AllocateResource(ControlResource.TURN);
				break;
			case "STRAFE":
				AllocateResource(ControlResource.STRAFE);
				break;
			case "COLLECTOR_ARMS":
				AllocateResource(ControlResource.COLLECTOR_ARMS);
				break;
			case "COLLECTOR_ROLLERS":
				AllocateResource(ControlResource.COLLECTOR_ROLLERS);
				break;
			case "ELEVATOR":
				AllocateResource(ControlResource.ELEVATOR);
				break;
			case "CARRIAGE_HOOKS":
				AllocateResource(ControlResource.CARRIAGE_HOOKS);
				break;
			case "CARRIAGE_EXTENDER":
				AllocateResource(ControlResource.CARRIAGE_EXTENDER);
				break;
			}
		}
	}

	@Override
	protected boolean Start() {
		// check if allocate failed
		if(mAllocateError) return false;
		
		// bindings start here
		// end
		
		return true;
	}

	@Override
	protected boolean Abort() {
		if(mCurrentAutomation != null) {
			return mCurrentAutomation.AbortAutomation(GetAbortEvent());
		}
		
		return true;
	}

	@Override
	protected boolean Run() {
		if(mCurrentAutomation == null || mCurrentAutomation.Update()) {
			// ready for next step
			
			// resume the script
			ScriptExecutionState state = mLua.step();
			
			switch(state) {
			case EXEC_ERROR:
				// ya done goofed now, son
				return true; // houston, abort!!
			case PAUSED:
				// script has yielded control back to us
				if(mCurrentAutomation == null) {
					// ...?  don't do anything -- next script segment will be run on next call to Run() because mCurrentAutomation == NULL
				} else {
					ConfigRuntime.ConfigureAll();
					
					if(!mCurrentAutomation.StartAutomation(GetStartEvent())) {
						// quietly fail
						mCurrentAutomation = null; // TODO: make this action configurable
					}
				}
			}
			
			return state == ScriptExecutionState.FINISHED;
		} else {
			// routine is still running, so indicate we're still doing work
			return false;
		}
	}

}
