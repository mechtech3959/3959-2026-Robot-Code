package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {
    private VisionIO io;

    public VisionSubsystem(VisionIO io) {
        this.io = io;
    }

    public void setRobot(double yaw) {
        LimelightHelpers.SetRobotOrientation(io.pipeLine, yaw, 0, 0, 0, 0, 0);
        // figure out angle of LL Mounts

    }

    public void setPipeline(String pipeLine) {
        io.setVisionNeutral();
        io.trackingStart();
    }

    public double getTX() {
        return io.TX;
    }

    public double getTY() {
        return io.TY;
    }

    public double getTA() {
        return io.TA;
    }

    public boolean getTV() {
        return io.TV;
    }

    @Override
    public void periodic() {
        io.updateTracking();
    }
}
