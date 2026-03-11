package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.encoder.SplineEncoder;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import frc.robot.RobotMap;

public class IntakeREVIO implements IntakeIO {
    private final SparkMax intakeMotor = new SparkMax(RobotMap.INTAKE.AXIS_MOTOR, SparkLowLevel.MotorType.kBrushless);

    private final SplineEncoder intakeEncoder = new SplineEncoder(RobotMap.INTAKE.ENCODER);
    private double target = 0;
    private boolean forwardLimitSwitchTriggered = false; // Track the state of the limit switch
    private boolean reverseLimitSwitchTriggered = false; // Track the state of the limit switch
    // proper
    // canID

    public IntakeREVIO() {
        IntakeConfiguration config = new IntakeConfiguration();
        // IntakeConfiguration intakeMotorConfig = new IntakeConfiguration();
        config.getConfig().closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder, intakeEncoder);
        intakeMotor.configure(config.getSparkMotorConfig(), ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

    }

    @Override
    public void setControl(double position) {
        if (position == target) {
            return; // No need to update if we're already at the target
        }
        if (forwardLimitSwitchTriggered) {
            intakeMotor.stopMotor();
            intakeMotor.getEncoder().setPosition(position); // Reset the encoder position to the target position when
                                                            // the limit switch is triggered
            forwardLimitSwitchTriggered = false; // Reset the limit switch state
            return;
        }
        if (reverseLimitSwitchTriggered) {
            intakeMotor.stopMotor();
            intakeMotor.getEncoder().setPosition(position); // Reset the encoder position to the target position when
                                                            // the limit switch is triggered
            reverseLimitSwitchTriggered = false; // Reset the limit switch state
            return;
        }

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
