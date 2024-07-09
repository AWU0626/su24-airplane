package airplane.g6;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import airplane.sim.Plane;
import airplane.sim.Player;


public class SerializedPlayer extends Player {

	private Logger logger = Logger.getLogger(this.getClass()); // for logging
	
	@Override
	public String getName() {
		return "Serialized Player";
	}
	
	/*
	 * This is called at the beginning of a new simulation. 
	 * Each Plane object includes its current location (origin), destination, and
	 * current bearing, which is -1 to indicate that it's on the ground.
	 */
	@Override
	public void startNewGame(ArrayList<Plane> planes) {
		logger.info("Starting new game!");

	}
	
	/*
	 * This is called at each step of the simulation.
	 * The List of Planes represents their current location, destination, and current
	 * bearing; the bearings array just puts these all into one spot. 
	 * This method should return an updated array of bearings.
	 */
//	@Override
//	public double[] updatePlanes(ArrayList<Plane> planes, int round, double[] bearings) {
//
//		// if any plane is in the air, then just keep things as-is
//		for (Plane p : planes) {
//		    if (p.getBearing() != -1 && p.getBearing() != -2) return bearings;
//		}
//
//		// if no plane is in the air, find the one with the earliest
//		// departure time and move that one in the right direction
//		int minTime = 10000;
//		int minIndex = 10000;
//		for (int i = 0; i < planes.size(); i++) {
//			Plane p = planes.get(i);
//		    if (p.getDepartureTime() < minTime && p.getBearing() == -1 && p.dependenciesHaveLanded(bearings)) {
//				minIndex = i;
//				minTime = p.getDepartureTime();
//		    }
//		}
//
//		// if it's not too early, then take off and head straight for the destination
//		if (round >= minTime) {
//		    Plane p = planes.get(minIndex);
//		    bearings[minIndex] = calculateBearing(p.getLocation(), p.getDestination());
//		}
//
//
//		return bearings;
//	}
//
//
	@Override
	public double[] updatePlanes(ArrayList<Plane> planes, int round, double[] bearings) {
		int size = planes.size();

		if (size == 1) {
			if (bearings[0] == -2) return bearings;

			bearings[0] = 1.1 * calculateBearing(planes.get(0).getLocation(), planes.get(0).getDestination());

			if (1.05 * calculateBearing(planes.get(0).getLocation(), planes.get(0).getDestination()) >= 10) {
				bearings[0] = 0.95 * calculateBearing(planes.get(0).getLocation(), planes.get(0).getDestination());
			}
			return bearings;
		}

		for (int i = 0; i < size; i++) {
			Plane currentPlane = planes.get(i);

			double newBearing = calculateBearing(currentPlane.getLocation(), currentPlane.getDestination());

			double x = (currentPlane.getX() + (Math.cos(newBearing)));
			double y = (currentPlane.getY() + (Math.sin(newBearing)));

			for (int j = i; j < size; j++) {
				if (j != i) {
					Plane otherPlane = planes.get(j);
					double newBearing2 = calculateBearing(otherPlane.getLocation(), otherPlane.getDestination());
					double x2 = (otherPlane.getX() + (Math.cos(newBearing2)));
					double y2 = (otherPlane.getY() + (Math.sin(newBearing2)));

					double distanceX = x2 - x;
					double distanceY = y2 - y;

//					if (Math.abs(newBearing - currentPlane.getBearing()) > 10) {
//						if (newBearing < 0) {
//							newBearing += 5;
//						} else {
//							newBearing -= 5;
//						}
//					}

					if (bearings[i] != -2 && bearings[j] != -2) {
						if (Math.sqrt(distanceX * distanceX + distanceY * distanceY) >= 500) {
							bearings[i] = newBearing * 1.2;
							bearings[j] = newBearing2 * 1.2;
						} else if (Math.sqrt(distanceX * distanceX + distanceY * distanceY) >= 200) {
							bearings[i] = newBearing * 1.15;
							bearings[j] = newBearing2 * 1.15;
						} else {
							bearings[i] = newBearing * 1.1;
							bearings[j] = newBearing2 * 1.1;
						}
					}
				}
			}
		}

		return bearings;
	}

	public double[] updatePlanes1(ArrayList<Plane> planes, int round, double[] bearings) {
		int size = planes.size();

		for (int i = 0; i < size; i++) {
			Plane currentPlane = planes.get(i);
			if (currentPlane.getLocation() == currentPlane.getDestination()) {
//				currentPlane.setBearing(-2);
			}


			if (currentPlane.getDepartureTime() == round && currentPlane.getBearing() == -1) {
				currentPlane.setBearing(calculateBearing(currentPlane.getLocation(), currentPlane.getDestination()));
			} else if (currentPlane.getBearing() != -1){
				double radialBearing = currentPlane.getBearing() % 360;
				radialBearing = (radialBearing - 90) * Math.PI / 180;
				double newX = currentPlane.getX() + (Math.cos(radialBearing));
				double newY = currentPlane.getY() + (Math.sin(radialBearing));

				currentPlane.setX(newX);
				currentPlane.setY(newY);
			}


//			for (int j = i; j < size; j++) {
//				Plane others = planes.get(j);
//
//				if (j != i) {
//					double distanceXSquared = (others.getX() - currentPlane.getX()) * (others.getX() - currentPlane.getX());
//					double distanceYSquared = (others.getY() - currentPlane.getY()) * (others.getY() - currentPlane.getY());
//					double distance = Math.sqrt(distanceXSquared + distanceYSquared);
//					if (distance < 10) {
//						currentPlane.setBearing(currentPlane.getBearing() + 10);
//						others.setBearing(others.getBearing() + 10);
//						bearings[i] = currentPlane.getBearing();
//						bearings[j] = others.getBearing();
//					}
//				}
//			}
		}

		return bearings;
	}
}
