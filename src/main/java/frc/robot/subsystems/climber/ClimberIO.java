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

    void setPosition(double position);

    void stop();

    double getPosition();
}