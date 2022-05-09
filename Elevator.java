import java.util.ArrayList;
import java.lang.Math;

public class Elevator {

    int currentFloor;           // 6 floors, 0-5
    boolean moving;             // whenever picking up people, or have people inside
    boolean direction;          // if moving, which direction, 0 - down, 1 - up
    ArrayList<Person> peopleAffected;
    ArrayList<Person> peopleInside;

    final float timeMove = 6;   // time to get from floor to floor
    final float timeOn = 2;     // time for people to get on the elevator
    final float timeOff = 2;    // time for people to get off the elevator
    
    public Elevator() {
        currentFloor = 0; 
        moving = false;
        direction = true; 
        peopleAffected = new ArrayList<Person>();
        peopleInside = new ArrayList<Person>();
    }

    void move() {
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
            peopleInside.remove(0);
        }

        if (peopleInside.isEmpty()) moving = false; 
    }

    void setDirection(int floorTo) {
        if (currentFloor < floorTo) direction = true;
        else direction = false;
    }
}
