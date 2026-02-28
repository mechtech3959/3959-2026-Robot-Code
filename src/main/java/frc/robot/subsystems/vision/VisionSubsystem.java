package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

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
       // limelight.SetRobotOrientation(inputs.pipeLine, yaw, 0, 0, 0, 0, 0);
        // figure out angle of LL Mounts

    }

    public void setPipeline(String pipeLine) {
        io.setVisionNeutral();
        io.trackingStart();
    }

    public double getTX() {
        return 0;//inputs.TX;
    }

    public double getTY() {
        return 0; //inputs.TY;
    }

    public double getTA() {
        return 0;//inputs.TA;
    }

    public boolean getTV() {
        return false;//inputs.TV;
    }

}
