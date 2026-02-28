package frc.robot.subsystems.vision;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;
import frc.robot.subsystems.drivetrain.DrivetrainSubsystem;

public class VisionSubsystem extends SubsystemBase {
    
    private final VisionIO[] cameras;
    private final VisionIOInputsAutoLogged[] inputs;
    private final DrivetrainSubsystem drivetrain;

    public VisionSubsystem(DrivetrainSubsystem drivetrain, VisionIO... cameras) {
        this.drivetrain = drivetrain;
        this.cameras = cameras;
        this.inputs = new VisionIOInputsAutoLogged[cameras.length];
        for (int i = 0; i < cameras.length; i++) {
            inputs[i] = new VisionIOInputsAutoLogged();
        }
    }

    @Override
    public void periodic() {
        double yaw = drivetrain.getHeading().getDegrees();

        for (int i = 0; i < cameras.length; i++) {
            // Set orientation first
            cameras[i].updateTracking(drivetrain.getHeading().getDegrees());
            // Update
            cameras[i].periodic();
            cameras[i].updateInputs(inputs[i]);
            Logger.processInputs("Vision/Camera" + i, inputs[i]);
            // Feed to drivetrain
            updatePoseEstimate(cameras[i]);
        }
    }

    private void updatePoseEstimate(VisionIO camera) {
        var measurement = camera.getPoseEstimate();
        if (measurement == null)
            return;
        if (measurement.tagCount == 0)
            return;
        if (measurement.avgTagDist > 4.0)
            return;

        // Trust more tags or closer tags more
        double stdDev = measurement.tagCount > 1 ? 0.3 : 1.0;
        stdDev *= (measurement.avgTagDist / 2.0); // scale by distance

        drivetrain.poseEst(
                measurement.pose,
                measurement.timestampSeconds,
                VecBuilder.fill(stdDev, stdDev, 9999));
    }

}

    public void setRobot(double yaw) {
        LimelightHelpers.SetRobotOrientation(inputs.pipeLine, yaw, 0, 0, 0, 0, 0);
        // figure out angle of LL Mounts

    }

    public void setPipeline(String pipeLine) {
        io.setVisionNeutral();
        io.trackingStart();
    }

    public double getTX() {
        return inputs.TX;
    }

    public double getTY() {
        return inputs.TY;
    }

    public double getTA() {
        return inputs.TA;
    }

    public boolean getTV() {
        return inputs.TV;
    }

}
