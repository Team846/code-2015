
package com.lynbrookrobotics.frc2015.config;

import java.util.*;

public interface Configurable {
	
	public void Configure();
	
	public default int GetConfig(String key, int defaultValue)
	{
		String simpleClassName = this.getClass().getName();
		simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.')+1);
		return ConfigRuntime.Instance().Get(simpleClassName,key,defaultValue);
	}
	
	public default double GetConfig(String key, double defaultValue)
	{
		String simpleClassName = this.getClass().getName();
		simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf('.')+1);
		return ConfigRuntime.Instance().Get(simpleClassName,key,defaultValue);
	}
}

