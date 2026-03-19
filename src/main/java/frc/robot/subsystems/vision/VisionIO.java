package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.generated.LimelightHelpers;

public interface VisionIO {
    @AutoLog
    public class VisionIOInputs {
        public double TX = 0;
        public double TY = 0;
        public double TA = 0;
        public boolean TV = false;
        public String pipeLine = "";
        public Pose2d foundPosition = new Pose2d();
        public double timeStamp = 0;
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
    default double distanceEstimate(double TY) {
        return -1;
    }
    default void updateTracking(double yawDegrees) {
    }

    default void updateInputs(VisionIOInputs inputs) {
    }

    default void periodic() {
    }

}
