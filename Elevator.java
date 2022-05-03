import java.util.ArrayList;

public class Elevator {

    int currentFloor;            // 6 floors, 0-5
    boolean moving;
    boolean direction;         // if moving, which direction, 0 - down, 1 - up
    ArrayList<Person> people;

    final float timeMove = 6;   // time to get from floor to floor
    final float timeOn = 0.5f;     // time for people to get on the elevator
    final float timeOff = 0.5f;    // time for people to get off the elevator
    
    public Elevator() {
        currentFloor = 0; 
        moving = false;
        direction = true; 
        people = new ArrayList<Person>();
    }

    // goes one floor at a time in case people press the button while traveling
    // use for as long as there are people in the elevator
    // control outside: moving, direction, people pick up/drop off handling
    void deliver(boolean d) {
        for (int i = 0; i < people.size(); i++) people.get(i).addTotalTime(6);

        if (d) currentFloor++;
        else currentFloor--;
    }
}
