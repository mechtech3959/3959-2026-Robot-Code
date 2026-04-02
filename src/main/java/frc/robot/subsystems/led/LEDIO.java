package frc.robot.subsystems.led;

public interface LEDIO {

    default void setLEDColor(int r, int g, int b) {
    }
    default void setLEDON(){}
    default void setLEDOFF(){}
    default void setLED(){}

} 
