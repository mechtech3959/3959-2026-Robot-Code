package frc.robot.subsystems.intake;

import com.revrobotics.spark.config.SparkMaxConfig;

public class IntakeConfiguration {
    private final SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();

    public IntakeConfiguration() {
        intakeMotorConfig.softLimit
                .forwardSoftLimit(5.0)
                .forwardSoftLimitEnabled(true)
                .reverseSoftLimit(0.1)
                .reverseSoftLimitEnabled(true);

        intakeMotorConfig.closedLoop
                .pid(2.0, 0.0, 0.0)
                .feedForward
                .kS(0.15)
                .kV(0.12)
                .kCos(0.2)
                // kCosRatio = (Gear Ratio) * (2 * PI) = converts Rotations to Radians
                .kCosRatio(9.0 * 2.0 * Math.PI);

        intakeMotorConfig.closedLoop.maxMotion
                .cruiseVelocity(2000) // 2000 rpm
                .maxAcceleration(4000) // 2000/4000 = 0.5 sec to reach 2000 rpm
                .allowedProfileError(0.01); // Deadband
    }

    public SparkMaxConfig getConfig() {
        return intakeMotorConfig;
    }
}
