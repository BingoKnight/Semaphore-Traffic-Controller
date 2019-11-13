import java.util.LinkedList;
import java.util.Queue;

// TODO: may run into issue where after release next car will acquire and go
//       but cars that already checked will not go

/*
Semaphore traffic chart:

             |
       1     |     0
             |
  -----------+-----------
             |
       2     |     3
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

    private int semaphoreId;
    private int permits = 1;
//    private Queue<Car> accessQueue = new LinkedList<>();
    private Car activeThread;

    public Semaphore(int id){
        this.semaphoreId = id;
    }

    public void acquire(Car car, Queue<Car> intersectionQueue){ // TODO: acquire needs check queue because release does not
        if(this.permits == 1 && car.isEqual(intersectionQueue.peek())){
            this.permits--;
            this.activeThread = car;
//            System.out.println(car.getCid());
//            System.out.println(permits);
        } else if(activeThread != null &&
                car.getDir_original() == activeThread.getDir_original() &&
                car.getWaitTimer() > activeThread.getWaitTimer() &&
                car.isEqual(intersectionQueue.peek())){
//            System.out.println(car.getDir_original() == activeThread.getDir_original());
//            System.out.println(car.getWaitTimer() > activeThread.getWaitTimer());
//            System.out.println(car.isEqual(intersectionQueue.peek()));
            activeThread = car;
        } else {
//            System.out.println("car.getWaitTimer() = " + car.getWaitTimer() + "; activeThread.getWaitTimer() = " + activeThread.getWaitTimer());
        }
    }

    public void release(Car car){
        if(this.permits < 1 && (activeThread == null || activeThread.getCid() == car.getCid())){
//            System.out.println("Car " + car.getCid() + " is now released sem " + semaphoreId);
            this.permits = 1;
            this.activeThread = null;
        }
    }

    public Car getActiveThread(){ return this.activeThread; }

    public boolean isAvailable(Car car){ // permits == 1 will never get hit
        return this.permits == 1 || car == this.activeThread || car.getDir_original() == activeThread.getDir_original();
    }

//    public Queue<Car> getAccessQueue() { return accessQueue; }

}
