package frc.robot.subsystems.led;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.hardware.CANdle;

public class LEDCTREIO implements LEDIO  {
  //CHECK THE ID 
    private final CANdle ledController = new CANdle(13, new CANBus("rio"));
    //CANDle leds are unknown so we will just split them into 3 sections, left, middle, and right then set them to different colors for testing
    private int[] allLEDs = {0,129};
    private int[] CANdleLEDs = {0,9};
    private int[] middleLEDs = {40,90};
    private int[] leftLEDs = {10,39};
    private int[] rightLEDs = {91,129};
        public enum LEDState {
        OFF, ON, SOLID_COLOR, BLINKING
    }

    public enum LEDPattern {
        RAINBOW, THEATER_CHASE, COLOR_WIPE
    }

    public enum LEDColor {
        RED, GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE
    }

    public enum LEDBlinkPattern {
        SLOW_BLINK, FAST_BLINK, PULSE
    }

    public class LED {
      
        private  int[] ledIDs; // Range of LEDS [start, end] that this LED object controls
        private  SolidColor solidColor; // This would hold the SolidColor object for the color, which can be used to set the color of the LEDs
        private  RgbFadeAnimation rgbFadeAnimation; // This would hold the RgbFadeAnimation object for the pattern, which can be used to set the pattern of the LEDs
        private EmptyAnimation emptyAnimation; // This would hold the EmptyAnimation object for the pattern, which can be used to turn off the LEDs
        private LarsonAnimation larsonAnimation; // This would hold the LarsonAnimation object for the pattern, which can be used to set the pattern of the LEDs
        private LEDState state;
        private LEDPattern pattern;
        private LEDColor color;
        private LEDBlinkPattern blinkPattern;
        private int[] colorValues; // This would hold the RGB values for the color
        private int blinkRate; // This would hold the rate for blinking
        private int patternValue; // This would hold the value for the pattern, such as the speed or direction
        public LED(int[] ledIDs){
            this.ledIDs = ledIDs;
            this.state = LEDState.OFF;
            this.pattern = null;
            this.color = null;
            this.blinkPattern = null;
        };
        public LED(int[] ledIDs, LEDState state, LEDPattern pattern, LEDColor color, LEDBlinkPattern blinkPattern){
            this.ledIDs = ledIDs;
            this.state = state;
            this.pattern = pattern;
            this.color = color;
            this.blinkPattern = blinkPattern;
            this.solidColor = new SolidColor(ledIDs[0],ledIDs[1]);
            this.rgbFadeAnimation = new RgbFadeAnimation(ledIDs[0],ledIDs[1]);
         //   this.emptyAnimation = new EmptyAnimation(ledIDs[0],ledIDs[1]);
            this.larsonAnimation = new LarsonAnimation(ledIDs[0],ledIDs[1]);
        };

        public void update(){
                switch(state){
                    case OFF -> {}
                    case ON -> {}
                    case SOLID_COLOR ->  
                        setLEDColor(colorValues[0], colorValues[1], colorValues[2]);  
                    case BLINKING -> // This is a placeholder, you would need to implement a way to set the blink pattern based on the LEDBlinkPattern enum
                        {} // Example: set to blinking
                }
                switch (pattern) {
                    case RAINBOW -> {}
                    case THEATER_CHASE -> {}
                    case COLOR_WIPE -> {}
                    default -> throw new AssertionError();
                }
                }

        

        public void setState(LEDState state){
            this.state = state;
        };

        public void setPattern(LEDPattern pattern){
            this.pattern = pattern;
        };

        public void setColor(LEDColor color){
            this.color = color;
        };

        public void setBlinkPattern(LEDBlinkPattern blinkPattern){
            this.blinkPattern = blinkPattern;
        };

    }
    @Override
    public void setLEDColor(int r, int g, int b) {
      //  ledController.setControl(new SolidColor(r, b) (r, g, b));
    }
    public void setAnimation(ControlRequest animation) {
        ledController.setControl(animation);
    }
} 
