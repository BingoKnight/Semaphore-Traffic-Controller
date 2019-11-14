import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Car {
    public static boolean isNorthBlocked = false,
                          isSouthBlocked = false,
                          isWestBlocked = false,
                          isEastBlocked = false;

    private int cid, arrival_time;
    private char dir_original, dir_target;
    private int waitTimer;
    private int status;
    private Character[] directions = {'^', '>', 'V', '<'};
    private int iteration;
    private int[] semaphores = {-1, -1, -1};

    public Car(int cid, int arrival_time, char dir_original, char dir_target) {
        this.cid = cid;
        this.arrival_time = arrival_time;
        this.dir_original = dir_original;
        this.dir_target = dir_target;
        this.status = -1;
    }

    public void run(int iteration, List<Semaphore> quadrants, Queue<Car> intersectionQueue) {

        setIteration(iteration);

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
                if(!isBlocked(this))
                    CrossIntersection(quadrants, intersectionQueue);
                break;
            case 3: // crossing
                waitTimer--;
                if (waitTimer != 0) {
                    break;
                }
            case 4: // exiting
                status = 4;
                ExitIntersection(quadrants);
                break;
            default:
                break;
        }
    }

    public void ArriveIntersection(Queue<Car> intersectionQueue) {
        waitTimer = 2;
        status = 1;

        DirectionalQueues.add(this);

        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ") arriving");
//        intersectionQueue.add(this);
    }

    public void CrossIntersection(List<Semaphore> quadrants, Queue<Car> intersectionQueue) { // acquire semaphores on turns
        int pre = Arrays.asList(directions).indexOf(dir_original);
        int post = Arrays.asList(directions).indexOf(dir_target);

        if (pre == 3 && post == 0) {
            TurnRight(pre, quadrants, intersectionQueue);
        } else if (pre == 0 && post == 3) {
            TurnLeft(pre, quadrants, intersectionQueue);
        } else {
            int action = post - pre;
            if (action == 1)
                TurnRight(pre, quadrants, intersectionQueue);
            else if (action == 0)
                DriveThrough(pre, quadrants, intersectionQueue);
            else if (action == -1)
                TurnLeft(pre, quadrants, intersectionQueue);
        }

        if (waitTimer > 0) {
            status = 3;
            System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ")          crossing");
            for(Car car : intersectionQueue){
                if (this.isEqual(car)) {
                    intersectionQueue.remove(car);
                    break;
                }
            }
            setBlocked();
        }
    }

    public void ExitIntersection(List<Semaphore> quadrants) {

        for(int i = 0; i < semaphores.length; i++){
            if(semaphores[i] != -1) {
                quadrants.get(semaphores[i]).release(this);
                semaphores[i] = -1;
            }
        }

        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ")                   exiting");

        status = 5;
    }

public void DriveThrough(int pre, List<Semaphore> quadrants, Queue<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
          waitTimer = 4;

        switch (pre) {
            case 0:
                semaphores[0] = 3;
                semaphores[1] = 0;
                break;
            case 1:
                semaphores[0] = 2;
                semaphores[1] = 3;
                break;
            case 2:
                semaphores[0] = 1;
                semaphores[1] = 2;
                break;
            case 3:
                semaphores[0] = 0;
                semaphores[1] = 1;
                break;
            default:
                break;
        }

        quadrants.get(semaphores[0]).acquire(this, intersectionQueue);
        quadrants.get(semaphores[1]).acquire(this, intersectionQueue);
        isSemaphoreOpen = quadrants.get(semaphores[0]).isAvailable(this, intersectionQueue) && quadrants.get(semaphores[1]).isAvailable(this, intersectionQueue);

        if (!isSemaphoreOpen)  {
            waitTimer = 0;
            if(isSemaphoreOwned(semaphores[0], quadrants)) {
                quadrants.get(semaphores[0]).release(this);
                semaphores[0] = -1;
            }if(isSemaphoreOwned(semaphores[1], quadrants)) {
                quadrants.get(semaphores[1]).release(this);
                semaphores[1] = -1;
            }
        }
    }

    public void TurnLeft(int pre, List<Semaphore> quadrants, Queue<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
        waitTimer = 5;

        switch (pre) {
            case 0:
                semaphores[0] = 3;
                semaphores[1] = 0;
                semaphores[2] = 1;
                break;
            case 1:
                semaphores[0] = 2;
                semaphores[1] = 3;
                semaphores[2] = 0;
                break;
            case 2:
                semaphores[0] = 1;
                semaphores[1] = 2;
                semaphores[2] = 3;
                break;
            case 3:
                semaphores[0] = 0;
                semaphores[1] = 1;
                semaphores[2] = 2;
                break;
            default:
                break;
        }

        quadrants.get(semaphores[0]).acquire(this, intersectionQueue);
        quadrants.get(semaphores[1]).acquire(this, intersectionQueue);
        quadrants.get(semaphores[2]).acquire(this, intersectionQueue);
        isSemaphoreOpen = quadrants.get(semaphores[0]).isAvailable(this, intersectionQueue)
                && quadrants.get(semaphores[1]).isAvailable(this, intersectionQueue)
                && quadrants.get(semaphores[2]).isAvailable(this, intersectionQueue);

        if (!isSemaphoreOpen) {
            waitTimer = 0;
            if(isSemaphoreOwned(semaphores[0], quadrants)) {
                quadrants.get(semaphores[0]).release(this);
                semaphores[0] = -1;
            }if(isSemaphoreOwned(semaphores[1], quadrants)) {
                quadrants.get(semaphores[1]).release(this);
                semaphores[1] = -1;
            }if(isSemaphoreOwned(semaphores[2], quadrants)) {
                quadrants.get(semaphores[2]).release(this);
                semaphores[2] = -1;
            }
        }
    }

    public void TurnRight(int pre, List<Semaphore> quadrants, Queue<Car> intersectionQueue) {

        boolean isSemaphoreOpen;
        waitTimer = 3;

        switch (pre) {
            case 0:
                semaphores[0] = 3;
                break;
            case 1:
                semaphores[0] = 2;
                break;
            case 2:
                semaphores[0] = 1;
                break;
            case 3:
                semaphores[0] = 0;
                break;
            default:
                break;
        }

        quadrants.get(semaphores[0]).acquire(this, intersectionQueue);
        isSemaphoreOpen = quadrants.get(semaphores[0]).isAvailable(this, intersectionQueue);

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

    private boolean isSemaphoreOwned(int index, List<Semaphore> quadrants){
        return quadrants.get(index).getActiveThread() != null && this.getCid() == quadrants.get(index).getActiveThread().getCid();
    }

    public static void ResetBlocked(){
        isNorthBlocked = isSouthBlocked =  isEastBlocked = isWestBlocked = false;
    }

    private void setBlocked(){
        if(dir_original == '^')
            isNorthBlocked = true;
        else if(dir_original == '>')
            isEastBlocked = true;
        else if(dir_original == '<')
            isWestBlocked = true;
        else if(dir_original == 'V')
            isSouthBlocked = true;
    }

    public static boolean isBlocked(Car car){
        if(car.getDir_original() == '^')
            return isNorthBlocked;
        else if(car.getDir_original() == '>')
            return isEastBlocked;
        else if(car.getDir_original() == '<')
            return isWestBlocked;
        else if(car.getDir_original() == 'V')
            return isSouthBlocked;
        else
            return false;
    }
}
