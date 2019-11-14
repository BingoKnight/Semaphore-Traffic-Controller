import java.util.Queue;

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
    private Car activeThread;

    public Semaphore(int id){
        this.semaphoreId = id;
    }

    public void acquire(Car car, Queue<Car> intersectionQueue){
        if(this.permits == 1 && car.isEqual(intersectionQueue.peek())){
            this.permits--;
            this.activeThread = car;
        } else if(this.permits == 1 && intersectionQueue.peek() != null && Car.isBlocked(intersectionQueue.peek())){
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
                car.isEqual(intersectionQueue.peek())){
            activeThread = car;
        }
    }

    public void release(Car car){
        if(this.permits < 1 && (activeThread == null || activeThread.getCid() == car.getCid())){
            this.permits = 1;
            this.activeThread = null;
        }
    }

    public Car getActiveThread(){ return this.activeThread; }

    public boolean isAvailable(Car car, Queue<Car> intersectionQueue){
        return !Car.isBlocked(car) &&
                ((this.permits == 1 && areCarsAheadBlocked(car, intersectionQueue)) || // running car 7 because its not blocked regardless of if cars ahead are blocked
                    car == this.activeThread ||
                    (activeThread != null &&
                        car.getDir_original() == activeThread.getDir_original() &&
                        car.isEqual(intersectionQueue.peek())));
    }

    private boolean areCarsAheadBlocked(Car car, Queue<Car> intersectionQueue){
        boolean carsAheadBlocked = true;
        for(Car temp : intersectionQueue){
            if(temp.getCid() == car.getCid())
                break;
            if(!Car.isBlocked(temp)) {
                carsAheadBlocked = false;
                break;
            }
        }

        return carsAheadBlocked;
    }
}
