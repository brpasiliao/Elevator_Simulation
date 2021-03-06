import java.util.ArrayList;

public class Elevator {

    int currentFloor;           // 6 floors, 0-5
    boolean moving;             // whenever picking up people, or have people inside
    boolean direction;          // if moving, which direction, 0 - down, 1 - up
    ArrayList<Person> peopleAffected;
    ArrayList<Person> peopleInside;

    final float timeMove = 6;   // time to get from floor to floor
    final float timeOn = 2;     // time for people to get on the elevator
    final float timeOff = 2;    // time for people to get off the elevator

    int id;
    
    public Elevator(int i) {
        currentFloor = 0; 
        moving = false;
        direction = true; 
        peopleAffected = new ArrayList<Person>();
        peopleInside = new ArrayList<Person>();

        id = i;
    }

    void move() {
        //after debugging the infinite loop, elevator was going to floors > 5 or < 0 . 
        if(currentFloor == 5){ //the only way to go is down from the top-most floor
            direction = false;
        }
        if(currentFloor == 0){ //the only way to go is up from the bottom-most floor
            direction = true;
        } // two if-statements used to prevent infinite loop of passenger waiting for elevator that goes to -/+ infinity
        if (direction) currentFloor++;
        else currentFloor--;

        for (int i = 0; i < peopleAffected.size(); i++) {
            peopleAffected.get(i).nextTime += timeMove;
        }
    }

    void enter() {
        for (int i = 0; i < peopleAffected.size(); i++) {
            peopleAffected.get(i).nextTime += timeOn;
        }
    }

    void leave(Person p) {
        for (int i = 0; i < peopleAffected.size(); i++) {
            peopleAffected.get(i).nextTime += timeOff;
            peopleAffected.remove(p);
        }

        if (peopleInside.isEmpty()) moving = false; 
    }
    
    void setDirection(int floorTo) {
        if (currentFloor < floorTo) direction = true;
        else direction = false;
    }
}
