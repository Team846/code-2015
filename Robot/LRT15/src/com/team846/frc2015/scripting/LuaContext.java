package com.team846.frc2015.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaContext implements ScriptContext{
	
	private Globals mGlobals;
	private boolean mRun;
	
	private LuaValue mLuaState;
	private LuaThread mLuaExecutionContext;
	
	public LuaContext() {
		mGlobals = JsePlatform.standardGlobals();
		mRun = false;
	}
	
	public void bind(String name, LuaValue val) {
		mGlobals.set(name, val);
	}
	
	@Override
	public boolean load(String script) {
		LuaValue chunk = mGlobals.load(script);
		return setupContext(chunk);
	}

	@Override
	public boolean loadFromFile(String location) {
		LuaValue chunk = mGlobals.loadfile(location);
		return setupContext(chunk);
	}
	
	@Override
	public ScriptExecutionState step() {
		// TODO: since the context runs synchronously on a separate thread, need to kill the thread upon exit if it hasn't completed
		
		Varargs res = mLuaExecutionContext.resume(LuaValue.NONE); // resume the script
		
		boolean success = res.arg(1).checkboolean(); // check for failure
		if(success) {
			switch(mLuaExecutionContext.state.status) {
			case LuaThread.STATUS_DEAD:
				return ScriptExecutionState.FINISHED;
			case LuaThread.STATUS_SUSPENDED:
				return ScriptExecutionState.PAUSED;
			}
		}
		return ScriptExecutionState.EXEC_ERROR;
	}

	public Varargs exec(String func, Varargs args) {
		return mGlobals.invokemethod(func, args);
	}
	
	private boolean setupContext(LuaValue chunk) {
		mLuaState = chunk;
		chunk.call(); // load script functions and run any procedural calls
		LuaValue mainMethod = mGlobals.get("main");
		mLuaExecutionContext = new LuaThread(mGlobals, mainMethod);
		
		return true;
	}
}
