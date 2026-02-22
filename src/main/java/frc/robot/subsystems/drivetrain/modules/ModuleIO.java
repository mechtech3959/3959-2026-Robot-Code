package frc.robot.subsystems.drivetrain.modules;

import org.littletonrobotics.junction.AutoLog;

public interface ModuleIO {

    @AutoLog
    public class ModuleIOInputs {
        // --- Drive Motor ---
        public double drivePositionRad = 0.0;
        public double driveVelocityRadPerSec = 0.0;
        public double driveVelocitySetpointRadPerSec = 0.0;
        public double driveAppliedVolts = 0.0;
        public double driveSupplyCurrentAmps = 0.0;
        public double driveStatorCurrentAmps = 0.0;
        public double driveTemperature = 0.0;

        // --- Steer Motor ---
        public double steerPositionRad = 0.0;
        public double steerPositionSetpointRad = 0.0;
        public double steerVelocityRadPerSec = 0.0;
        public double steerAppliedVolts = 0.0;
        public double steerSupplyCurrentAmps = 0.0;
        public double steerStatorCurrentAmps = 0.0;
        public double steerTemperature = 0.0;

        // --- Sensors & Health ---
        public double steerAbsolutePositionRad = 0.0;

        public boolean driveConnected = true;
        public boolean steerConnected = true;
        public boolean encoderConnected = true;
    }

    default void updateInputs(ModuleIOInputs inputs) {
    }

}