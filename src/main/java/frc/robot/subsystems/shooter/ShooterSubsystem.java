package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    // TODO time longest possible time to shoot all balls / average time to shoot
    // all
    // balls, use this to determine auto shooting times
    public enum ShooterActions {
        STOPPED,
        IDLE,
        SPINNING_UP,
        AT_SPEED,
        SHOOTING
    }

    public enum ShooterStates {
        KNOWN_CLOSE,
        KNOWN_FAR,
        REST,
        UNKNOWN
    }

    private double targetRPM;
    private double targetAngle = 50; // fixed angle

    private final ShooterIO io;
    private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();
    private ShooterActions ShooterStatus = ShooterActions.IDLE;
    private ShooterStates ShooterState = ShooterStates.UNKNOWN;

    public ShooterSubsystem(ShooterIO io) {
        this.io = io;

    }

    private void handleState() {

        switch (ShooterState) {
            case KNOWN_CLOSE -> io.setShooterSpeed(targetRPM);
            case KNOWN_FAR -> {
                // no-op for known far mode (configure if needed)
            }
            case REST -> io.setShooterNeutral();
            case UNKNOWN -> io.setShooterSpeed(0);
        }
    }

    public void handleShooterStatus() {
        if (ShooterState == ShooterStates.REST) {
            ShooterStatus = ShooterActions.IDLE;
        } else if (io.getShooterSpeed() == 0) {
            ShooterStatus = ShooterActions.STOPPED;
        } else if (!io.isNearTargetSpeed()) {
            ShooterStatus = ShooterActions.SPINNING_UP;
        } else {
            ShooterStatus = ShooterActions.AT_SPEED;
        }
    }

    public void setShooterAction(ShooterActions newState) {
        ShooterStatus = newState;
    }

    public void changeState(ShooterStates newState, double targetRPM) {
        this.targetRPM = targetRPM;
        ShooterState = newState;
    }

    @Override
    public void periodic() {

        io.periodic();
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
        Logger.recordOutput(getName()+ "ShooterStatus", ShooterStatus);
        handleShooterStatus();
        handleState();
    }

}
