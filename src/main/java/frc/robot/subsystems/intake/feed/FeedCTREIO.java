package frc.robot.subsystems.intake.feed;

import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.RobotMap;

public class FeedCTREIO implements FeedIO {
    private final TalonFX feedMotor = new TalonFX(RobotMap.INTAKE.FEED_MOTOR, RobotMap.CAN.SLOW_BUS);

    public FeedCTREIO() {
        FeedConfiguration feedMotorConfig = new FeedConfiguration();
        feedMotor.getConfigurator().apply(feedMotorConfig.getConfig());
    }

    @Override
    public void runFeedMotor() {
        feedMotor.set(0.75);
    }

    @Override
    public void stopFeedMotor() {
        feedMotor.set(0.0);
    }
    @Override
    public void setSpeed(double speed) {
        feedMotor.set(speed);
    }
    @Override 
    public void updateInputs(FeedIOInputs inputs) {
        inputs.motorStaturCurrent = feedMotor.getStatorCurrent().getValueAsDouble();
        inputs.motorSupplyCurrent = feedMotor.getSupplyCurrent().getValueAsDouble();
        inputs.motorTemperature = feedMotor.getDeviceTemp().getValueAsDouble();
        inputs.motorVelocity = feedMotor.getVelocity().getValueAsDouble();
    }
}
