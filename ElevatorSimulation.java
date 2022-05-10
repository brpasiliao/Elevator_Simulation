import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

class ElevatorSimulation {

    public static Elevator[] elevators = new Elevator[4];
    public static ArrayList<Person> peopleSystem = new ArrayList<Person>();

    //collecting simulation data from passengers
    static float waitingTime[]; // time passengers spend waiting for elevator
    static float systemTime[]; //system time equals waiting time plus riding time

    // public static void main(final String[] args) {
    //     Random random = new Random();
    //     for (int i = 0; i < 100; i++)
    //         System.out.println(Math.round((-6 * (float)Math.log(1 - random.nextFloat())) * 100) / 100f);
    // }

    public static void main(final String[] args) {
    // public static void program() {
        // initializes elevators
        for (int i = 0; i < 4; i++) {
            elevators[i] = new Elevator(i);
        }

        // initializes people
        for (int i = 0; i < 10; i++) {
            peopleSystem.add(new Person(i));
            //waitingTime[peopleSystem.get(i).] = 0;   // use passenger id to keep track of them in the simulation instead of i
        }

        Person p;
        while (!peopleSystem.isEmpty()) {
            // handles p assuming p is the next event to happen
            p = peopleSystem.get(0);

            // if p is waiting for an elevator
            if (p.status == "waiting") {
                //waitingTime[i] = waitingTime[i] + p.nextTime;   // use passenger id to keep track of them in the simulation instead of i
                // if p does not have an elevator being sent to them
                if (p.elevator == null) {
                    // call an elevator
                    Elevator e = dispatch(p);
                    System.out.println(p.nextTime + ":\tPerson" + p.id + " arrived on floor " + p.getFloorFrom());

                    // if no available elevators
                    if (e == null) {
                        // try again next time
                        System.out.println(p.nextTime + ":\tPerson" + p.id + " cannot call any elevators");
                        p.stuck = true;
                        moveBack(p);
                        //waitingTime[i] = waitingTime[i] + p.nextTime;  // // use passenger id to keep track of them in the simulation
                    // assigned an elevator
                    } else {
                        // send elevator to person
                        e.setDirection(p.getFloorFrom());
                        e.moving = true;
                        p.elevator = e;
                        e.peopleAffected.add(p);
                        System.out.println(p.nextTime + ":\tPerson" + p.id + " is waiting for Elevator" + e.id);
                    }
                } 
                // if p has an elevator being sent to them
                if (p.elevator != null) {
                    // if elevator is on the same floor as person
                    if (p.elevator.currentFloor == p.getFloorFrom()) {
                        p.status = "entering";
                    } else {
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
                System.out.println(p.nextTime + ":\tPerson" + p.id + " is riding Elevator" + p.elevator.id);
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
                System.out.println(p.nextTime + ":\tPerson" + p.id + " left on floor " + p.getFloorTo());

                peopleSystem.remove(0);
            }

            // sort to chronological order of events
            Collections.sort(peopleSystem, Person.TimeComparator);
        }

        // using collected data to calculate results, and outputting them
    }

    // closest elevator picks person(s) up
    public static Elevator dispatch(Person p) {
        Elevator closestElevator = null;
        int minDistance = 6;

        for (int i = 0; i < 4; i++) {
            // if elevator is on the same floor and going the same way
            if (elevators[i].moving && elevators[i].currentFloor == p.getFloorFrom() && elevators[i].direction == p.getDirection())
                closestElevator = elevators[i];
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
    public static void moveBack(Person stuckP) {
        int i = 0;
        Person p = peopleSystem.get(i);
        while (p.stuck) {
            i++;
            p = peopleSystem.get(i);
        }

        stuckP.nextTime = peopleSystem.get(i).nextTime;
        for (int j = 0; j < i; j++) 
            peopleSystem.set(j, peopleSystem.get(j+1));
        peopleSystem.set(i, stuckP);
    }
}