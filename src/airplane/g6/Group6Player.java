package airplane.g6;

import java.util.ArrayList;

import airplane.sim.Plane;
import airplane.sim.Player;
import org.apache.log4j.Logger;

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

    public double[] updatePlanes1(ArrayList<Plane> planes, int round, double[] bearings) {
        for (Plane p : planes) {
            if (p.getBearing() != -1 && p.getBearing() != -2) return bearings;
        }

        int minTime = 10000;
        int minIndex = 10000;
        for (int i = 0; i < planes.size(); i++) {
            Plane p = planes.get(i);
            if (p.getDepartureTime() < minTime && p.getBearing() == -1 && p.dependenciesHaveLanded(bearings)) {
                minIndex = i;
                minTime = p.getDepartureTime();
            }
        }

        if (round >= minTime) {
            Plane p = planes.get(minIndex);
            bearings[minIndex] = calculateBearing(p.getLocation(), p.getDestination());
        }

        return bearings;
    }


    @Override
    public double[] updatePlanes(ArrayList<Plane> planes, int round, double[] bearings) {
        int size = planes.size();
        int[] delay = new int[planes.size()];

        for (int i = 0; i < size; i++) {
            delay[i] = 10 * i;
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

                for (int j = 0; j < size; j++) {
                    if (j != i) {
                        Plane p1 = planes.get(i);
                        Plane p2 = planes.get(j);

                        if (p1.getLocation().distance(p2.getLocation()) <= 50) {
                            double newBearing = Player.calculateBearing(planes.get(i).getLocation(), planes.get(i).getDestination()) + 10;
                            double multiplier = planes.get(i).getLocation().distance(planes.get(i).getDestination());
                            multiplier /= 1 + planes.get(i).getLocation().distance(planes.get(i).getDestination());

                            if (Math.abs(newBearing - bearings[i]) >= 10) { bearings[i] += 9 * multiplier; }
                        } else if (p1.getLocation().distance(p2.getLocation()) <= 20) {
                            double newBearing = Player.calculateBearing(planes.get(i).getLocation(), planes.get(i).getDestination()) + 10;
                            double multiplier = planes.get(i).getLocation().distance(planes.get(i).getDestination());
                            multiplier /= 1 + planes.get(i).getLocation().distance(planes.get(i).getDestination());

                            if (Math.abs(newBearing - bearings[i]) >= 10) { bearings[i] += 9.2 * multiplier; }
                        }
                    }
                }
            }
        }

        return bearings;
    }
}
