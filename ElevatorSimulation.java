import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

class ElevatorSimulation {

    public static Elevator[] elevators = new Elevator[4];
    public static ArrayList<Person> peopleSystem = new ArrayList<Person>();

    //collecting simulation data from passengers
    static float[] waitingTime = new float[10]; // time passengers spend waiting for elevator
    static float[] ridingTime = new float[10]; // time passengers spend in the elevator
    static float[] systemTime = new float[10]; //system time equals waiting time plus riding time

    public static void main(final String[] args) {
    // public static void program() {
        // initializes elevators
        for (int i = 0; i < 4; i++) {
            elevators[i] = new Elevator(i);
        }

        // initializes people
        for (int i = 0; i < 10; i++) {
            peopleSystem.add(new Person(i));
            waitingTime[i] = 0.0f; //all waiting times set to zero initially
        }

        Person p;

        while (!peopleSystem.isEmpty()) {
            // handles p assuming p is the next event to happen
            p = peopleSystem.get(0);

            // if p is waiting for an elevator
            if (p.status == "waiting") {
                waitingTime[p.getId()] += p.nextTime; // increases waiting time by however much
                // if p does not have an elevator being sent to them
                if (p.getElevator() == null) {
                    // call an elevator
                    if (p.first) {
                        System.out.println(p.nextTime + ":\tPerson" + p.id + " arrived on floor " + p.getFloorFrom() + " to " + p.getFloorTo());
                        p.first = false;
                    }
                    Elevator e = dispatch(p);

                    // if no available elevators
                    if (e == null) {
                        // try again next time
                        System.out.println(p.nextTime + ":\tPerson" + p.id + " cannot call any elevators");
                        p.stuck = true;
                        unStuck(p);
                        //waitingTime[i] = waitingTime[i] + p.nextTime;  // // use passenger id to keep track of them in the simulation
                    // assigned an elevator
                    } else {
                        // send elevator to person
                        e.setDirection(p.getFloorFrom());
                        e.moving = true;
                        p.elevator = e;
                        e.peopleAffected.add(p);
                    }
                } 
                // if p has an elevator being sent to them
                if (p.elevator != null) {
                    // if elevator is on the same floor as person
                    if (p.elevator.currentFloor == p.getFloorFrom()) {
                        p.status = "entering";
                    } else {
                        System.out.println(p.nextTime + ":\tPerson" + p.id + " is waiting for Elevator" + p.elevator.id + " (" + p.elevator.currentFloor + "/" + p.getFloorFrom() + ")");
                        p.elevator.move();
                    }
                }
            }

            // if p is entering an elevator
            else if (p.status == "entering") {
                System.out.println(p.nextTime + ":\tPerson" + p.id + " is entering Elevator" + p.elevator.id);
                p.elevator.enter();
                p.elevator.peopleInside.add(p);
                p.elevator.setDirection(p.getFloorTo());

                // sort by nearest floor
                if (p.elevator.direction) 
                    Collections.sort(p.elevator.peopleInside, Person.FloorUpComparator);
                else Collections.sort(p.elevator.peopleInside, Person.FloorDownComparator);
                
                p.status = "riding";
            }

            // if p is riding the elevator
            else if (p.status == "riding") {
                System.out.println(p.nextTime + ":\tPerson" + p.id + " is riding Elevator" + p.elevator.id + " (" + p.elevator.currentFloor + "/" + p.getFloorTo() + ")");
                p.elevator.move();

                // remove people as long as there are those who need to get off on the elevator's current floor
                while (!p.elevator.peopleInside.isEmpty() && p.elevator.peopleInside.get(0).getFloorTo() == p.elevator.currentFloor) {
                    p.elevator.peopleInside.get(0).status = "leaving";
                    p.elevator.peopleInside.remove(0);
                }
            }

            // if p is leaving the elevator
            else if (p.status == "leaving") {
                System.out.println(p.nextTime + ":\tPerson" + p.id + " is leaving");
                p.elevator.leave(p);
                System.out.println(p.nextTime + ":\tPerson" + p.id + " left from floor " + p.getFloorFrom() + " to " + p.getFloorTo());

                peopleSystem.remove(0);
            }

            // sort to chronological order of events
            Collections.sort(peopleSystem, Person.TimeComparator);
        }

        // using collected data to calculate results, and outputting them
    }//main

    // closest elevator picks person(s) up
    public static Elevator dispatch(Person p) {
        Elevator closestElevator = null;
        int minDistance = 6;

        for (int i = 0; i < 4; i++) {
            // if elevator is on the same floor and going the same way
            if (elevators[i].moving && elevators[i].currentFloor == p.getFloorFrom() && elevators[i].direction == p.getDirection()) {
                closestElevator = elevators[i];
                minDistance = 0;
            }
            // else if the elevator is not moving and is the closest to desired floor
            else if (!elevators[i].moving) {
                if (Math.abs(elevators[i].currentFloor - p.getFloorFrom()) < minDistance) {
                    closestElevator = elevators[i];
                    minDistance = Math.abs(elevators[i].currentFloor - p.getFloorFrom());
                }
            }
        }
        
        return closestElevator;
    }

    // moves person that cannot call elevator behind the next person that is not also stuck
    public static void unStuck(Person stuckP) {
        int i = 0;
        Person p = peopleSystem.get(i);
        while (p.stuck && i < peopleSystem.size()-1) {
            i++;
            p = peopleSystem.get(i);
        }

        stuckP.nextTime = peopleSystem.get(i).nextTime;
        shiftDown(stuckP, i);
    }

    public static void shiftDown (Person p, int pos) {
        for (int i = 0; i < pos; i++) 
            peopleSystem.set(i, peopleSystem.get(i+1));
        peopleSystem.set(pos, p);
    }

    public void displayVisuals(Elevator[] elevators){ // work in progress

        for(int i = 6; i > 0 ; i--){
            System.out.print(i+": ");
            for (int j = 0; j > 4; j++){
                if(elevators[j].currentFloor == i){
                    System.out.print("[] ");
                }
                else{
                    System.out.print("__ ");
                }
            }
            System.out.println();
        }
    }
}

/*
Visualization:

 __ is a floor
 [] is an elevator
 6 floors (rows) * 4 elevators (columns)

 Ex output:

 6: __ [] __ __
 5: __ __ __ __
 4: [] __ __ __
 3: __ __ [] __
 2: __ __ __ __
 1: __ __ __ []
 E: 1  2  3  4
*/