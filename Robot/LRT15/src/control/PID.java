package control;

public class PID {
	
	double m_proportional_gain;
	double m_integral_gain;
	double m_derivative_gain;
	double m_feedforward_gain;
	double m_integral_decay;
	double m_input;
	double m_output;
	double m_setpoint;
	double m_error;
	double m_prev_error;
	double m_acc_error;
	boolean m_is_feed_forward;
	boolean m_enabled;
	
	boolean m_IIREnabled;
	RunningSum m_runningSum;

	PID(double p_gain, double i_gain, double d_gain, double ff_gain,
			double i_decay, boolean feedforward, double filterFreq)
	{
		SetParameters(p_gain, i_gain, d_gain, ff_gain, i_decay, feedforward, filterFreq);
		m_runningSum = new RunningSum(IIR_DECAY(filterFreq));
		m_IIREnabled = false;
	}

	public PID() 
	{
		m_runningSum = new RunningSum(IIR_DECAY(7.0));
		SetParameters(0, 0, 0,0,0,false,0);
		m_IIREnabled = false;
	}
	
	private double IIR_DECAY (double freq)
	{
		return 2 * 3.14159 * freq / 50;
	}

	void SetParameters(double p_gain, double i_gain, double d_gain,
			double ff_gain, double i_decay, boolean feedforward, double filterFreq)
	{
		Reset();
		m_proportional_gain = p_gain;
		m_integral_gain = i_gain;
		m_derivative_gain = d_gain;
		m_feedforward_gain = ff_gain;
		m_integral_decay = i_decay != 1.0 ? i_decay : 0.9999999999999;
		m_is_feed_forward = feedforward;
		m_runningSum.setDecayConstant(IIR_DECAY(filterFreq));
		EnablePID();
	}

	public double Update(double dt)
	{
		m_error = m_setpoint - m_input;
		//	AsyncPrinter::Printf("Error: %.02f\r\n", m_error);

		if (m_IIREnabled)
		{
			m_error = m_runningSum.UpdateSum(m_error);
		}
		else
		{
			m_runningSum.Clear();
		}

		// calculate discrete derivative
		double delta = (m_error - m_prev_error) / dt;

		// approximate with riemann sum and decay
		m_acc_error *= m_integral_decay;
		m_acc_error += m_error * dt;
		if (m_acc_error != m_acc_error) // catch NaN
		{
			m_acc_error = 0;
		}
		double integral = m_acc_error / (1 - m_integral_decay);

		// magic PID line
		double PID_output = m_proportional_gain * (m_error + m_integral_gain
				* integral + m_derivative_gain * delta);

		if (m_is_feed_forward)
		{
			// feed-forward PID
			m_output = m_setpoint * m_feedforward_gain + PID_output;
		}
		else
		{
			// standard PID
			m_output = PID_output;
		}

		m_prev_error = m_error;

		return m_output;
	}

	public void SetSetpoint(double setpoint)
	{
		m_setpoint = setpoint;
	}

	public void SetInput(double input)
	{
		m_input = input;
	}

	public double GetProportionalGain()
	{
		return m_proportional_gain;
	}

	public double GetIntegralGain()
	{
		return m_integral_gain;
	}

	public double GetDerivativeGain()
	{
		return m_derivative_gain;
	}

	public double GetFeedForwardGain()
	{
		return m_feedforward_gain;
	}

	public double GetIntegralDecay()
	{
		return m_integral_decay;
	}

	public double GetInput()
	{
		return m_input;
	}

	public double GetSetpoint()
	{
		return m_setpoint;
	}

	public double GetError()
	{
		return m_error;
	}

	public double GetAccumulatedError()
	{
		return m_acc_error;
	}

	public double GetPreviousError()
	{
		return m_prev_error;
	}

	public double GetOutput()
	{
		if (m_enabled)
			return m_output;
		else
			return m_setpoint;
	}

	public void DisablePID()
	{
		m_enabled = false;
	}

	public void EnablePID()
	{
		m_enabled = true;
	}

	public void Reset()
	{
		m_acc_error = 0.0;
		m_error = 0.0;
		m_prev_error = 0.0;
		m_input = 0.0;
		m_output = 0.0;
		m_setpoint = 0.0;
	}

	public void SetIIREnabled(boolean enabled)
	{
		m_IIREnabled = enabled;
		if (!enabled)
			m_runningSum.Clear();
	}

	public void SetIIRDecay(double decay)
	{
		m_runningSum.setDecayConstant(decay);
	}

}
