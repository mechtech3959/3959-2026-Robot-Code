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
    // back left 138-158
    // back right 8-27
    // top back 28-60
    // left 61-98
    // right 99-137
    // CANdle 0-7
    private int mainLEDStartIndex = 0;
    private int mainLEDEndIndex = 158;// 9
    private int secondaryLEDStartIndex = 28;// 2
    private int secondaryLEDEndIndex = 60;// 4
    // left
    private int secondSecondaryLEDStartIndex = 138;// 6
    private int secondSecondaryLEDEndIndex = 158;// 8
    private int rightSecondSecondaryLEDStartIndex = 8;// 6
    private int rightSecondSecondaryLEDEndIndex = 27;// 8

    int segment1Start = mainLEDStartIndex;
    int segment1End = 7;

    int segment2Start = secondaryLEDEndIndex + 1;
    int segment2End = secondSecondaryLEDStartIndex - 1;

    int segment3Start = secondSecondaryLEDEndIndex + 1;
    int segment3End = mainLEDEndIndex;

    public LEDSubsystem(LEDIO ledIO) {
        this.ledIO = ledIO;
        ledIO.LEDInit();

    }

    public void handStates() {
        switch (mainLEDStates) {
            case DISABLED -> {
                ledIO.setAnimation(new SolidColor((segment1Start), (segment1End))
                        .withColor(new RGBWColor(10, 255, 1)));
                ledIO.setAnimation(new SolidColor((segment2Start), (segment2End))
                        .withColor(new RGBWColor(10, 255, 1)));
                ledIO.setAnimation(new SolidColor((segment3Start), (segment3End))
                        .withColor(new RGBWColor(10, 255, 1)));
            }
            case AUTO -> {
                ledIO.setAnimation(new SolidColor((segment1Start), (segment1End))
                        .withColor(new RGBWColor(0, 255, 0)));
                ledIO.setAnimation(new SolidColor((segment2Start), (segment2End))
                        .withColor(new RGBWColor(0, 255, 10)));
                ledIO.setAnimation(new SolidColor((segment3Start), (segment3End))
                        .withColor(new RGBWColor(0, 255, 0)));
            }

            case TELEOP -> {
                if (FieldBasedConstants.isBlueAlliance()) {

                    ledIO.setAnimation(new SolidColor((segment1Start), (segment1End))
                            .withColor(new RGBWColor(0, 0, 255)));
                    ledIO.setAnimation(new SolidColor((segment2Start), (segment2End))
                            .withColor(new RGBWColor(0, 0, 255)));
                    ledIO.setAnimation(new SolidColor((segment3Start), (segment3End))
                            .withColor(new RGBWColor(0, 0, 255)));

                } else {

                    ledIO.setAnimation(new SolidColor((segment1Start), (segment1End))
                            .withColor(new RGBWColor(255, 0, 0)));
                    ledIO.setAnimation(new SolidColor((segment2Start), (segment2End))
                            .withColor(new RGBWColor(255, 0, 0)));
                    ledIO.setAnimation(new SolidColor((segment3Start), (segment3End))
                            .withColor(new RGBWColor(255, 0, 0)));
                }
            }

            case MATCH_END -> {
                if (FieldBasedConstants.isBlueAlliance()) {
                    //ledIO.resetAnimation(0);
                    ledIO.setAnimation(new ColorFlowAnimation(mainLEDStartIndex, mainLEDEndIndex)
                            .withColor(new RGBWColor(0, 0, 255)).withFrameRate(5).withSlot(0));
                } else {
                    //ledIO.resetAnimation(0);

                    ledIO.setAnimation(new ColorFlowAnimation(mainLEDStartIndex, mainLEDEndIndex)
                            .withColor(new RGBWColor(255, 0, 0)).withFrameRate(5).withSlot(0));
                }
            }
        }
        switch (secondaryLEDStates) {
            case MATCH_READY ->
                ledIO.setAnimation(new ColorFlowAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                        .withColor(new RGBWColor(0, 255, 0)).withFrameRate(10).withSlot(1));
            case HEADING_MISMATCH ->
                ledIO.setAnimation(new StrobeAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 0, 0)).withFrameRate(10).withSlot(1));
            case BOOTING -> ledIO.setAnimation(new LarsonAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                    .withColor(new RGBWColor(255, 255, 0)).withFrameRate(5).withSlot(1));
            case HEADING_LOCK -> ledIO.setAnimation(new LarsonAnimation(secondaryLEDStartIndex, secondaryLEDEndIndex)
                    .withColor(new RGBWColor(255, 255, 0)).withFrameRate(5).withSlot(1));
            // ledIO.setAnimation(
            // new SolidColor(secondaryLEDStartIndex, secondaryLEDEndIndex).withColor(new
            // RGBWColor(0, 255, 0)));
        }
        switch (secondSecondaryLEDStates) {
            case INTAKING -> {
                                 //   ledIO.resetAnimation(2);

                ledIO.setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 255, 0)));
                ledIO.setAnimation(new SolidColor(rightSecondSecondaryLEDStartIndex, rightSecondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 255, 0)));
            }
            case SHOOTING -> {
                                //    ledIO.resetAnimation(2);

                ledIO.setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(162, 115, 255)));
                ledIO.setAnimation(new SolidColor(rightSecondSecondaryLEDStartIndex, rightSecondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(162, 115, 255)));
            }
            case INTAKING_AND_SHOOTING -> {
                                  //  ledIO.resetAnimation(2);

                ledIO.setAnimation(new SolidColor(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 0, 255)));
                ledIO.setAnimation(new SolidColor(rightSecondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(255, 0, 255)));
            }
            case NEITHER -> {
                ledIO.setAnimation(new LarsonAnimation(secondSecondaryLEDStartIndex, secondSecondaryLEDEndIndex)
                        .withColor(new RGBWColor(0, 255, 255)).withFrameRate(5).withSlot(2));
                ledIO.setAnimation(
                        new LarsonAnimation(rightSecondSecondaryLEDStartIndex, rightSecondSecondaryLEDEndIndex)
                                .withColor(new RGBWColor(0, 255, 255)).withFrameRate(5).withSlot(3));

            }
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
