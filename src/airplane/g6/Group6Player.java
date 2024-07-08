package airplane.g6;

import java.util.ArrayList;

import airplane.sim.Plane;

class Group6Player extends airplane.sim.Player {
    String playerName;

    public Group6Player(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getName() {
        return this.playerName;
    }

    @Override
    public void startNewGame(ArrayList<Plane> planes) {
        throw new UnsupportedOperationException("Unimplemented method 'startNewGame'");
    }

    @Override
    public double[] updatePlanes(ArrayList<Plane> planes, int round, double[] bearings) {
        int size = planes.size();

        for (int i = 0; i < size; i++) {
            Plane currentPlane = planes.get(i);
            if (currentPlane.getLocation() == currentPlane.getDestination()) {
                currentPlane.setBearing(-2);
            }

            if (currentPlane.getDepartureTime() == round && currentPlane.getBearing() == -1) {
                currentPlane.setBearing(calculateBearing(currentPlane.getLocation(), currentPlane.getDestination()));
            } else {
                double radialBearing = currentPlane.getBearing() % 360;
                radialBearing = (radialBearing - 90) * Math.PI / 180;
                double newX = currentPlane.getX() + (Math.cos(radialBearing));
                double newY = currentPlane.getY() + (Math.sin(radialBearing));

                currentPlane.setX(newX);
                currentPlane.setY(newY);
            }


            for (int j = i; j < size; j++) {
                Plane others = planes.get(j);

                if (j != i) {
                    double distanceXSquared = (others.getX() - currentPlane.getX()) * (others.getX() - currentPlane.getX());
                    double distanceYSquared = (others.getY() - currentPlane.getY()) * (others.getY() - currentPlane.getY());
                    double distance = Math.sqrt(distanceXSquared + distanceYSquared);
                    if (distance < 10) {
                        currentPlane.setBearing(currentPlane.getBearing() + 10);
                        others.setBearing(others.getBearing() + 10);
                        bearings[i] = currentPlane.getBearing();
                        bearings[j] = others.getBearing();
                    }
                }
            }
        }

        round++;
        return bearings;
    }

}