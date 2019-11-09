import java.util.LinkedList;
import java.util.Queue;

/*
Semaphore traffic chart:

             |
       2     |     1
             |
  -----------+-----------
             |
       3     |     4
             |


The above chart represents the four quadrants in the intersection
Examples of quandrants used:
    Right Turn from bottom:
        A right turn from the bottom of the graph would only use quandrant 4
    Left Turn from right:
        A left turn from the right side of the chart would use quadrants 1, 2, and 3
    Drive Through from top:
        Driving directly through the intersection from the top would use quandrants 2 and 3
*/
public class Semaphore {

    private static int permits = 1;
    private static Queue<Car> accessQueue = new LinkedList<>();
    private static Car activeThread;

    private static Semaphore[] sem = new Semaphore[4];

    private Semaphore(){}

    public static Semaphore get(int index){
        try {
//            System.out.println(index);
            if(index >= sem.length)
                throw new ArrayIndexOutOfBoundsException();

            if(sem[index] == null)
                sem[index] = new Semaphore();
            return sem[index];

        } catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void acquire(Car car){
        if(permits == 1){
            permits--;
            activeThread = car;
        } else if(!accessQueue.contains(car)) {
            accessQueue.add(car);
        }
    }

    public static void release(Car car){
        if(permits < 1 && accessQueue.size() > 0){
            activeThread = accessQueue.poll();
        } else if(permits < 1){
            permits = 1;
        }
    }

}
