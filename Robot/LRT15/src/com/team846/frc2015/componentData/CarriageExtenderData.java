package com.team846.frc2015.componentData;

public class CarriageExtenderData extends ComponentData {
    private CarriageControlMode control;
    private Setpoint setpoint;

    private double maxSpeed;
    private double speed;

    private double desiredCarriagePosition;
    private int currentPosition;

    public enum CarriageControlMode {
        SETPOINT,
        VELOCITY,
        POSITION
    }

    public enum Setpoint {
        RETRACT,
        EXTEND
    }

    public CarriageExtenderData() {
        super("CarriageExtenderData");
        maxSpeed = 0.8;
        ResetCommands();
    }

    public static CarriageExtenderData get() {
        return (CarriageExtenderData) ComponentData.GetComponentData("CarriageExtenderData");
    }

    public void setMaxSpeed(double speed) {
        maxSpeed = speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setSpeed(double speed) {
        this.speed = speed * maxSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public CarriageControlMode getControlMode() {
        return control;
    }

    public void setControlMode(CarriageControlMode controlMode) {
        control = controlMode;
    }

    public void setAutomatedSetpoint(Setpoint set) {
        setpoint = set;
    }

    public Setpoint getSetpoint() {
        return setpoint;
    }

    public void setPositionSetpoint(double pos) {
        this.desiredCarriagePosition = pos;
    }

    public double getPositionSetpoint() {
        return desiredCarriagePosition;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int d) {
        currentPosition = d;
    }

    @Override
    protected void ResetCommands() {
        speed = 0.0;
        maxSpeed = 1.0;
        control = CarriageControlMode.VELOCITY;
        setpoint = Setpoint.RETRACT;
    }

}
