package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputsAutoLogged;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;

public class VisionSubsystem extends SubsystemBase {
    private VisionIO io;
    private final VisionIOInputsAutoLogged inputs = new VisionIOInputsAutoLogged();

    public VisionSubsystem(VisionIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(getName(), inputs);
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

    @Override
    public void periodic() {
        io.updateTracking();
    }
}
