package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.RPM;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.ShooterIO.ShooterIOInputs;

public class ShooterSubsystem extends SubsystemBase {
    // TODO time longest possible time to shoot all balls / average time to shoot
    // all
    // balls, use this to determine auto shooting times
    public enum ShooterState {
        STOPPED,
        IDLE,
        SPINNING_UP,
        AT_SPEED,
        SHOOTING
    }

    public enum ShooterMode {
        KNOWN_CLOSE,
        KNOWN_FAR,
        REST,
        UNKNOWN
    }

    private double targetRPM;
    private double targetAngle = 0;

    private final ShooterIO io;
    private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();
    private ShooterState shooterState = ShooterState.IDLE;
    private ShooterMode shooterMode = ShooterMode.UNKNOWN;

    public ShooterSubsystem(ShooterIO io) {
        this.io = io;

    }

    private void handleShooterState() {

        switch (shooterMode) {
            case KNOWN_CLOSE -> io.setShooterSpeed(targetRPM);
            case KNOWN_FAR -> {
                // no-op for known far mode (configure if needed)
            }
            case REST -> io.setShooterNeutral();
            case UNKNOWN -> io.setShooterSpeed(0);
        }
    }

    public void shooterStatus() {
        if (shooterMode == ShooterMode.REST) {
            shooterState = ShooterState.IDLE;
        } else if (io.getShooterSpeed() == 0) {
            shooterState = ShooterState.STOPPED;
        } else if (!io.isNearTargetSpeed()) {
            shooterState = ShooterState.SPINNING_UP;
        } else {
            shooterState = ShooterState.AT_SPEED;
        }
    }

    public void ChangeShooterState(ShooterState newState) {
        shooterState = newState;
    }

    public void ChangeShooterState(ShooterMode newState, double targetRPM) {
        this.targetRPM = targetRPM;
        shooterMode = newState;
    }

    @Override
    public void periodic() {

        io.periodic();
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
        shooterStatus();
        handleShooterState();
    }

}
