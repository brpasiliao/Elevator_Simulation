import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

class ElevatorSimulation {

    public static Elevator[] elevators = new Elevator[4];
    public static ArrayList<Person> peopleSystem = new ArrayList<Person>();

    public static void main(final String[] args) {
        // initializes people
        for (int i = 0; i < 100; i++) {
            peopleSystem.add(new Person());
        }

        Person p;
        while (!peopleSystem.isEmpty()) {
            // handles p assuming p is the next event to happen
            p = peopleSystem.get(0);

            // if p is waiting for an elevator
            if (p.status == "waiting") {
                // if p does not have an elevator being sent to them
                if (p.elevator == null) {
                    // call an elevator
                    Elevator e = dispatch(p);

                    // if no available elevators
                    if (e == null) {
                        // try again next time
                        p.nextTime = peopleSystem.get(1).nextTime;
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
                    // if p 
                    if (p.elevator.currentFloor == p.getFloorFrom()) {
                        p.status = "entering";
                    } else {
                        p.elevator.move();
                    }
                }
            }

            // if p is entering an elevator
            else if (p.status == "entering") {
                p.elevator.enter();
                p.elevator.peopleInside.add(p);
                // include reverse
                Collections.sort(p.elevator.peopleInside, Person.FloorComparator);
                p.status = "riding";
            }

            // if p is riding the elevator
            else if (p.status == "riding") {
                p.elevator.move();

                while (p.elevator.peopleInside.get(0).getFloorTo() == p.elevator.currentFloor) {
                    p.elevator.peopleInside.get(0).status = "leaving";
                    p.elevator.peopleInside.remove(0);
                }
            }

            // if p is leaving the elevator
            else if (p.status == "leaving") {
                p.elevator.leave(p);
                peopleSystem.remove(0);
            }

            Collections.sort(peopleSystem, Person.TimeComparator);
        }
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
}