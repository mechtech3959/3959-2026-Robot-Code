package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import frc.robot.RobotMap;

public class IntakeREVIO implements IntakeIO {
    private final SparkMax intakeMotor = new SparkMax(RobotMap.INTAKE.AXIS_MOTOR, SparkLowLevel.MotorType.kBrushless); // replace
    private double target = 0; // with
    // proper
    // canID

    public IntakeREVIO() {
        IntakeConfiguration intakeMotorConfig = new IntakeConfiguration();
        intakeMotor.configure(intakeMotorConfig.getConfig(), ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

    }

    @Override
    public void setControl(double position) {
        intakeMotor.getClosedLoopController().setSetpoint(position, SparkBase.ControlType.kMAXMotionPositionControl);
        target = position;
    }

    @Override
    public void stop() {
        intakeMotor.stopMotor();
    }

    @Override
    public double getPosition() {
        return intakeMotor.getEncoder().getPosition();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.axisPosition = intakeMotor.getEncoder().getPosition();
        inputs.axisVelocity = intakeMotor.getEncoder().getVelocity();
        inputs.axisTarget = target;
        inputs.axisCurrent = intakeMotor.getOutputCurrent();
        inputs.axisTemperature = intakeMotor.getMotorTemperature();
        inputs.isAtTarget = Math.abs(target - getPosition()) < 0.1; // Adjust the tolerance as needed
    }
}
