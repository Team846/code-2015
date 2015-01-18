package config;

public class Configurable {
	public final ConfigRuntime  m_config;
	private String m_configSection;
	
	public Configurable(String configSection)
	{
		m_config = ConfigRuntime.Instance();
		m_configSection = configSection;
		ConfigRuntime.Register(this);
	}
	//public virtual ~Configurable();
	
	//virtual void Configure() = 0;
	
	//static <T> GetConfig(String key, T defaultValue)
	//default <T> GetConfig
}
