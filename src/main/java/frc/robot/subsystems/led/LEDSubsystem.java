package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {

    private final LEDIO ledIO;



    public LEDSubsystem(LEDIO ledIO) {
        this.ledIO = ledIO;
    }

    public void setLEDColor(int r, int g, int b) {
        ledIO.setLEDColor(r, g, b);
    }

    public void setLEDON() {
        ledIO.setLEDON();
    }

    public void inputHandler() {
    }

}
