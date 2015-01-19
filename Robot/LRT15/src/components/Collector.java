package components;

import componentData.CollectorData;
import actuators.LRTSpeedController;

public class Collector extends Component {
	private CollectorData collectorData;
	private static LRTSpeedController motor;
	public Collector(int driverStationDigitalIn) {
		super("Collector", driverStationDigitalIn);
		collectorData = CollectorData.get();
	}

	@Override
	protected void UpdateEnabled() {
		float speed = 0.0F;
		if(collectorData.isRunning()){
			if(collectorData.getCurrentState() == CollectorData.possibleCollectorState.INPUT){
				speed = collectorData.getSpeed();
			}
			else if(collectorData.getCurrentState() == CollectorData.possibleCollectorState.INPUT){
				speed = -collectorData.getSpeed();
			}
			else{
				speed = 0;
			}
 		}
		//set motor speed to speed
	}

	@Override
	protected void UpdateDisabled() {
		//deactivate motors here
	}

	@Override
	protected void OnEnabled() {

	}

	@Override
	protected void OnDisabled() {
		// TODO Auto-generated method stub

	}

}
