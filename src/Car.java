import java.util.Arrays;
import java.util.List;
import java.util.Queue;

// TODO: correct wait timer location in CrossIntersection()

public class Car {
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
                ArriveIntersection();
                break;
            case 1: // waiting to cross
                waitTimer--;
                if (waitTimer == 0) {
                    status = 2;
                } else {
                    break;
                }
            case 2: // attempting to cross
                CrossIntersection(quadrants);
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

    public void ArriveIntersection() {
        waitTimer = 2;
        status = 1;
        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ") arriving");
    }

    public void CrossIntersection(List<Semaphore> quandrants) { // acquire semaphores on turns
        int pre = Arrays.asList(directions).indexOf(dir_original);
        int post = Arrays.asList(directions).indexOf(dir_target);
//        System.out.println("Pre: " + pre + "; Post: " + post);

        if (pre == 3 && post == 0) {
            TurnRight(pre, quandrants);
        } else if (pre == 0 && post == 3) {
            TurnLeft(pre, quandrants);
        } else {
            int action = post - pre;
            if (action == 1)
                TurnRight(pre, quandrants);
            else if (action == 0)
                DriveThrough(pre, quandrants);
            else if (action == -1)
                TurnLeft(pre, quandrants);
        }

        if (waitTimer > 0) {
            status = 3;
            System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ")          crossing");
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

    public void DriveThrough(int pre, List<Semaphore> quandrants) {

        boolean isSemaphoreOpen;

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

        quandrants.get(semaphores[0]).acquire(this);
        quandrants.get(semaphores[1]).acquire(this);
        isSemaphoreOpen = quandrants.get(semaphores[0]).isAvailable(this) && quandrants.get(semaphores[1]).isAvailable(this);

        if (isSemaphoreOpen) {
            waitTimer = 4;
        } else {
            if(this.getCid() == quandrants.get(semaphores[0]).getActiveThread().getCid()) {
                quandrants.get(semaphores[0]).release(this);
                semaphores[0] = -1;
            }if(this.getCid() == quandrants.get(semaphores[1]).getActiveThread().getCid()) {
                quandrants.get(semaphores[1]).release(this);
                semaphores[1] = -1;
            }
        }
    }

    public void TurnLeft(int pre, List<Semaphore> quandrants) {

        boolean isSemaphoreOpen;

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

        quandrants.get(semaphores[0]).acquire(this);
        quandrants.get(semaphores[1]).acquire(this);
        quandrants.get(semaphores[2]).acquire(this);
        isSemaphoreOpen = quandrants.get(semaphores[0]).isAvailable(this)
                && quandrants.get(semaphores[1]).isAvailable(this)
                && quandrants.get(semaphores[2]).isAvailable(this);

        if (isSemaphoreOpen){
            waitTimer = 5;
        } else {
            if(this.getCid() == quandrants.get(semaphores[0]).getActiveThread().getCid()) {
                quandrants.get(semaphores[0]).release(this);
                semaphores[0] = -1;
            }if(this.getCid() == quandrants.get(semaphores[1]).getActiveThread().getCid()) {
                quandrants.get(semaphores[1]).release(this);
                semaphores[1] = -1;
            }if(this.getCid() == quandrants.get(semaphores[2]).getActiveThread().getCid()) {
                quandrants.get(semaphores[2]).release(this);
                semaphores[2] = -1;
            }
        }
    }

    public void TurnRight(int pre, List<Semaphore> quandrants) {

        boolean isSemaphoreOpen;

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

        quandrants.get(semaphores[0]).acquire(this);
        isSemaphoreOpen = quandrants.get(semaphores[0]).isAvailable(this);

        if(isSemaphoreOpen)
            waitTimer = 3;
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

    private void setIteration(int i) {
        this.iteration = i;
    }
}
