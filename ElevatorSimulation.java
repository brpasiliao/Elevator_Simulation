import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

class ElevatorSimulation {

    public static Elevator[] elevators = new Elevator[4];
    // FUTURE EVENTS LIST
    public static ArrayList<Person> peopleSystem = new ArrayList<Person>();
    public static int prints = 0;

    public static void main(final String[] args) {
    // public static void program() {

        // initializes elevators
        for (int i = 0; i < 4; i++) {
            elevators[i] = new Elevator(i);
        }

        int PersonID = 0;
        Person p = new Person(PersonID++);
        peopleSystem.add(p);

        while (!peopleSystem.isEmpty()) { // while fel is not empty
            // handles p assuming p is the next event to happen
            p = peopleSystem.get(0);

            // if p is waiting for an elevator
            if (p.status == "waiting") {
                Person.waitingTime += p.nextTime; // increases waiting time

                // if p does not have an elevator being sent to them
                if (p.getElevator() == null) {
                    // IF JUST ARRIVED, DECLARE ARRIVAL AND GENERATE NEW PERSON
                    if (p.first) {
                        print(p, "arrived");
                        if (PersonID < Integer.valueOf(args[0]))
                            peopleSystem.add(new Person(PersonID++));
                        p.first = false;
                    }
                    // call an elevator
                    Elevator e = dispatch(p);

                    // if no available elevators
                    if (e == null) {
                        // try again next time
                        print(p, "cannot call any elevators");
                        p.stuck = true;
                        unStuck(p);

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
                        print(p, "is waiting for Elevator" + p.elevator.id + " [" + p.elevator.currentFloor + "]");
                        p.elevator.move();
                    }
                }
            }

            // if p is entering an elevator
            else if (p.status == "entering") {
                print(p, "is entering Elevator" + p.elevator.id);
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
                Person.ridingTime = p.nextTime;

                print(p, "is riding Elevator" + p.elevator.id + " [" + p.elevator.currentFloor + "]");
                p.elevator.move();

                // remove people as long as there are those who need to get off on the elevator's current floor
                while (!p.elevator.peopleInside.isEmpty() && p.elevator.peopleInside.get(0).getFloorTo() == p.elevator.currentFloor) {
                    p.elevator.peopleInside.get(0).status = "leaving";
                    p.elevator.peopleInside.remove(0);
                }
            }

            // if p is leaving the elevator
            else if (p.status == "leaving") {
                print(p, "is leaving");
                p.elevator.leave(p);
                print(p, "left");

                peopleSystem.remove(0);
            }

            // SORTS FEL IN TIME ORDER
            Collections.sort(peopleSystem, Person.TimeComparator);
        }

        // System.out.println("On average, a person waited " + Person.waitingTime/10 + " in the system");
        // System.out.println("On average, a person rode " + Person.ridingTime/10 + " in the system");
        // System.out.println("On average, a person existed " + (Person.waitingTime/10 + Person.ridingTime/10) + " in the system");
    
    } //end main

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

    public static void shiftDown(Person p, int pos) {
        for (int i = 0; i < pos; i++) 
            peopleSystem.set(i, peopleSystem.get(i+1));
        peopleSystem.set(pos, p);
    }

    public static void print(Person p, String status) {
        if (prints++ < 20) {
            System.out.println(prints + " | " + p.nextTime + ": Person" + p.id + " [" + p.getFloorFrom() + "-" + p.getFloorTo() + "] " + status);
            System.out.println("FEL:");
            for (int i = 0; i < peopleSystem.size(); i++) {
                System.out.println("\t" + peopleSystem.get(i).nextTime + ": Person" + peopleSystem.get(i).getId() + " is " + peopleSystem.get(i).status);
            }
            System.out.print("\n");

            displayVisuals(); // extra credit for displaying elevator positions in diagram
        }
    }

    // Extra Credit for Visualization
    public static void displayVisuals() { 
        for (int i = 5; i > -1 ; i--){

            System.out.print(i + ": ");
            for (int j = 0; j < 4; j++){
                if (elevators[j].currentFloor == i) {
                    System.out.print("[] ");
                }
                else {
                    System.out.print("__ ");
                }
            }

            System.out.print("| ");
            for (int j = 0; j < peopleSystem.size(); j++) {
                if (peopleSystem.get(j).status == "waiting" && 
                    peopleSystem.get(j).getFloorFrom() == i &&
                    !peopleSystem.get(j).first)
                    System.out.print("(" + peopleSystem.get(j).getId() + ")");
            }

            System.out.println();
        }
        System.out.println("E: 0  1  2  3\n");
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