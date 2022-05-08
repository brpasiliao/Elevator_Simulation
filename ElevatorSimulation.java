import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

class ElevatorSimulation {

    public static Elevator[] elevators = new Elevator[4];
    public static ArrayList<Person> peopleF0 = new ArrayList<Person>();
    public static ArrayList<Person> peopleF1 = new ArrayList<Person>();
    public static ArrayList<Person> peopleF2 = new ArrayList<Person>();
    public static ArrayList<Person> peopleF3 = new ArrayList<Person>();
    public static ArrayList<Person> peopleF4 = new ArrayList<Person>();
    public static ArrayList<Person> peopleF5 = new ArrayList<Person>();
    // public static ArrayList<Person> peopleSystem = new ArrayList<Person>();

    public static void main(final String[] args) {
        // List fel = new List();
        // List delayed_list = new List();
        
        // while(! fel.get(0).equals("end_of_simulation")){
        //     delayed_list.add(fel.get(0));
        //     fel.add(Event) // generate new arrival (passenger generation)
        //     fel.add(Event) // elevator event
        // }
        
        for (int i = 0; i < 100; i++) {
            Person p = new Person();
            ArrayList<Person> peopleOnFloor = getPeopleOnFloor(p.getFloorFrom());
            peopleOnFloor.add(p);

            Elevator e = dispatch(p);
            e.setDirection(p.getFloorFrom());
            e.moving = true;
            while (e.currentFloor != p.getFloorFrom()) {
                // handle if other people call elevator

                e.fetch(peopleOnFloor);
            }
            e.moving = false;

            e.direction = p.getDirection();
            for (int j = 0; j < peopleOnFloor.size(); j++) {
                // if people on floor are going in the same direction as the person who called the elevator
                if (peopleOnFloor.get(j).getDirection() == e.direction) {
                    // adds person to elevator
                    e.peopleInside.add(peopleOnFloor.get(j));
                    // removes person from floor
                    peopleOnFloor.remove(j);
                    // adds time waited for 
                    peopleOnFloor.get(j).addTotalTime(peopleOnFloor.size() * e.timeOn);
                }
            }
            // sort people by desired floor whenever new people get on
            if (e.direction) Collections.sort(e.peopleInside);
            else Collections.reverse(e.peopleInside);

            e.moving = true;
            while (!e.peopleInside.isEmpty()) {
                // handle if other people call elevator

                e.deliver();

                // while there are people who need to get off at current floor
                while (e.peopleInside.get(0).getFloorTo() == e.currentFloor) {
                    // add 2 seconds for every person leaving
                    for (int j = 0; j < e.peopleInside.size(); j++) 
                        e.peopleInside.get(j).addTotalTime(e.timeOff);
                    // remove from elevator
                    e.peopleInside.remove(0);
                }
            }
        }
    }

    // closest elevator picks person(s) up
    public static Elevator dispatch(Person p) {
        Elevator closestElevator = elevators[0];
        int minDistance = 5;

        for (int i = 0; i < 4; i++) {
            // if an elevator is closer to the person's floor than any other elevator
            if (Math.abs(elevators[i].currentFloor - p.getFloorFrom()) < minDistance) {
                // if the elevator is not moving in the wrong direction
                if (!(elevators[i].moving && elevators[i].direction != p.getDirection())) {
                    closestElevator = elevators[i];
                    minDistance = Math.abs(elevators[i].currentFloor - p.getFloorFrom());
                }
            }
        }

        return closestElevator;
    }

    public static ArrayList<Person> getPeopleOnFloor (int i) {
        if (i == 0) return peopleF0;
        else if (i == 1) return peopleF1;
        else if (i == 1) return peopleF2;
        else if (i == 1) return peopleF3;
        else if (i == 1) return peopleF4;
        else return peopleF5;
    }
}


// if the elevator is not moving OR (it is moving BUT (in the right direction and is at least 1 floor behind) OR (the person's floor is its last))