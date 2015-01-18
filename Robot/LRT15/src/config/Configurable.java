package config;

import java.util.*;

public class Configurable {
	public final ConfigRuntime  m_config;
	private String m_configSection;
	
	public Configurable(String configSection)
	{
		m_config = ConfigRuntime.Instance();
		m_configSection = configSection;
		ConfigRuntime.Register(this);
	}
	
	//virtual void Configure() = 0;
	
	/*
	public <T> T GetConfig(String key, T defaultValue)
	{
		return m_config.Get<T>(m_configSection,key,defaultValue);
	}
	*/
}
