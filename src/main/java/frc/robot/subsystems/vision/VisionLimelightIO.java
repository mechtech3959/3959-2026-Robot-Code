package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose2d;

public class VisionLimelightIO implements VisionIO {

    public double TX;
    public double TY;
    public double TA;
    public boolean TV;
    public String pipeLine;
    public Pose2d foundPosition;
    public double timeStamp;
    private LimelightHelpers.PoseEstimate limelightMeasurement;

    public VisionLimelightIO(String pipeLine) {
        this.pipeLine = pipeLine;
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
    public void updateTracking() {
        TX = LimelightHelpers.getTX(pipeLine);
        TY = LimelightHelpers.getTY(pipeLine);
        TA = LimelightHelpers.getTA(pipeLine);
        TV = LimelightHelpers.getTV(pipeLine);
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

}
