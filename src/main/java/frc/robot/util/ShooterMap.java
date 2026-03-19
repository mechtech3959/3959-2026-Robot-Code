package frc.robot.util;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class ShooterMap {
    InterpolatingDoubleTreeMap speedForHubShot = new InterpolatingDoubleTreeMap();
    public ShooterMap() {
        // Populate the map with distance (in inches) to shooter speed (in RPS)
        speedForHubShot.put(0.0, 0.0); // Example entry for testing
        speedForHubShot.put(60.0, 3000.0); // Example entry for testing
        speedForHubShot.put(120.0, 6000.0); // Example entry for testing
    }
    public double getShooterSpeedForDistance(double distanceInInches) {
        return speedForHubShot.get(distanceInInches);
    }
}
