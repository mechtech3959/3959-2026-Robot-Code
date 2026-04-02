package frc.robot.subsystems.led;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.hardware.CANdle;

public class LEDCTREIO implements LEDIO {
    private final CANdle ledController = new CANdle(2, new CANBus("rio"));
    
    @Override
    public void setLEDColor(int r, int g, int b) {
      //  ledController.setControl(new SolidColor(r, b) (r, g, b));
    }
} 
