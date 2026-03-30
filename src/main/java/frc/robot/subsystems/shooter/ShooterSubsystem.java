package frc.robot.subsystems.shooter;

import java.io.Console;

import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

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
        INTAKE,
        TUNING,
        AUTO,
        UNKNOWN
    }

    private double targetRPS = 0;
    // private double targetAngle = 50; // fixed angle

    private final ShooterIO io;
    private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();
    private ShooterActions ShooterStatus = ShooterActions.IDLE;
    private ShooterStates ShooterState = ShooterStates.UNKNOWN;
    LoggedNetworkNumber tuningRPS;

    public ShooterSubsystem(ShooterIO io) {
        this.io = io;
        this.tuningRPS = new LoggedNetworkNumber("/Tuning/RPS", 0.0);
    }

    private void handleState() {

        switch (ShooterState) {
            case KNOWN_CLOSE -> io.setShooterSpeed(tuningRPS.get());//io.setShooterSpeed(42);
            case KNOWN_FAR -> io.setShooterSpeed(52);
            case REST -> io.setShooterNeutral();
            case UNKNOWN -> io.setShooterSpeed(0);
            case INTAKE -> io.setShooterSpeed(15);
            case TUNING -> io.setShooterSpeed(tuningRPS.get());
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

    public void changeState(ShooterStates newState) {
        ShooterState = newState;
    }

    public void changeState(ShooterStates newState, double targetRPS) {
        this.targetRPS = targetRPS;
        ShooterState = newState;
    }

    public void setEstimatedRPS(double rps) {
        this.targetRPS = rps;

    }

    @Override
    public void periodic() {
        io.periodic();
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
        Logger.recordOutput("States/shooter-status", ShooterStatus);
        Logger.recordOutput("States/shooter-state", ShooterState.toString());
        handleShooterStatus();
        handleState();
    }

}
