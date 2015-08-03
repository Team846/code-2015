package com.team846.frc2015.control;

public class PID {
	
	private double proportional_gain;
	private double integral_gain;
	private double derivative_gain;
	private double feedforward_gain;
	
	private double integral_decay;
	private double input;
	private double output;
	private double setpoint;
	
	private double error;
	private double prev_error;
	private double acc_error;
	
	private boolean is_feed_forward;
	private boolean enabled;
	
	private boolean IIREnabled;
	private final RunningSum runningSum;

	PID(double p_gain, double i_gain, double d_gain, double ff_gain,
			double i_decay, boolean feedforward, double filterFreq)
	{
		setParameters(p_gain, i_gain, d_gain, ff_gain, i_decay, feedforward, filterFreq);
		runningSum = new RunningSum(IIR_DECAY(filterFreq));
		IIREnabled = false;
	}

	public PID() 
	{
		runningSum = new RunningSum(IIR_DECAY(7.0));
		setParameters(0, 0, 0,0,0,false,0);
		IIREnabled = false;
	}
	
	private double IIR_DECAY (double freq)
	{
		return 2 * Math.PI * freq / 50;
	}

	public void setParameters(double p_gain, double i_gain, double d_gain,
			double ff_gain, double i_decay, boolean feedforward, double filterFreq)
	{
		reset();
		proportional_gain = p_gain;
		integral_gain = i_gain;
		derivative_gain = d_gain;
		feedforward_gain = ff_gain;
		integral_decay = i_decay != 1.0 ? i_decay : 0.9999999999999;
		is_feed_forward = feedforward;
		runningSum.setDecayConstant(IIR_DECAY(filterFreq));
		enablePID();
	}

	public double update(double dt)
	{
		error = setpoint - input;

		if (IIREnabled)
			error = runningSum.UpdateSum(error);
		else
			runningSum.Clear();

		// calculate discrete derivative
		double delta = (error - prev_error) / dt;

		// approximate with riemann sum and decay
		acc_error *= integral_decay;
		acc_error += error * dt;
		if (Double.isNaN(acc_error)) // catch NaN
		{
			acc_error = 0;
		}
		double integral = acc_error / (1 - integral_decay);

		// magic PID line
		double PID_output = proportional_gain * (error + integral_gain
				* integral + derivative_gain * delta);

		if (is_feed_forward)
		{
			// feed-forward PID
			output = setpoint * feedforward_gain + PID_output;
		}
		else
		{
			// standard PID
			output = PID_output;
		}

		prev_error = error;

		return output;
	}

	public void setSetpoint(double setpoint)
	{
		this.setpoint = setpoint;
	}

	public void setInput(double input)
	{
		this.input = input;
	}

	public double getProportionalGain()
	{
		return proportional_gain;
	}

	public double getIntegralGain()
	{
		return integral_gain;
	}

	public double getDerivativeGain()
	{
		return derivative_gain;
	}

	public double getFeedForwardGain()
	{
		return feedforward_gain;
	}

	public double getIntegralDecay()
	{
		return integral_decay;
	}

	public double getInput()
	{
		return input;
	}

	public double getSetpoint()
	{
		return setpoint;
	}

	public double getError()
	{
		return error;
	}

	public double getAccumulatedError()
	{
		return acc_error;
	}

	public double getPreviousError()
	{
		return prev_error;
	}

	public double getOutput()
	{
		if (enabled)
			return output;
		else
			return setpoint;
	}

	public void disablePID()
	{
		enabled = false;
	}

	void enablePID()
	{
		enabled = true;
	}

	void reset()
	{
		acc_error = 0.0;
		error = 0.0;
		prev_error = 0.0;
		input = 0.0;
		output = 0.0;
		setpoint = 0.0;
	}

	public void setIIREnabled(boolean enabled)
	{
		IIREnabled = enabled;
		if (!enabled)
			runningSum.Clear();
	}

	public void setIIRDecay(double decay)
	{
		runningSum.setDecayConstant(decay);
	}

}
