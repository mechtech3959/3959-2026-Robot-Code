package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.generated.LimelightHelpers;

public class VisionLimelightIO implements VisionIO {

    public double TX;
    public double TY;
    public double TA;
    public boolean TV;
    public String pipeLine;
    public Pose2d foundPosition;
    public double timeStamp;
    private LimelightHelpers.PoseEstimate limelightMeasurement;
    private String limelightModel;

    public VisionLimelightIO(String pipeLine,String limelightModel) {
        this.pipeLine = pipeLine;
        this.limelightModel = limelightModel;
    }

    @Override
    public LimelightHelpers.PoseEstimate getPoseEstimate() {
        return limelightMeasurement;
    }

    @Override
    public void setVisionNeutral() {
        TX = 0;
        TY = 0;
        TA = 0;
        TV = false;
        foundPosition = new Pose2d();
        timeStamp = 0;
    }

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
        TX = LimelightHelpers.getTX(pipeLine);
        TY = LimelightHelpers.getTY(pipeLine);
        TA = LimelightHelpers.getTA(pipeLine);
        TV = LimelightHelpers.getTV(pipeLine);

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