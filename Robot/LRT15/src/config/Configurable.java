
package config;

import java.util.*;

public interface Configurable {
	
	public void Configure();
	
	public default <T extends Number> T GetConfig(String key, T defaultValue)
	{
		return ConfigRuntime.Instance().Get(this.getClass().getName(),key,defaultValue);
	}
}

