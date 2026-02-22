package frc.robot.subsystems.intake.feed;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;

public class FeedCTREIO implements FeedIO {
    private final CANBus canBus = new CANBus("CanBus");
    private final TalonFX feedMotor = new TalonFX(0, canBus);

    public FeedCTREIO() {
        FeedConfiguration feedMotorConfig = new FeedConfiguration();
        feedMotor.getConfigurator().apply(feedMotorConfig.getConfig());
    }

    public void runFeedMotor() {
        feedMotor.set(1.0);
    }

    public void stopFeedMotor() {
        feedMotor.set(0.0);
    }
}
