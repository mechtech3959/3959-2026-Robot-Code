package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {
    @AutoLog
    class ClimberIOInputs {
        public double position = 0; // Encoder position
        public double appliedVolts = 0; // Attempted volts
        public double currentAmps = 0; // Current amps being drawn
        public double tempCelsius = 0; // Current motor temperature
        public double velocity = 0; // Current motor temperature
        public double target = 0;
    }

    default void updateInputs(ClimberIOInputs inputs) {
    }

    default void setPosition(double position) {
    }

    default void stop() {
    }

    default boolean isAtTarget() {
        return false;
    }

    default double getPosition() {
        return 0;
    }
}