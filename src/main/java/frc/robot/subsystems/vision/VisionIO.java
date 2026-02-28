package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.AutoLog;


import edu.wpi.first.math.geometry.Pose2d;

public interface VisionIO {
    @AutoLog
    public class VisionIOInputs {

        public boolean TV = false;
        public String pipeLine = "";
    }
 default LimelightHelpers.PoseEstimate getPoseEstimate() {
    return null;
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

    default void updateTracking(double yawDegrees) {
    }
    default void updateInputs(VisionIOInputs inputs) {
    }

    default void periodic() {
    }

}
