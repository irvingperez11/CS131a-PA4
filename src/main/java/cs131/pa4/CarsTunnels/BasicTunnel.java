package cs131.pa4.CarsTunnels;

import java.util.ArrayList;

import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

// TODO: Auto-generated Javadoc
/**
 * 
 * The class for the Basic Tunnel, extending Tunnel.
 * @author cs131a
 *
 */
public class BasicTunnel extends Tunnel{
	
	/** The cars. */
	public ArrayList<Vehicle> cars = new ArrayList<>();
	
	/**
	 * Creates a new instance of a basic tunnel with the given name.
	 *
	 * @param name the name of the basic tunnel
	 */
	public BasicTunnel(String name) {
		super(name);
	}

	/**
	 * Try to enter inner.
	 *
	 * @param vehicle the vehicle
	 * @return true, if successful
	 */
	@Override
	protected boolean tryToEnterInner(Vehicle vehicle) {
		if(cars.isEmpty() ) {
			cars.add(vehicle);
			return true;
		}
		else if(cars.size() < 3) {
			int x = 0; 
			if(vehicle instanceof Sled || cars.get(x) instanceof Sled ) {
				return false;
			}
						
			else if(!cars.get(x).getDirection().equals(vehicle.getDirection())) {
                            return false;
                        }
			else {
                            cars.add(vehicle);
                            return true;
                        }
		}
		
		return false;

	}

	/**
	 * Exit tunnel inner.
	 *
	 * @param vehicle the vehicle
	 */
	@Override
	protected void exitTunnelInner(Vehicle vehicle) {
		cars.remove(vehicle);
	}
	
}
