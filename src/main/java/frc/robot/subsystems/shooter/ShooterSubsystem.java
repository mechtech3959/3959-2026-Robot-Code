// Why two different enums?(ShooterActions and ShooterStates)
// The shooter has more variation in states. But the the motion of each state
// doesnt vary much, EX: the shooter motors are either forward, backward, or
// stopped. but we have several different versions of foward motion.
// A status machine(ShooterActions) is used to debug what the should
// theoretically vs what the shooter is actually doing.
// Addtionally, the status machine is a vital simplification for the shooter to
// communicate with other subsystems, like the indexer and intake. The indexer
// and intake only need to know if the shooter is at speed or not, they dont
// care about the specific state of the shooter
// if this is applied system wide this could be used to prevent jams in the
// indexer and conveyor.(not implemented yet)

package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    // Shooter Actions specifies the current physical output of the shooter.
    public enum ShooterActions {
        STOPPED,
        IDLE,
        SPINNING_UP,
        AT_SPEED,
        SHOOTING
    }

    // Shooter States is the current specific action the shooter is performing
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
            case KNOWN_CLOSE -> io.setShooterSpeed(42);
            case KNOWN_FAR -> io.setShooterSpeed(53);
            case REST -> io.setShooterNeutral();
            case UNKNOWN -> io.setShooterSpeed(0);
            case INTAKE -> io.setShooterSpeed(18);
            case AUTO -> io.setShooterSpeed(targetRPS);
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

    public ShooterStates getShooterState() {
        return ShooterState;
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
