import java.util.ArrayList;

public class Elevator {

    int currentFloor;           // 6 floors, 0-5
    boolean moving;
    boolean direction;          // if moving, which direction, 0 - down, 1 - up
    ArrayList<Person> peopleInside;

    final float timeMove = 6;   // time to get from floor to floor
    final float timeOn = 2;     // time for people to get on the elevator
    final float timeOff = 2;    // time for people to get off the elevator
    
    public Elevator() {
        currentFloor = 0; 
        moving = false;
        direction = true; 
        peopleInside = new ArrayList<Person>();
    }

    // goes one floor at a time in case people press the button while traveling
    // use until elevator gets to desired floor
    // control outside: moving, direction, people pick up/drop off handling
    void fetch(ArrayList<Person> peopleWaiting) {
        for (int i = 0; i < peopleWaiting.size(); i++) peopleWaiting.get(i).addTotalTime(6);

        if (direction) currentFloor++;
        else currentFloor--;
    }

    // goes one floor at a time in case people press the button while traveling
    // use for as long as there are people in the elevator
    // control outside: moving, direction, people pick up/drop off handling
    void deliver() {
        for (int i = 0; i < peopleInside.size(); i++) peopleInside.get(i).addTotalTime(6);

        if (direction) currentFloor++;
        else currentFloor--;
    }

    // sets the direction in which the elevator is going
    void setDirection(int floorTo) {
        if (currentFloor < floorTo) direction = true;
        else direction = false;
    }
}
