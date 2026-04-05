package frc.robot.subsystems.led;

import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.signals.RGBWColor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.FieldBasedConstants;

public class LEDSubsystem extends SubsystemBase {
    public enum MainLEDStates {
        DISABLED,
        AUTO,
        TELEOP,
        MATCH_END

    }

    public enum SecondaryLEDStates {
        MATCH_READY,
        HEADING_MISMATCH,
        BOOTING,
        HEADING_LOCK

    }

    public enum SecondSecondaryLEDStates {
        INTAKING,
        SHOOTING,
        INTAKING_AND_SHOOTING,
        NEITHER

    }

    private final LEDIO ledIO;

    private MainLEDStates mainLEDStates = MainLEDStates.DISABLED;
    private SecondaryLEDStates secondaryLEDStates = SecondaryLEDStates.BOOTING;
    private SecondSecondaryLEDStates secondSecondaryLEDStates = SecondSecondaryLEDStates.NEITHER;
    // TODO GET THE LED VALUES
    private int mainLEDStartIndex = 0;
    private int mainLEDEndIndex = 100;
    private int secondaryLEDStartIndex = 25;
    private int secondaryLEDEndIndex = 50;
    private int secondSecondaryLEDStartIndex = 50;
    private int secondSecondaryLEDEndIndex = 75;

    public LEDSubsystem(LEDIO ledIO) {
        this.ledIO = ledIO;

    }

    public void handStates() {
        switch (mainLEDStates) {
            case DISABLED -> ledIO.setAnimation(
                    new SolidColor(mainLEDStartIndex, mainLEDEndIndex).withColor(new RGBWColor(255, 174, 37)));

            case AUTO -> ledIO.setAnimation(
                    new SolidColor(mainLEDStartIndex, mainLEDEndIndex).withColor(new RGBWColor(0, 255, 0)));
            case TELEOP -> {
                if (FieldBasedConstants.isBlueAlliance()) {
                    ledIO.setAnimation(
                            new SolidColor(mainLEDStartIndex, mainLEDEndIndex).withColor(new RGBWColor(0, 0, 255)));
                } else {
                    ledIO.setAnimation(
                            new SolidColor(mainLEDStartIndex, mainLEDEndIndex).withColor(new RGBWColor(255, 0, 0)));
                }
            }

            case MATCH_END -> {
                if (FieldBasedConstants.isBlueAlliance()) {
                    ledIO.setAnimation(new ColorFlowAnimation(mainLEDStartIndex, mainLEDEndIndex)
                            .withColor(new RGBWColor(0, 0, 255)).withFrameRate(20));
                } else {
                    ledIO.setAnimation(new ColorFlowAnimation(mainLEDStartIndex, mainLEDEndIndex)
                            .withColor(new RGBWColor(255, 0, 0)).withFrameRate(20));
                }
            }
        }
        switch (secondaryLEDStates) {
            case MATCH_READY ->
                ledIO.setAnimation(new StrobeAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                        .withColor(new RGBWColor(0, 255, 0)).withFrameRate(10));
            case HEADING_MISMATCH ->
                ledIO.setAnimation(new StrobeAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 0, 0)).withFrameRate(10));
            case BOOTING -> ledIO.setAnimation(new LarsonAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                    .withColor(new RGBWColor(255, 255, 0)).withFrameRate(20));
            case HEADING_LOCK -> ledIO.setAnimation(
                    new SolidColor(secondaryLEDStartIndex, secondaryLEDEndIndex).withColor(new RGBWColor(0, 255, 0)));
        }
        switch (secondSecondaryLEDStates) {
            case INTAKING -> ledIO
                    .setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                            .withColor(new RGBWColor(255, 255, 0)));
            case SHOOTING -> ledIO.setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                    .withColor(new RGBWColor(162, 115, 255)));
            case INTAKING_AND_SHOOTING ->
                ledIO.setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 0, 255)));
            case NEITHER ->
                ledIO.setAnimation(new LarsonAnimation(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(0, 255, 255)).withFrameRate(20));
        }

    }

    public void changeMainLEDState(MainLEDStates newState) {
        mainLEDStates = newState;
    }

    public void changeSecondaryLEDState(SecondaryLEDStates newState) {
        secondaryLEDStates = newState;
    }

    public void changeSecondSecondaryLEDState(SecondSecondaryLEDStates newState) {
        secondSecondaryLEDStates = newState;
    }

    @Override
    public void periodic() {
        handStates();
    }

}
