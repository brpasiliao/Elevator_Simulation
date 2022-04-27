import java.util.Random;

public class Person {
    private int floorFrom;               // start from 0
    private int floorTo;
    // boolean waiting;             // waiting for elevator; can be idle
    private float arrivalTime;

    public Person() {
        // 
        Random random = new Random();
        if (random.nextFloat() < 0.5) floorFrom = 0;
        else floorFrom = random.nextInt(5) + 1;

        if (floorFrom == 0) {
            while (floorTo != floorFrom)
                floorTo = random.nextInt(5) + 1;
        } else {
            if (random.nextFloat() < 0.5) floorTo = 0;
            while (floorTo != floorFrom)
                floorTo = random.nextInt(5) + 1;
        }

        arrivalTime = (int)Math.round((-6 * Math.log(1 - random.nextFloat())));
        // System.out.println("fgsdg");
    }

    public int getFloorFrom() {
        return floorFrom;
    }

    public int getFloorTo() {
        return floorTo;
    }

    public float getArrivalTime() {
        return arrivalTime;
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