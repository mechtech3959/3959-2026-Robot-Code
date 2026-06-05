package frc.robot.subsystems.led;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.EmptyAnimation;

import com.ctre.phoenix6.hardware.CANdle;

public class LEDCTREIO implements LEDIO {
    // CHECK THE ID
    private final CANdle ledController = new CANdle(17, new CANBus("rio"));

    @Override
    public void LEDInit() {
        ledController.clearAllAnimations();

    }

    @Override
    public void setAnimation(ControlRequest animation) {
        ledController.setControl(animation);
    }

    @Override
    public void resetAnimation(
    int slot){
     //   ledController.setControl(new EmptyAnimation().withSlot(slot));
    }
}
