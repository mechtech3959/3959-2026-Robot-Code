package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;

public interface VisionIO {
     public double TX= 0;
    public double TY= 0;
    public double TA = 0;
    public boolean TV = false;
    public String pipeLine = "";
    public Pose2d foundPosition = new Pose2d();
    public double timeStamp = 0;

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
    default void periodic() {
    }

}
