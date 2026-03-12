package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.encoder.SplineEncoder;
import com.revrobotics.encoder.config.DetachedEncoderConfig;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.RobotMap;

public class IntakeREVIO implements IntakeIO {
    private final SparkMax intakeMotor = new SparkMax(RobotMap.INTAKE.AXIS_MOTOR, SparkLowLevel.MotorType.kBrushless);
    private final SparkMaxConfig sparkMotorConfig = new SparkMaxConfig();

    //private final SplineEncoder intakeEncoder = new SplineEncoder(RobotMap.INTAKE.ENCODER);
    private double target = 0;
    private boolean forwardLimitSwitchTriggered = false; // Track the state of the limit switch
    private boolean reverseLimitSwitchTriggered = false; // Track the state of the limit switch
    // proper
    // canID

    public IntakeREVIO() {

        // intakeEncoder.configure(en, ResetMode.kResetSafeParameters,
        // PersistMode.kPersistParameters);
        sparkMotorConfig.smartCurrentLimit(40, 20);
        // sparkMotorConfig.apply(AbsoluteEncoderConfig.Presets.REV_SplineEncoder);
        sparkMotorConfig.idleMode(IdleMode.kBrake);
        sparkMotorConfig.inverted(true);
        // sparkMotorConfig.signals.absoluteEncoderPositionAlwaysOn(true);
        // sparkMotorConfig.absoluteEncoder.setSparkMaxDataPortConfig()
        // .apply(AbsoluteEncoderConfig.Presets.REV_SplineEncoder);

        sparkMotorConfig.softLimit
                .forwardSoftLimit(1.0)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(0.699)
                .reverseSoftLimitEnabled(true);
        sparkMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder, 43)
                .pid(2.0, 0.0, 0.0).feedForward
                .kS(0.15)
                .kV(0.12)
                
             //   .kCos(0.2)
                // kCosRatio = (Gear Ratio) * (2 * PI) = converts Rotations to Radians

                // removed 9
                .kCosRatio(2.0 * Math.PI);

        sparkMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(1.5) // 1.5 RAD/s = 14.3 rpm
                .maxAcceleration(3) // 0.5 sec to reach 1.5 RAD/s
                .allowedProfileError(1); // Deadband
        // IntakeConfiguration intakeMotorConfig = new IntakeConfiguration();
        intakeMotor.configure(sparkMotorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        // intakeEncoder.setPosition(1);

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
