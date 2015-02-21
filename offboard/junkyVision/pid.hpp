/*
 * pid.hpp
 *
 *  Created on: Feb 20, 2015
 *      Author: Andy
 */

#ifndef PID_HPP_
#define PID_HPP_

class RunningSum {
private:
	double decayConstant;
	double runningSum;

public:
	RunningSum(double newDecayConstant)
	{
		decayConstant = newDecayConstant;
		runningSum = 0;
	}

	double UpdateSum(double x)
	{
		runningSum *= decayConstant;
		runningSum += x;
		return runningSum * (1 - decayConstant);
	}

	void Clear()
	{
		runningSum = 0;
	}

	void setDecayConstant(double newDecayConstant)
	{
		decayConstant = newDecayConstant;
	}
};

class PID {
private:

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
	bool is_feed_forward;
	bool enabled;

	bool IIREnabled;
	RunningSum * runningSum;

public:
	void Reset()
	{
		acc_error = 0.0;
		error = 0.0;
		prev_error = 0.0;
		input = 0.0;
		output = 0.0;
		setpoint = 0.0;
	}

	void EnablePID()
	{
		enabled = true;
	}

	void SetParameters(double p_gain, double i_gain, double d_gain,
			double ff_gain, double i_decay, bool feedforward, double filterFreq)
	{
		Reset();
		proportional_gain = p_gain;
		integral_gain = i_gain;
		derivative_gain = d_gain;
		feedforward_gain = ff_gain;
		integral_decay = i_decay != 1.0 ? i_decay : 0.9999999999999;
		is_feed_forward = feedforward;
		runningSum->setDecayConstant(IIR_DECAY(filterFreq));
		EnablePID();
	}

	double IIR_DECAY (double freq)
	{
		return 2 * 3.14159 * freq / 50;
	}

	double Update(double dt)
	{
		error = setpoint - input;
		//	AsyncPrinter::Printf("Error: %.02f\r\n", error);

		if (IIREnabled)
		{
			error = runningSum->UpdateSum(error);
		}
		else
		{
			runningSum->Clear();
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

	void SetSetpoint(double newSetpoint)
	{
		setpoint = newSetpoint;
	}

	void SetInput(double newInput)
	{
		input = newInput;
	}

	double GetProportionalGain()
	{
		return proportional_gain;
	}

	double GetIntegralGain()
	{
		return integral_gain;
	}

	double GetDerivativeGain()
	{
		return derivative_gain;
	}

	double GetFeedForwardGain()
	{
		return feedforward_gain;
	}

	double GetIntegralDecay()
	{
		return integral_decay;
	}

	double GetInput()
	{
		return input;
	}

	double GetSetpoint()
	{
		return setpoint;
	}

	double GetError()
	{
		return error;
	}

	double GetAccumulatedError()
	{
		return acc_error;
	}

	double GetPreviousError()
	{
		return prev_error;
	}

	double GetOutput()
	{
		if (enabled)
			return output;
		else
			return setpoint;
	}

	void DisablePID()
	{
		enabled = false;
	}

	void SetIIREnabled(bool enabled)
	{
		IIREnabled = enabled;
		if (!enabled)
			runningSum->Clear();
	}

	void SetIIRDecay(double decay)
	{
		runningSum->setDecayConstant(decay);
	}

	PID(double p_gain, double i_gain, double d_gain, double ff_gain,
			double i_decay, bool feedforward, double filterFreq)
	{
		runningSum = new RunningSum(IIR_DECAY(filterFreq));
		SetParameters(p_gain, i_gain, d_gain, ff_gain, i_decay, feedforward, filterFreq);
		IIREnabled = false;
	}

	PID()
	{
		runningSum = new RunningSum(IIR_DECAY(7.0));
		SetParameters(0, 0, 0,0,0,false,0);
		IIREnabled = false;
	}
};

#endif /* PID_HPP_ */
