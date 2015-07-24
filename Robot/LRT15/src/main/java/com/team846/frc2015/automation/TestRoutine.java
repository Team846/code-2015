package com.team846.frc2015.automation;

import com.team846.frc2015.utils.AsyncPrinter;

public class TestRoutine extends Automation {

	@Override
	public void AllocateResources() {

	}

	@Override
	protected boolean Start() {
		AsyncPrinter.info("Started routine");
		return true;
	}

	@Override
	protected boolean Abort() {
		AsyncPrinter.info("aborted routine");
		return true;
	}

	@Override
	protected boolean Run() {
		AsyncPrinter.info("Running Routine");
		return false;
	}

}
