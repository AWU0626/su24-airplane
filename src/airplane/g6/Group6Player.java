package airplane.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import airplane.sim.Plane;
import airplane.sim.Player;
import org.apache.log4j.Logger;

import javax.sound.sampled.Line;

public class Group6Player extends airplane.sim.Player {
    private Logger logger = Logger.getLogger(this.getClass()); // for logging

    @Override
    public String getName() {
        return "G6 Player";
    }

    @Override
    public void startNewGame(ArrayList<Plane> planes) {
        logger.info("Start new game");
    }

//    private double[] movePlane(ArrayList<Plane> planes, int round, double[] bearings, int[] delay) {
//        int size = planes.size();
//
//
//    }

    private int getQuadrant(double bearing) {
        if (bearing >= 0 && bearing < 90) {
            return 1;
        } else if (bearing >= 90 && bearing < 180) {
            return 2;
        } else if (bearing >= 180 && bearing < 270) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public double[] updatePlanes(ArrayList<Plane> planes, int round, double[] bearings) {
        int size = planes.size();
        int[] delay = new int[size];

        Map<Integer, Integer> quadrantDelayMap = new HashMap<>();
        quadrantDelayMap.put(1, 0);
        quadrantDelayMap.put(2, 0);
        quadrantDelayMap.put(3, 10);
        quadrantDelayMap.put(4, 10);

        // Determine the quadrant for each plane and assign delay
        for (int i = 0; i < size; i++) {
            delay[i] = 20 * i;
            delay[i] += Math.max(0, delay[i] - quadrantDelayMap.get(getQuadrant(bearings[i])));
        }

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (j != i) {
                    if (planes.get(i).getLocation() == planes.get(j).getLocation()) {
                        delay[j] += 10;
                    }
                }
            }
        }

// single plane
        if (size == 1) {
            if (bearings[0] == -2) return bearings;
            bearings[0] = calculateBearing(planes.get(0).getLocation(), planes.get(0).getDestination());
            return bearings;
        }

        // otherwise
        for (int i = 0; i < size; i++) {
            if (bearings[i] == - 2) continue; // stopping condition

            // delay
            if (round >= delay[i]) {
                bearings[i] = Player.calculateBearing(planes.get(i).getLocation(), planes.get(i).getDestination());
                if (bearings[i] >= 360) bearings[i] -= 360;

                for (int j = 0; j < size; j++) {
                    if (j > i) {
                        Plane p1 = planes.get(i);
                        Plane p2 = planes.get(j);

                        if (p1.getLocation().distance(p2.getLocation()) <= 100) {
                            double newBearing = Player.calculateBearing(planes.get(i).getLocation(), planes.get(i).getDestination()) + 10;
                            double multiplier = planes.get(i).getLocation().distance(planes.get(i).getDestination());
                            multiplier /= 1 + planes.get(i).getLocation().distance(planes.get(i).getDestination());

                            if (Math.abs(newBearing - bearings[i]) >= 10) {
                                Line2D line1 = new Line2D.Double(p1.getLocation().getX(), p1.getLocation().getY(), p1.getDestination().getX(), p1.getDestination().getY());
                                Line2D line2 = new Line2D.Double(p2.getLocation().getX(), p2.getLocation().getY(), p2.getDestination().getX(), p2.getDestination().getY());

                                if (line2.intersectsLine(line1) ||
                                        (Math.abs(bearings[i] - bearings[j]) <= 225 && Math.abs(bearings[i] - bearings[j]) >= 135)
                                ) {
                                    bearings[i] += 9.2 * multiplier;
                                    if (bearings[i] >= 360) bearings[i] -= 360;
                                } else {
                                    bearings[i] += 1;
                                    if (bearings[i] >= 360) bearings[i] -= 360;
                                }
                            }
                        } else if (p1.getLocation().distance(p2.getLocation()) <= 50) {
                            double newBearing = Player.calculateBearing(planes.get(i).getLocation(), planes.get(i).getDestination()) + 10;
                            double multiplier = planes.get(i).getLocation().distance(planes.get(i).getDestination());
                            multiplier /= 1 + planes.get(i).getLocation().distance(planes.get(i).getDestination());

                            if (Math.abs(newBearing - bearings[i]) >= 10) {
                                Line2D line1 = new Line2D.Double(p1.getLocation().getX(), p1.getLocation().getY(), p1.getDestination().getX(), p1.getDestination().getY());
                                Line2D line2 = new Line2D.Double(p2.getLocation().getX(), p2.getLocation().getY(), p2.getDestination().getX(), p2.getDestination().getY());

                                if (line2.intersectsLine(line1) && Math.abs(bearings[i] - bearings[j]) <= 225 && Math.abs(bearings[i] - bearings[j]) >= 135) {
                                    bearings[i] += 9.4 * multiplier;
                                    if (bearings[i] >= 360) bearings[i] -= 360;
                                } else {
                                    bearings[i] += 1;
                                    if (bearings[i] >= 360) bearings[i] -= 360;
                                }
                            } else {
                                bearings[i] += 1;
                                if (bearings[i] >= 360) bearings[i] -= 360;
                            }
                        }
                    }
                }
            }
        }

        return bearings;
    }
}
