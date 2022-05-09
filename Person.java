import java.util.Random;
import java.util.Comparator;

public class Person {
    private int floorFrom;                  // start from 0
    private int floorTo;
    private boolean direction;              // which direction to go to, 0 - down, 1 - up
    String status;                          // waiting, entering, riding, leaving
    Elevator elevator;

    private static float systemTime = 0;    // sum of arrival times
    float arrivalTime;                      // time person arrives after previous
    float nextTime;                         // system time when the person's next event will happen

    public Person() {
        Random random = new Random();
        if (random.nextFloat() < 0.5) floorFrom = 0;
        else floorFrom = random.nextInt(5) + 1;

        if (floorFrom != 0 && random.nextFloat() < 0.5) floorTo = 0;
        else {
            while (floorTo == floorFrom) floorTo = random.nextInt(5) + 1;
        }

        direction = floorTo - floorFrom > 0;
        status = "waiting";
        elevator = null;

        arrivalTime = (int)Math.round((-6 * Math.log(1 - random.nextFloat())));
        systemTime += arrivalTime;
        nextTime = systemTime;
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

    // Comparator for sorting people by time
    public static Comparator<Person> TimeComparator = new Comparator<Person>() {
        public int compare(Person a, Person b) {
            return Float.compare(a.nextTime, b.nextTime);
        }
    };
    
    // Comparator for sorting people by floor ascending
    public static Comparator<Person> FloorUpComparator = new Comparator<Person>() {
        public int compare(Person a, Person b) {
            return Integer.compare(a.getFloorTo(), b.getFloorTo());
       }
    };

    // Comparator for sorting people by floor descending
    public static Comparator<Person> FloorDownComparator = new Comparator<Person>() {
        public int compare(Person a, Person b) {
            return Integer.compare(b.getFloorTo(), a.getFloorTo());
       }
    };
}