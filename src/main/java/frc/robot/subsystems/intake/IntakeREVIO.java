package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.encoder.SplineEncoder;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;

import frc.robot.RobotMap;

public class IntakeREVIO implements IntakeIO {
    private final SparkFlex intakeMotor = new SparkFlex(RobotMap.INTAKE.AXIS_MOTOR, SparkLowLevel.MotorType.kBrushless);
    private final SparkFlexConfig sparkMotorConfig = new SparkFlexConfig();

    private final SplineEncoder intakeEncoder = new SplineEncoder(RobotMap.INTAKE.ENCODER);
    private double target = 0;

    public IntakeREVIO() {
        sparkMotorConfig.closedLoop.feedbackSensor(FeedbackSensor.kDetachedAbsoluteEncoder, 43)
                .pid(1.65, 0.0005, 0).iZone(0.03)
                .iMaxAccum(0.05).feedForward.kS(0.1).kV(0.12); //.kCosRatio(2.0 * Math.PI);
        sparkMotorConfig.smartCurrentLimit(20, 40);

        sparkMotorConfig.idleMode(IdleMode.kBrake);
        sparkMotorConfig.inverted(true);

        sparkMotorConfig.softLimit
                .forwardSoftLimit(0.314)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(0.001)
                .reverseSoftLimitEnabled(true);

        sparkMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(20) // 1.5 RAD/s = 14.3 rpm
                .maxAcceleration(15) // 0.5 sec to reach 1.5 RAD/s
                .allowedProfileError(1); // Deadband
        // IntakeConfiguration intakeMotorConfig = new IntakeConfiguration();
        intakeMotor.configure(sparkMotorConfig, ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

    }

    @Override
    public void setControl(double position) {
        // if (position == target) {
        // return; // No need to update if we're already at the target
        // }

        intakeMotor.getClosedLoopController().setSetpoint(position, SparkBase.ControlType.kMAXMotionPositionControl);

        target = position;
    }

    @Override
    public void stop() {
        intakeMotor.stopMotor();
    }

    @Override
    public double getPosition() {
        return intakeEncoder.getAngle();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.axisPosition = getPosition();
        inputs.axisVelocity = intakeEncoder.getVelocity();
        inputs.axisTarget = target;
        inputs.axisCurrent = intakeMotor.getOutputCurrent();
        inputs.axisTemperature = intakeMotor.getMotorTemperature();
        inputs.isAtTarget = Math.abs(target - getPosition()) < 0.1; // Adjust the tolerance as needed
    }
}
