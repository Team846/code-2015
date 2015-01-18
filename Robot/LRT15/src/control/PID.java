package control;

public class PID {
	
	double proportional_gain;
	double integral_gain;
	double derivative_gain;
	double feedforward_gain;
	double integral_decay;
	double input;
	double output;
	double setpoint;
	double error;
	double prev_error;
	double acc_error;
	boolean is_feed_forward;
	boolean enabled;
	
	boolean IIREnabled;
	RunningSum runningSum;

	PID(double p_gain, double i_gain, double d_gain, double ff_gain,
			double i_decay, boolean feedforward, double filterFreq)
	{
		SetParameters(p_gain, i_gain, d_gain, ff_gain, i_decay, feedforward, filterFreq);
		runningSum = new RunningSum(IIR_DECAY(filterFreq));
		IIREnabled = false;
	}

	public PID() 
	{
		runningSum = new RunningSum(IIR_DECAY(7.0));
		SetParameters(0, 0, 0,0,0,false,0);
		IIREnabled = false;
	}
	
	private double IIR_DECAY (double freq)
	{
		return 2 * 3.14159 * freq / 50;
	}

	void SetParameters(double p_gain, double i_gain, double d_gain,
			double ff_gain, double i_decay, boolean feedforward, double filterFreq)
	{
		Reset();
		proportional_gain = p_gain;
		integral_gain = i_gain;
		derivative_gain = d_gain;
		feedforward_gain = ff_gain;
		integral_decay = i_decay != 1.0 ? i_decay : 0.9999999999999;
		is_feed_forward = feedforward;
		runningSum.setDecayConstant(IIR_DECAY(filterFreq));
		EnablePID();
	}

	public double Update(double dt)
	{
		error = setpoint - input;
		//	AsyncPrinter::Printf("Error: %.02f\r\n", error);

		if (IIREnabled)
		{
			error = runningSum.UpdateSum(error);
		}
		else
		{
			runningSum.Clear();
		}

		// calculate discrete derivative
		double delta = (error - prev_error) / dt;

		// approximate with riemann sum and decay
		acc_error *= integral_decay;
		acc_error += error * dt;
		if (acc_error != acc_error) // catch NaN
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

	public void SetSetpoint(double setpoint)
	{
		setpoint = setpoint;
	}

	public void SetInput(double input)
	{
		input = input;
	}

	public double GetProportionalGain()
	{
		return proportional_gain;
	}

	public double GetIntegralGain()
	{
		return integral_gain;
	}

	public double GetDerivativeGain()
	{
		return derivative_gain;
	}

	public double GetFeedForwardGain()
	{
		return feedforward_gain;
	}

	public double GetIntegralDecay()
	{
		return integral_decay;
	}

	public double GetInput()
	{
		return input;
	}

	public double GetSetpoint()
	{
		return setpoint;
	}

	public double GetError()
	{
		return error;
	}

	public double GetAccumulatedError()
	{
		return acc_error;
	}

	public double GetPreviousError()
	{
		return prev_error;
	}

	public double GetOutput()
	{
		if (enabled)
			return output;
		else
			return setpoint;
	}

	public void DisablePID()
	{
		enabled = false;
	}

	public void EnablePID()
	{
		enabled = true;
	}

	public void Reset()
	{
		acc_error = 0.0;
		error = 0.0;
		prev_error = 0.0;
		input = 0.0;
		output = 0.0;
		setpoint = 0.0;
	}

	public void SetIIREnabled(boolean enabled)
	{
		IIREnabled = enabled;
		if (!enabled)
			runningSum.Clear();
	}

	public void SetIIRDecay(double decay)
	{
		runningSum.setDecayConstant(decay);
	}

}
