/*
 * pid.hpp
 *
 *  Created on: Feb 20, 2015
 *      Author: Andy
 */

#ifndef PID_H_
#define PID_H_

class RunningSum
{
private:
	double decayConstant;
	double runningSum;

public:
	RunningSum(double newDecayConstant);

	double UpdateSum(double x);
	void Clear();
	void setDecayConstant(double newDecayConstant);
};

class PID
{
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
	void Reset();
	void EnablePID();
	void SetParameters(double p_gain, double i_gain, double d_gain,
			double ff_gain, double i_decay, bool feedforward, double filterFreq);
	double IIR_DECAY(double freq);
	double Update(double dt);
	void SetSetpoint(double newSetpoint);
	void SetInput(double newInput);
	double GetProportionalGain();
	double GetIntegralGain();
	double GetDerivativeGain();
	double GetFeedForwardGain();
	double GetIntegralDecay();
	double GetInput();
	double GetSetpoint();
	double GetError();
	double GetAccumulatedError();
	double GetPreviousError();
	double GetOutput();
	void DisablePID();
	void SetIIREnabled(bool enabled);
	void SetIIRDecay(double decay);

	PID(double p_gain, double i_gain, double d_gain, double ff_gain,
			double i_decay, bool feedforward, double filterFreq);
	PID();

	~PID();
};

#endif /* PID_H_ */
