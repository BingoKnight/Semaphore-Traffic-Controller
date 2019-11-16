import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/*
    This object references a car and its attempts to cross an intersection. Each car is considered
    a thread to the intersection attempting to access semaphores based on their availability and the
    cars priority.
    A cars priority is determined by it's position in line from a given direction in respect to the intersection
    and by it's arrival time.
    Each car uses a wait timer which references the time it must wait before attempting its next action.
 */

public class Car {

    public static LinkedList<Car> retryQueue = new LinkedList<>();

    private int cid, arrival_time;
    private char dir_original, dir_target;
    private int waitTimer;
    private int status;
    private Character[] directions = {'^', '>', 'V', '<'};
    private int iteration;
    private int subIndex = 0;
    private int[] semaphoresUsed = {-1, -1, -1};
    private int priority = 0;
    private boolean hasWaited = false;

    public Car(int cid, int arrival_time, char dir_original, char dir_target) {
        this.cid = cid;
        this.arrival_time = arrival_time;
        this.dir_original = dir_original;
        this.dir_target = dir_target;
        this.status = -1;
    }

    // runs one turn of the car
    public void run(int iteration, List<Car> intersectionQueue) {

        // sets turn for output
        setIteration(iteration);

        priority = DirectionalQueues.getPriority(this);

        switch (status) {
            case 0: // ready
                ArriveIntersection(intersectionQueue);
                break;
            case 1: // waiting to cross
                waitTimer--;
                if (waitTimer == 0) {
                    status = 2;
                } else {
                    break;
                }
            case 2: // attempting to cross
                CrossIntersection(intersectionQueue);
                break;
            case 3: // crossing
                waitTimer--;
                if (waitTimer != 0) {
                    break;
                }
            case 4: // exiting
                status = 4;
                ExitIntersection();
                break;
            default:
                break;
        }
    }

    // performs action based on car arriving at an intersection
    public void ArriveIntersection(List<Car> intersectionQueue) {
        waitTimer = 2;
        status = 1;
        subIndex = arrival_time;

        DirectionalQueues.add(this);

        System.out.println("Time  " + iteration + "." + subIndex + ": Car " + cid + "(" + dir_original + " " + dir_target + ") arriving");
        intersectionQueue.add(this);
    }

    // Attempts to either turn left, right, or drive through by trying to access the semaphores.
    // if not all semaphores are available that the car needs to perform its required action it will
    // release any that it did obtain.
    public void CrossIntersection(List<Car> intersectionQueue) { // acquire semaphores on turns
        int pre = Arrays.asList(directions).indexOf(dir_original);
        int post = Arrays.asList(directions).indexOf(dir_target);

        if (pre == 3 && post == 0) {
            TurnRight(pre, intersectionQueue);
        } else if (pre == 0 && post == 3) {
            TurnLeft(pre, intersectionQueue);
        } else {
            int action = post - pre;
            if (action == 1)
                TurnRight(pre, intersectionQueue);
            else if (action == 0)
                DriveThrough(pre, intersectionQueue);
            else if (action == -1)
                TurnLeft(pre, intersectionQueue);
        }

        if (waitTimer > 0) {
            status = 3;
            if(hasWaited)
                this.subIndex = Main.subIndex;
            System.out.println("Time  " + iteration + "." + subIndex + ": Car " + cid + "(" + dir_original + " " + dir_target + ")          crossing");
            for(Car car : intersectionQueue){
                if (this.isEqual(car)) {
                    intersectionQueue.remove(car);
                    DirectionalQueues.remove(car);
                    break;
                }
            }
        } else if(!retryQueue.contains(this)) {
            retryQueue.add(this);
            hasWaited = true;
            this.subIndex = Main.subIndex;
        }
    }

    public void ExitIntersection() {

        Main.subIndex = subIndex;

        for(int i = 0; i < semaphoresUsed.length; i++){
            if(semaphoresUsed[i] != -1) {
                Semaphore.semaphores.get(semaphoresUsed[i]).release(this);
                semaphoresUsed[i] = -1;
            }
        }

        System.out.println("Time  " + iteration + "." + subIndex + ": Car " + cid + "(" + dir_original + " " + dir_target + ")                   exiting");

        status = 5;
}

public void DriveThrough(int pre, List<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
          waitTimer = 4;

        switch (pre) {
            case 0:
                semaphoresUsed[0] = 3;
                semaphoresUsed[1] = 0;
                break;
            case 1:
                semaphoresUsed[0] = 2;
                semaphoresUsed[1] = 3;
                break;
            case 2:
                semaphoresUsed[0] = 1;
                semaphoresUsed[1] = 2;
                break;
            case 3:
                semaphoresUsed[0] = 0;
                semaphoresUsed[1] = 1;
                break;
            default:
                break;
        }

        Semaphore.semaphores.get(semaphoresUsed[0]).acquire(this, intersectionQueue);
        Semaphore.semaphores.get(semaphoresUsed[1]).acquire(this, intersectionQueue);
        isSemaphoreOpen = Semaphore.semaphores.get(semaphoresUsed[0]).isAvailable(this, intersectionQueue) && Semaphore.semaphores.get(semaphoresUsed[1]).isAvailable(this, intersectionQueue);

        if (!isSemaphoreOpen)  {
            waitTimer = 0;
            if(isSemaphoreOwned(semaphoresUsed[0])) {
                Semaphore.semaphores.get(semaphoresUsed[0]).release(this);
                semaphoresUsed[0] = -1;
            }if(isSemaphoreOwned(semaphoresUsed[1])) {
                Semaphore.semaphores.get(semaphoresUsed[1]).release(this);
                semaphoresUsed[1] = -1;
            }
        }
    }

    public void TurnLeft(int pre, List<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
        waitTimer = 5;

        switch (pre) {
            case 0:
                semaphoresUsed[0] = 3;
                semaphoresUsed[1] = 0;
                semaphoresUsed[2] = 1;
                break;
            case 1:
                semaphoresUsed[0] = 2;
                semaphoresUsed[1] = 3;
                semaphoresUsed[2] = 0;
                break;
            case 2:
                semaphoresUsed[0] = 1;
                semaphoresUsed[1] = 2;
                semaphoresUsed[2] = 3;
                break;
            case 3:
                semaphoresUsed[0] = 0;
                semaphoresUsed[1] = 1;
                semaphoresUsed[2] = 2;
                break;
            default:
                break;
        }

        Semaphore.semaphores.get(semaphoresUsed[0]).acquire(this, intersectionQueue);
        Semaphore.semaphores.get(semaphoresUsed[1]).acquire(this, intersectionQueue);
        Semaphore.semaphores.get(semaphoresUsed[2]).acquire(this, intersectionQueue);
        isSemaphoreOpen = Semaphore.semaphores.get(semaphoresUsed[0]).isAvailable(this, intersectionQueue)
                && Semaphore.semaphores.get(semaphoresUsed[1]).isAvailable(this, intersectionQueue)
                && Semaphore.semaphores.get(semaphoresUsed[2]).isAvailable(this, intersectionQueue);

        if (!isSemaphoreOpen) {
            waitTimer = 0;
            if(isSemaphoreOwned(semaphoresUsed[0])) {
                Semaphore.semaphores.get(semaphoresUsed[0]).release(this);
                semaphoresUsed[0] = -1;
            }if(isSemaphoreOwned(semaphoresUsed[1])) {
                Semaphore.semaphores.get(semaphoresUsed[1]).release(this);
                semaphoresUsed[1] = -1;
            }if(isSemaphoreOwned(semaphoresUsed[2])) {
                Semaphore.semaphores.get(semaphoresUsed[2]).release(this);
                semaphoresUsed[2] = -1;
            }
        }
    }

    public void TurnRight(int pre, List<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
        waitTimer = 3;

        switch (pre) {
            case 0:
                semaphoresUsed[0] = 3;
                break;
            case 1:
                semaphoresUsed[0] = 2;
                break;
            case 2:
                semaphoresUsed[0] = 1;
                break;
            case 3:
                semaphoresUsed[0] = 0;
                break;
            default:
                break;
        }

        Semaphore.semaphores.get(semaphoresUsed[0]).acquire(this, intersectionQueue);
        isSemaphoreOpen = Semaphore.semaphores.get(semaphoresUsed[0]).isAvailable(this, intersectionQueue);

        if(!isSemaphoreOpen)
            waitTimer = 0;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getArrival_time() {
        return arrival_time;
    }

    public boolean isActive() {
        return !(status == 5 || status == -1);
    }

    public int getCid(){ return cid; }

    public char getDir_original(){
        return this.dir_original;
    }

    public int getWaitTimer(){
        return this.waitTimer;
    }

    public boolean isEqual(Car car){
        return isNotNull(car) && isNotNull(this) && this.getCid() == car.getCid();
    }

    private boolean isNotNull(Car car){
        return car != null;
    }

    private void setIteration(int i) {
        this.iteration = i;
    }

    private boolean isSemaphoreOwned(int index){
        return Semaphore.semaphores.get(index).getActiveThread() != null && this.getCid() == Semaphore.semaphores.get(index).getActiveThread().getCid();
    }

    public int getPriority(){
        return this.priority;
    }
}
