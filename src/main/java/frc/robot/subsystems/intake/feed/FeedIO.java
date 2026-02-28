package frc.robot.subsystems.intake.feed;

import org.littletonrobotics.junction.AutoLog;

public interface FeedIO {
    @AutoLog
    public class FeedIOInputs {
        public double motorStaturCurrent = 0;
        public double motorSupplyCurrent = 0;
        public double motorTemperature = 0;
        public double motorVelocity = 0;

    }

    default void runFeedMotor() {
    }

    default void stopFeedMotor() {
    }

    default void setSpeed(double speed) {
    }

    default void updateInputs(FeedIOInputs inputs) {
    }
}
