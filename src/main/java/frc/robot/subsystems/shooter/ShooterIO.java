package frc.robot.subsystems.shooter;

public interface ShooterIO {

    class ShooterIOInputs {

        public double shooterSpeed = 0;
        public double hoodAngle = 0;
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
