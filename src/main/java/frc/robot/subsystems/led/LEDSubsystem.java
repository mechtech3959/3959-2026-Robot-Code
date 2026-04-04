package frc.robot.subsystems.led;

import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
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

    private SolidColor solidColor;
    private RgbFadeAnimation rgbFadeAnimation;
    private EmptyAnimation emptyAnimation;
    private LarsonAnimation larsonAnimation;
    private StrobeAnimation strobeAnimation;
    private ColorFlowAnimation colorFlowAnimation;
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
            case DISABLED -> ledIO.setAnimation(solidColor.withColor(new RGBWColor(255, 174, 37))
                    .withLEDStartIndex(mainLEDStartIndex).withLEDEndIndex(mainLEDEndIndex));
            case AUTO -> ledIO.setAnimation(solidColor.withColor(new RGBWColor(0, 255, 0))
                    .withLEDStartIndex(mainLEDStartIndex).withLEDEndIndex(mainLEDEndIndex));
            case TELEOP -> {
                if (FieldBasedConstants.isBlueAlliance()) {
                    ledIO.setAnimation(solidColor.withColor(new RGBWColor(0, 0, 255))
                            .withLEDStartIndex(mainLEDStartIndex).withLEDEndIndex(mainLEDEndIndex));
                } else {
                    ledIO.setAnimation(solidColor.withColor(new RGBWColor(255, 0, 0))
                            .withLEDStartIndex(mainLEDStartIndex).withLEDEndIndex(mainLEDEndIndex));
                }
            }

            case MATCH_END -> {
                if (FieldBasedConstants.isBlueAlliance()) {
                    ledIO.setAnimation(colorFlowAnimation.withColor(new RGBWColor(0, 0, 255)).withFrameRate(20));
                } else {
                    ledIO.setAnimation(colorFlowAnimation.withColor(new RGBWColor(255, 0, 0)).withFrameRate(20));
                }
            }
        }
        switch (secondaryLEDStates) {
            case MATCH_READY ->
                ledIO.setAnimation(strobeAnimation.withColor(new RGBWColor(0, 255, 0)).withFrameRate(10)
                        .withLEDStartIndex(secondaryLEDStartIndex).withLEDEndIndex(secondaryLEDEndIndex));
            case HEADING_MISMATCH ->
                ledIO.setAnimation(strobeAnimation.withColor(new RGBWColor(255, 0, 0)).withFrameRate(10)
                        .withLEDStartIndex(secondaryLEDStartIndex).withLEDEndIndex(secondaryLEDEndIndex));
            case BOOTING -> ledIO.setAnimation(larsonAnimation.withColor(new RGBWColor(255, 255, 0)).withFrameRate(20)
                    .withLEDStartIndex(secondaryLEDStartIndex).withLEDEndIndex(secondaryLEDEndIndex));
            case HEADING_LOCK -> ledIO.setAnimation(solidColor.withColor(new RGBWColor(0, 255, 0))
                    .withLEDStartIndex(secondaryLEDStartIndex).withLEDEndIndex(secondaryLEDEndIndex));
        }
        switch (secondSecondaryLEDStates) {
            case INTAKING -> ledIO
                    .setAnimation(solidColor.withColor(new RGBWColor(255, 255, 0))
                            .withLEDStartIndex(secondSecondaryLEDStartIndex)
                            .withLEDEndIndex(secondSecondaryLEDEndIndex));
            case SHOOTING -> ledIO.setAnimation(solidColor.withColor(new RGBWColor(162, 115, 255))
                    .withLEDStartIndex(secondSecondaryLEDStartIndex).withLEDEndIndex(secondSecondaryLEDEndIndex));
            case INTAKING_AND_SHOOTING -> ledIO.setAnimation(solidColor.withColor(new RGBWColor(255, 0, 255))
                    .withLEDStartIndex(secondSecondaryLEDStartIndex).withLEDEndIndex(secondSecondaryLEDEndIndex));
            case NEITHER -> ledIO.setAnimation(larsonAnimation.withColor(new RGBWColor(0, 255, 255)).withFrameRate(20)
                    .withLEDStartIndex(secondSecondaryLEDStartIndex).withLEDEndIndex(secondSecondaryLEDEndIndex));
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
