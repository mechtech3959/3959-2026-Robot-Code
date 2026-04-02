package frc.robot.subsystems.vision;

import frc.robot.generated.LimelightHelpers;

public interface VisionIO {
    
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
    default double distanceEstimate(double TY) {
        return -1;
    }
    default void updateTracking(double yawDegrees) {
    }

    
    default void periodic() {
    }

}
