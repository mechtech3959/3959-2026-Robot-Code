package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;

public class VisionSubsystem extends SubsystemBase {

    private final VisionIO[] cameras;
    private final DrivetrainSubsystem drivetrain;

    //VisionIO is set to an Array to allow easy addition of more cameras in the future, but for now we will just use 2 Limelight cameras
    public VisionSubsystem(DrivetrainSubsystem drivetrain, VisionIO... cameras) {
        this.drivetrain = drivetrain;
        this.cameras = cameras;
     
    }

    @Override
    public void periodic() {
        
        double yaw = drivetrain.getHeading().getDegrees();

        // Set orientation first
        cameras[0].updateTracking(yaw);
        // Update
        cameras[0].periodic();
        var measurement_0 = cameras[0].getPoseEstimate();
        if (measurement_0 != null) {
            Logger.recordOutput("Vision/Camera" + 0 + "/Pose", measurement_0.pose);
            Logger.recordOutput("Vision/Camera" + 0 + "/TagCount", measurement_0.tagCount);
            Logger.recordOutput("Vision/Camera" + 0 + "/AvgTagDist", measurement_0.avgTagDist);
            Logger.recordOutput("Vision/Camera" + 0 + "/Timestamp", measurement_0.timestampSeconds);
        }

        // Feed to drivetrain
        updatePoseEstimate(cameras[0]);
        // Set orientation first
        cameras[1].updateTracking(yaw);
        // Update
        cameras[1].periodic();

        var measurement_1 = cameras[1].getPoseEstimate();
        if (measurement_1 != null) {
            Logger.recordOutput("Vision/Camera" + 1 + "/Pose", measurement_1.pose);
            Logger.recordOutput("Vision/Camera" + 1 + "/TagCount", measurement_1.tagCount);
            Logger.recordOutput("Vision/Camera" + 1 + "/AvgTagDist", measurement_1.avgTagDist);
            Logger.recordOutput("Vision/Camera" + 1 + "/Timestamp", measurement_1.timestampSeconds);
        }

        // Feed to drivetrain
        updatePoseEstimate(cameras[1]);

    }

    private void updatePoseEstimate(VisionIO camera) {
        var measurement = camera.getPoseEstimate();
        if (measurement == null)
            return;
        if (measurement.tagCount == 0)
            return;
        if (measurement.rawFiducials.length > 0) {
            for (var fiducial : measurement.rawFiducials) {
                if (fiducial.ambiguity > 0.7)
                    return; // 0-1 scale, lower is better
            }
        }
        if (measurement.avgTagDist > 4.0)
            return;

        if (Math.abs(drivetrain.getAngularVelocity()) > 2) {
            // Don't trust measurements that are received while the robot is rotating
            // quickly, since they are likely to be inaccurate due to latency
            return;
        }
        // Trust more tags or closer tags more
        double stdDev = measurement.tagCount > 1 ? 0.3 : 1.0;
        stdDev *= (measurement.avgTagDist / 2.0); // scale by distance
        stdDev = Math.max(stdDev, 0.2); // don't go below 0.2 std dev
        Logger.recordOutput("PoseEst", measurement.pose);
        Logger.recordOutput("PoseEstTIME", measurement.timestampSeconds);

        drivetrain.poseEst(
                measurement.pose,
                measurement.timestampSeconds,
                VecBuilder.fill(stdDev, stdDev, 9999));
    }

    public double getDistanceToTarget() {
        return cameras[0].estimatedDistanceToTarget();
    }

}
