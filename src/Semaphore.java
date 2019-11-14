import java.util.List;
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

    public static List<Semaphore> semaphores = List.of( new Semaphore(0),
            new Semaphore(1),
            new Semaphore(2),
            new Semaphore(3)
    );

    private int semaphoreId; // used for debugging purposes
    private int permits = 1;
    private Car activeThread;

    public Semaphore(int id){
        this.semaphoreId = id;
    }

    // locks semaphore for car that requests it if it is available and acts like a mutex where it
    // assigns it to the car that has the largest crossing time but also the next in line
    public void acquire(Car car, List<Car> intersectionQueue){
        if(this.permits == 1 && car.isEqual(intersectionQueue.get(0))){
            this.permits--;
            this.activeThread = car;
        } else if(this.permits == 1 && intersectionQueue.get(0) != null ){
            for(Car temp : intersectionQueue){
                if(car.isEqual(temp)){
                    this.permits--;
                    this.activeThread = car;
                    break;
                }
            }
        } else if(activeThread != null &&
                car.getDir_original() == activeThread.getDir_original() &&
                car.getWaitTimer() > activeThread.getWaitTimer() &&
                car.isEqual(intersectionQueue.get(0))){
            activeThread = car;
        }
    }

    // unlocks the semaphore and for another car to use
    public void release(Car car){
        if(this.permits < 1 && (activeThread == null || activeThread.getCid() == car.getCid())){
            this.permits = 1;
            this.activeThread = null;
        }
    }

    public Car getActiveThread(){ return this.activeThread; }

    public boolean isAvailable(Car car, List<Car> intersectionQueue){
        return  (this.permits == 1  ||
                    car == this.activeThread ||
                    (activeThread != null &&
                        car.getDir_original() == activeThread.getDir_original() &&
                        car.isEqual(intersectionQueue.get(0))));
    }
}
