/*
 * pid.cpp
 *
 *  Created on: Feb 21, 2015
 *      Author: Andy
 */
#include "pid.h"

RunningSum::RunningSum(double newDecayConstant)
{
	decayConstant = newDecayConstant;
	runningSum = 0;
}

double RunningSum::UpdateSum(double x)
{
	runningSum *= decayConstant;
	runningSum += x;
	return runningSum * (1 - decayConstant);
}

void RunningSum::Clear()
{
	runningSum = 0;
}

void RunningSum::setDecayConstant(double newDecayConstant)
{
	decayConstant = newDecayConstant;
}

void PID::Reset()
{
	acc_error = 0.0;
	error = 0.0;
	prev_error = 0.0;
	input = 0.0;
	output = 0.0;
	setpoint = 0.0;
}

void PID::EnablePID()
{
	enabled = true;
}

void PID::SetParameters(double p_gain, double i_gain, double d_gain,
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

double PID::IIR_DECAY(double freq)
{
	return 2 * 3.14159 * freq / 50;
}

double PID::Update(double dt)
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
	double PID_output = proportional_gain
			* (error + integral_gain * integral + derivative_gain * delta);

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

void PID::SetSetpoint(double newSetpoint)
{
	setpoint = newSetpoint;
}

void PID::SetInput(double newInput)
{
	input = newInput;
}

double PID::GetProportionalGain()
{
	return proportional_gain;
}

double PID::GetIntegralGain()
{
	return integral_gain;
}

double PID::GetDerivativeGain()
{
	return derivative_gain;
}

double PID::GetFeedForwardGain()
{
	return feedforward_gain;
}

double PID::GetIntegralDecay()
{
	return integral_decay;
}

double PID::GetInput()
{
	return input;
}

double PID::GetSetpoint()
{
	return setpoint;
}

double PID::GetError()
{
	return error;
}

double PID::GetAccumulatedError()
{
	return acc_error;
}

double PID::GetPreviousError()
{
	return prev_error;
}

double PID::GetOutput()
{
	if (enabled)
		return output;
	else
		return setpoint;
}

void PID::DisablePID()
{
	enabled = false;
}

void PID::SetIIREnabled(bool enabled)
{
	IIREnabled = enabled;
	if (!enabled)
		runningSum->Clear();
}

void PID::SetIIRDecay(double decay)
{
	runningSum->setDecayConstant(decay);
}

PID::PID(double p_gain, double i_gain, double d_gain, double ff_gain,
		double i_decay, bool feedforward, double filterFreq)
{
	runningSum = new RunningSum(IIR_DECAY(filterFreq));
	SetParameters(p_gain, i_gain, d_gain, ff_gain, i_decay, feedforward,
			filterFreq);
	IIREnabled = false;
}

PID::PID()
{
	runningSum = new RunningSum(IIR_DECAY(7.0));
	SetParameters(0, 0, 0, 0, 0, false, 0);
	IIREnabled = false;
}

PID::~PID()
{
	delete runningSum;
}

