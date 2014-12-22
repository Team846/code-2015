package sensors;

import edu.wpi.first.wpilibj.Encoder;

public class LRTEncoder extends Encoder {

	double trim = 1.0;

	public LRTEncoder(int aChannel, int bChannel, boolean reverseDirection,
			EncodingType encodingType) 
	{
		super(aChannel, bChannel, reverseDirection, encodingType);
	}

	void Start() 
	{
		super.stop();
		super.start();
		super.setMinRate(10);
	}

	double GetRate() 
	{
		double rate = super.getRate();

		if (super.getStopped() || rate != rate)
			return 0.0;

		return rate;
	}

	int Get() 
	{
		return (int) (super.get() * trim);
	}

}
