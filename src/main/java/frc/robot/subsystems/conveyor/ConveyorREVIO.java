package frc.robot.subsystems.conveyor;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import frc.robot.RobotMap;

public class ConveyorREVIO implements ConveyorIO {
    private final SparkMax conveyorMotor = new SparkMax(RobotMap.CONVEYOR.CONVEYOR_MOTOR, MotorType.kBrushless);

    public ConveyorREVIO() {
        ConveyorConfiguration conveyorMotorConfig = new ConveyorConfiguration();
        conveyorMotor.configure(conveyorMotorConfig.getConfig(), ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);

    }

    @Override
    public void runConveyorMotor() {
        conveyorMotor.set(0.5);
    }

    @Override
    public void stopConveyorMotor() {
        conveyorMotor.set(0);
    }

    @Override
    public void reverseConveyorMotor(){
        conveyorMotor.set(-0.5);
    }
    @Override
    public void updateInputs(ConveyorIOInputs inputs) {
        inputs.motorCurrent = conveyorMotor.getOutputCurrent();
        inputs.motorVelocity = conveyorMotor.get();
        inputs.motorTemperature = conveyorMotor.getMotorTemperature();
    }

}
