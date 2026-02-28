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
    public void setVisionNeutral() {

    }

    @Override
    public void trackingStart() {

    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
      ////  limeLight.getLatestResults().ifPresent((LimelightResults result) -> {
         //   inputs.TV = result.;
     //       inputs.pipeLine = pipeLine;
            
         
     //   });
    }

}