package com.team846.frc2015.componentData;

public class ElevatorData extends ComponentData {

    private double speed;
    private double position;

    private ElevatorControlMode controlMode;

    private ElevatorSetpoint setpoint;
    private ElevatorSetpoint currentSetpoint;

    public double errorThreshold = 10;
    private double currentPosition = 0;
    private boolean bangbang = false;

    public enum ElevatorControlMode {
        POSITION,
        VELOCITY,
        SETPOINT
    }

    public enum ElevatorSetpoint {
        // First object sequence is collect, grab, home
        // Additional object sequence is COLLECT_ADDITIONAL, GRAB_TOTE, HOME_TOTE
        COLLECT_TOTE,
        COLLECT_UPRIGHT_CONTAINER,
        COLLECT_SIDEWAYS_CONTAINER,
        GRAB_TOTE,
        GRAB_SIDEWAYS_CONTAINER,
        HOME_TOTE,
        HOME_UPRIGHT_CONTAINER,
        HOME_SIDEWAYS_CONTAINER,
        COLLECT_ADDITIONAL,
        HUMAN_LOAD_PREPARE,
        HUMAN_LOAD_GRAB,
        TOTE_1,
        TOTE_2,
        TOTE_3,
        TOTE_4,
        STEP,
        SWEEP_CONTAINER,
        NONE
    }

    public ElevatorData() {
        super("ElevatorData");
        position = 0;
        ResetCommands();
    }

    public static ElevatorData get() {
        return (ElevatorData) ComponentData.GetComponentData("ElevatorData");
    }

    public void setDesiredSpeed(double d) {
        speed = d;
    }

    public double getSpeed() {
        return speed;
    }

    public ElevatorSetpoint getDesiredSetpoint() {
        return setpoint;
    }

    public void setSetpoint(ElevatorSetpoint s) {
//		if (s != setpoint)
//		{
//			currentSetpoint = ElevatorSetpoint.NONE;
//		}
        setpoint = s;
    }

    public ElevatorControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ElevatorControlMode control) {
//		if (controlMode != control)
//		{
//			currentSetpoint = ElevatorSetpoint.NONE;
//		}
        this.controlMode = control;
    }

    public boolean isAtSetpoint(ElevatorSetpoint s) {

        return currentSetpoint == s;
    }

    public void setCurrentPosition(ElevatorSetpoint s) {
        currentSetpoint = s;
    }

    public ElevatorSetpoint getCurrentSetpoint() {
        return currentSetpoint;
    }

    public double velocity = 0.0;

    public boolean isAtPosition(double isAtPosition) {
        System.out.println("VELOCITY: " + velocity + " DISTANCE " + (isAtPosition - currentPosition) + " ERROR THRESHOLD " + errorThreshold);
        return Math.abs(velocity) < 10.0 &&
                Math.abs(isAtPosition - currentPosition) < 3 * errorThreshold;
    }

    public void setCurrentPosition(double position) {
        currentPosition = position;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public double getDesiredPosition() {
        return position;
    }

    public void setDesiredPosition(double d) {
        this.position = d;
    }

    public void setFast(boolean fast) {
        bangbang = fast;
    }

    public boolean getFast() {
        return bangbang;
    }

    @Override
    protected void ResetCommands() {
        controlMode = ElevatorControlMode.VELOCITY;
        speed = 0.0f;
        setpoint = ElevatorSetpoint.NONE;
        bangbang = false;
    }

}
