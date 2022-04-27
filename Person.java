import java.util.Random;

public class Person {
    int floorFrom;               // start from 0
    int floorTo;
    boolean waiting;             // waiting for elevator; can be idle
    float arrivalTime;

    public Person() {
        // 
        Random random = new Random();
        if (random.nextFloat() < 0.5) floorFrom = 0;
        else floorFrom = random.nextInt(6);

        if (random.nextFloat() < 0.5) floorTo = 0;
        else {
            while (floorTo != floorFrom)
            floorTo = random.nextInt(5) + 1;
        }


    }
    // void call() { // elevator is called
    //     if from floor0: 
    //         10 people per min = 1 person / 6 sec = average 6 sec arrivalTime
    //     else : 
    //         5 people per min = 1 person / 12 sec = average 12 sec arrivalTime
        
    //     arrivalTime
    //     Elevator.fetch(this);
    // }
}
