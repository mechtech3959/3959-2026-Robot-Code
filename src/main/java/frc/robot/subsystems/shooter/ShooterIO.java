package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
    @AutoLog
    class ShooterIOInputs {

        public double leftShooterSpeedRPS = 0;
        public double rightShooterSpeedRPS = 0;

        public double leftShooterSpeedRPM = 0;
        public double rightShooterSpeedRPM = 0;
        public double targetSpeedRPS = 0;
        public boolean atTargetSpeed = false;

        public double LeftShooterCurrentAmps = 0;
        public double RightShooterCurrentAmps = 0;
        public double hoodAngle = 50;
    }

    default void setShooterSpeed(double speed) {
    }

    default void setShooterNeutral() {
    }

    default void setHoodAngle(double angle) {
    }

    default double getShooterSpeed() {
        return 0;
    }

    default double getHoodAngle() {
        return 0;
    }

    default boolean isNearTargetSpeed() {
        return false;
    }

    default void stop() {
        setShooterSpeed(0);
    }

    default void updateInputs(ShooterIOInputs inputs) {
    }

    default void periodic() {
    }

}
