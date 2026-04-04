package frc.robot.subsystems.led;

public interface LEDIO {
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

      public  class LED {
        private final int[] ledIDs; // This would hold the IDs of the LEDs that this LED object controls
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

        public void update(){
               
        };

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
    default void setLEDColor(int r, int g, int b) {
    }
    default void setLEDON(){}
    default void setLEDOFF(){}
    default void setLED(){}

}
