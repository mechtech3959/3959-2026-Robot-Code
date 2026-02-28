package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.AutoLog;


import edu.wpi.first.math.geometry.Pose2d;

public interface VisionIO {
    @AutoLog
    public class VisionIOInputs {

        public boolean TV = false;
        public String pipeLine = "";
    }

    default void setVisionNeutral() {
    }

    default void trackingStart() {
    }

    default double estimatedDistanceToTarget() {
        return -1;
    }

    default boolean hasTarget() {
        return false;
    }

    default boolean isEstimateValid() {
        return false;
    }

    default boolean isPoseEstimateValid() {
        return false;
    }

    default void updateTracking() {
    }
    default void updateInputs(VisionIOInputs inputs) {
    }

    default void periodic() {
    }

}
