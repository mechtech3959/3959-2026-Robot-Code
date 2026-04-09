package frc.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class ShooterMap {
    InterpolatingDoubleTreeMap speedForHubShot = new InterpolatingDoubleTreeMap();
    public ShooterMap() {
        // Populate the map with distance (in inches) to shooter speed (in RPM)
        speedForHubShot.put( 1.2192, 43.0); //4 feet
        speedForHubShot.put(1.8288, 47.0); //6 feet
        speedForHubShot.put(2.4384, 51.0); //8 feet
        speedForHubShot.put(3.048, 55.0); //10 feet
        speedForHubShot.put(3.6576, 61.5); //12 feet
    }
    public double getShooterSpeedForDistance(double distanceInMeters) {
        return speedForHubShot.get(distanceInMeters);
    }
}
