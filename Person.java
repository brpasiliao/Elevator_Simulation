import java.util.Random;

public class Person implements Comparable<Person> {
    private int floorFrom;          // start from 0
    private int floorTo;
    private boolean direction;      // whch direction to go to, 0 - down, 1 - up

    private float arrivalTime;
    private float totalTime;

    public Person() {
        Random random = new Random();
        if (random.nextFloat() < 0.5) floorFrom = 0;
        else floorFrom = random.nextInt(5) + 1;

        if (floorFrom != 0 && random.nextFloat() < 0.5) floorTo = 0;
        else {
            while (floorTo == floorFrom) floorTo = random.nextInt(5) + 1;
        }

        direction = floorTo - floorFrom > 0;

        arrivalTime = (int)Math.round((-6 * Math.log(1 - random.nextFloat())));
        totalTime = 0;
    }

    public int getFloorFrom() {
        return floorFrom;
    }

    public int getFloorTo() {
        return floorTo;
    }

    public boolean getDirection() {
        return direction;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }

    public void addTotalTime(float t) {
        totalTime += t;
    }

    @Override
    public int compareTo(Person p) {
        return getFloorTo() - p.getFloorTo();
    }
}