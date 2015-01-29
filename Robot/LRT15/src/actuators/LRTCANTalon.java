package actuators;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Talon;


public class LRTCANTalon extends Actuator
{
	private double speed;
	
	private CANTalon talon;
	
	DigitalOutput brake_jumper;
	LRTSpeedController.NeutralMode neutral;
	
	public static ArrayList<LRTCANTalon> talon_list = new ArrayList<LRTCANTalon>();

	public LRTCANTalon(String name, int Channel, int jumpChannel)
	{
		
		super("LRTCANTalon" + name);
		
		talon = new CANTalon(Channel);
		// TODO Auto-generated constructor stub
		
		brake_jumper = (jumpChannel != 0 ? new DigitalOutput(jumpChannel) : null);
		neutral = LRTSpeedController.NeutralMode.kNeutralMode_Coast;
		speed = 0.0;
		talon_list.add(this);
		
		System.out.println("Constructed LRTCANTalon" + name+" on channel " + Channel);
		
	}

	@Override
	public void Output() 
	{
		// TODO Auto-generated method stub
	}
	
	public void ConfigFwdLimitSwitchClosed(boolean normallyOpen){
		talon.ConfigFwdLimitSwitchNormallyOpen(normallyOpen);
	}
	
	public boolean getFwdLimit(){
		return talon.isFwdLimitSwitchClosed();
	}
	
	public void ConfigRevLimitSwitchClosed(boolean normallyOpen){
		talon.ConfigRevLimitSwitchNormallyOpen(normallyOpen);
	}
	
	public boolean getRevLimit(){
		return talon.isRevLimitSwitchClosed();
	}
	
	public void enableControl(){
		talon.enableControl();
	}
	
	public void disableControl(){
		talon.disableControl();
		
	}
	
	public void clearAccum(){
		talon.ClearIaccum();
	}
	
	public void clearStickyFaults(){
		talon.clearStickyFaults();
	}
	
	public void delete(){
		talon.delete();
	}
	
	public void writePid(double output){
		talon.pidWrite(output);
		
	}
	
	public void setProfile(int profile){
		talon.setProfile(profile);
	}
	
	public void setControlMode(ControlMode control){
		talon.changeControlMode(control);
	}
	
	public ControlMode getControlMode(){
		return talon.getControlMode();
	}
	
	public void SetDutyCycle(double speed)
	{
		this.speed = speed;
	}
	
	public double GetDutyCycle()
	{
		return speed;
	}
	
	public double GetHardwareValue()
	{
		return talon.get();
	}
	
	public void Disable()
	{
		speed = 0.0;
	}
	
	public void PIDWrite( float output) 
	{
		SetDutyCycle(output);
	}
	
	public void ConfigNeutralMode(LRTSpeedController.NeutralMode mode)
	{
		neutral = mode;
	}
	
	LRTSpeedController.NeutralMode GetNeutralMode()
	{
		return neutral;
	}
	
	public void Update()
	{
		talon.set(speed);
		
		if (brake_jumper != null)
		{
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Coast)
				brake_jumper.set(true);
			if(neutral == LRTSpeedController.NeutralMode.kNeutralMode_Brake)
				brake_jumper.set(false);
		}
	}
}
