package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.RobotMap;

public class ShooterCTREIO implements ShooterIO {
    private final TalonFX leftShooter;
    private final TalonFX rightShooter;
    private final VelocityVoltage velocityVoltage = new VelocityVoltage(0);
    private final NeutralOut neutralOut = new NeutralOut();
    CANBus tempBus = new CANBus("rio");

    private double target = 0;

    public ShooterCTREIO() {
        this.leftShooter = new TalonFX(RobotMap.SHOOTER.LEFT_SHOOTER, tempBus);
        this.rightShooter = new TalonFX(RobotMap.SHOOTER.RIGHT_SHOOTER, tempBus);
        leftShooter.getConfigurator().apply(ShooterConfig.leftShooterConfiguration());
        rightShooter.getConfigurator().apply(ShooterConfig.rightShooterConfiguration());
        leftShooter.setControl(new StrictFollower(rightShooter.getDeviceID()));
    }

    @Override
    public void setShooterSpeed(double speed) {
        if (target != speed) {
            rightShooter.setControl(velocityVoltage.withVelocity(speed));
            target = speed;
        }
    }

    @Override
    public void setShooterNeutral() {
        rightShooter.setControl(neutralOut);
        target = 0;
    }

    @Override
    public double getShooterSpeed() {
        return rightShooter.getVelocity().getValueAsDouble();

    }

    @Override
    public boolean isNearTargetSpeed() {
        return rightShooter.getVelocity().isNear(target, 25); // Adjust the tolerance as needed

    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.leftShooterSpeedRPS = leftShooter.getVelocity().getValueAsDouble();
        inputs.rightShooterSpeedRPS = rightShooter.getVelocity().getValueAsDouble();
        inputs.leftShooterSpeedRPM = leftShooter.getVelocity().getValueAsDouble() * 60 / 2048;
        inputs.rightShooterSpeedRPM = rightShooter.getVelocity().getValueAsDouble() * 60 / 2048;
        inputs.targetSpeedRPS = target;
        inputs.atTargetSpeed = isNearTargetSpeed();
        inputs.LeftShooterCurrentAmps = leftShooter.getStatorCurrent().getValueAsDouble();
        inputs.RightShooterCurrentAmps = rightShooter.getStatorCurrent().getValueAsDouble();
    }
}
