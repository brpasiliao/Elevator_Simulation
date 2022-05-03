import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

class ElevatorSimulation {

    Elevator[] elevators = new Elevator[4];

    public static void main(final String[] args) {
        List fel = new List();
        List delayed_list = new List();
        
        while(! fel.get(0).equals("end_of_simulation")){
            delayed_list.add(fel.get(0));
            fel.add(Event) // generate new arrival (passenger generation)
            fel.add(Event) // elevator event
        }
    }

    public Elevator dispatch(Person p) {   // closest elevator picks person(s) up
        Elevator closestElevator = elevators[0];
        int minDistance = 5;

        for (int i = 0; i < 4; i++)
            if (!(elevators[i].moving && elevators[i].direction == p.getDirection()))
                if (Math.abs(elevators[i].currentFloor - p.getFloorFrom()) < minDistance) {
                    closestElevator = elevators[i];
                    minDistance = Math.abs(elevators[i].currentFloor - p.getFloorFrom());
                }

        return closestElevator;
    }

    // dispatch elevator as soon as person is instantiated

    // void call() { // elevator is called
    //     if from floor0: 
    //         10 people per min = 1 person / 6 sec = average 6 sec arrivalTime
    //     else : 
    //         5 people per min = 1 person / 12 sec = average 12 sec arrivalTime
    // }
}