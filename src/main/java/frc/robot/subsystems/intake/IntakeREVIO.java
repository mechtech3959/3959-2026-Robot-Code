package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

@SuppressWarnings("FieldCanBeLocal")
public class IntakeREVIO implements IntakeIO {
    private final SparkMax intakeMotor = new SparkMax(0, SparkLowLevel.MotorType.kBrushless); // replace with proper canID

    public IntakeREVIO() {
        IntakeConfiguration intakeMotorConfig = new IntakeConfiguration();
        intakeMotor.configure(intakeMotorConfig.getConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void setControl(double position) {
        intakeMotor.getClosedLoopController().setSetpoint(position, SparkBase.ControlType.kMAXMotionPositionControl);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }

    public double getPosition() {
        return intakeMotor.getEncoder().getPosition();
    }
}
