package cs131.pa4.CarsTunnels;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cs131.pa4.Abstract.Scheduler;
import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

/**
 * The priority scheduler assigns vehicles to tunnels based on their priority
 * It extends the Scheduler class.
 * @author cs131a
 *
 */
public class PriorityScheduler extends Scheduler{
	//collection of tunnels passed in
	private final Collection<Tunnel>tunnels;
	
	//Creates a waitQ for the vehicle
	private final Queue<Vehicle> waitQ;
	
	//variable for returning the tunnel in the admit method
	private Tunnel t;
	
	//Mapping vehicles to tunnels
	private Map<Vehicle,Tunnel> pair = new HashMap<>();
	
	//Lock for the critical Section
	final Lock locked = new ReentrantLock();
	
	//Condition Variable for the lock
	final Condition tryEnter = locked.newCondition();

	/**
	 * Creates an instance of a priority scheduler with the given name and tunnels
	 * @param name the name of the priority scheduler
	 * @param tunnels the tunnels where the vehicles will be scheduled to
	 */
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name, tunnels);
		this.tunnels = tunnels;
		this.waitQ = new PriorityQueue<Vehicle>(new PriorityQueue<>((Vehicle vehicle1, Vehicle vehicle2) -> vehicle2.getPriority() - vehicle1.getPriority()));
	}

	/*
	 * Adds vehicles to a waitQ and tries to add them to the Queue if they meet the criteria
	 * @param takes in a vehicle trying to be added to a tunnel
	 */
	@Override
	public Tunnel admit(Vehicle vehicle) {
		
		try {
			locked.lock();
            
			waitQ.add(vehicle);
			
			
			while(true) {
				if(waitQ.peek().equals(vehicle)) {	
					if(!tryE(vehicle)) {
						tryEnter.await();
					}
					else {
						break;
					}
				}else {
					tryEnter.await();
				}
				
			}
               
            }catch (InterruptedException e) {
            	return null;
    		}
            finally {
                waitQ.poll();
                tryEnter.signalAll();
                t = pair.get(vehicle);
                locked.unlock();
            }
                return t;

            
		}  
		

	
	/*
	 * removes the desire vehicle
	 * @param the desired car to remove is passed in
	 */
	@Override
	public void exit(Vehicle vehicle) {
		try {
			locked.lock();
			Tunnel t = pair.get(vehicle);
			if(t!=null)
				t.exitTunnel(vehicle);
			tryEnter.signalAll();
		}finally {
			locked.unlock();
		}
		
	}
	
	/*
	 * tries to add the vehicle to the tunnel and returns true if added or false if it couldn't
	 * @param takes in a vehicle
	 */
	public boolean tryE(Vehicle vehicle) {
        if(vehicle==null) return false;
        for (Tunnel tunnel: tunnels) {
        	if (tunnel.tryToEnter(vehicle)) {
        		if (tunnel!=null) {
        			pair.put(vehicle, tunnel);
        			
        		}
        		
        	}
        }
        if(pair.containsKey(vehicle)) {
			return true;
		}else {

			return false;
		}
}

	
}
