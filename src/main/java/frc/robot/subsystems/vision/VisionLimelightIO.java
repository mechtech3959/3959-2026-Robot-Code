package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;
import limelight.Limelight;
import limelight.networktables.LimelightResults;

public class VisionLimelightIO implements VisionIO {

    public boolean TV;
    public String pipeLine;
    public Pose2d foundPosition;
    // private LimelightHelpers.PoseEstimate limelightMeasurement;
    private Limelight limeLight;

    public VisionLimelightIO(String pipeLine) {
        this.pipeLine = pipeLine;

        this.limeLight = new Limelight(pipeLine);
    }

    @Override
    public LimelightHelpers.PoseEstimate getPoseEstimate() {
        return limelightMeasurement;
    }

    @Override
    public void setVisionNeutral() {}

@Override
public void updateTracking(double yawDegrees) {
    LimelightHelpers.SetRobotOrientation(pipeLine, yawDegrees, 0, 0, 0, 0, 0);
    limelightMeasurement = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(pipeLine);
    if (limelightMeasurement != null) {
        foundPosition = limelightMeasurement.pose;
        timeStamp = limelightMeasurement.timestampSeconds;
    }
}

    

    @Override
    public void trackingStart() {

    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        inputs.TX = LimelightHelpers.getTX(pipeLine);
        inputs.TY = LimelightHelpers.getTY(pipeLine);
        inputs.TA = LimelightHelpers.getTA(pipeLine);
        inputs.TV = LimelightHelpers.getTV(pipeLine);
        inputs.pipeLine = pipeLine;
        if (limelightMeasurement != null) {
            foundPosition = limelightMeasurement.pose;
            timeStamp = limelightMeasurement.timestampSeconds;
        }
    }

}