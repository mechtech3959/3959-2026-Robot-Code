package frc.robot.subsystems.led;

import com.ctre.phoenix6.controls.ControlRequest;

public interface LEDIO {
    default  void LEDInit(){}

    default void setLEDON() {
    }

    default void setLEDOFF() {
    }

    default void setLED() {
    }

    default void setAnimation(ControlRequest animation) {
    }
    default void resetAnimation(int slot){}

}
