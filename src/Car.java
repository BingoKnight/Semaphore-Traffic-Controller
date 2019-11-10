import java.util.Arrays;

// TODO: implement acquiring and releasing semaphores
// TODO: implement case where semaphore requested is in use
// TODO: allow for mutexes
// TODO: correct wait timer location in CrossIntersection()

public class Car {
    private int cid, arrival_time;
    private char dir_original, dir_target;
    private int waitTimer;
    private int status;
    private Character[] directions = new Character[]{'^', '>', 'V', '<'};
    private int iteration;

    public Car(int cid, int arrival_time, char dir_original, char dir_target) {
        this.cid = cid;
        this.arrival_time = arrival_time;
        this.dir_original = dir_original;
        this.dir_target = dir_target;
        this.status = -1;
    }

    public void run(int iteration) {

        setIteration(iteration);

        switch(status){
            case 0: // ready
                ArriveIntersection();
                break;
            case 1: // waiting to cross
                waitTimer--;
                if(waitTimer == 0){
                    status = 2;
                } else { break; }
            case 2: // attempting to cross
                CrossIntersection();
                break;
            case 3: // crossing
                waitTimer--;
                if(waitTimer != 0){
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

    public void ArriveIntersection(){
        waitTimer = 2;
        status = 1;
        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ") arriving");
    }

    public void CrossIntersection(){ // acquire semaphores on turns
        int pre = Arrays.asList(directions).indexOf(dir_original);
        int post = Arrays.asList(directions).indexOf(dir_target);
//        System.out.println("Pre: " + pre + "; Post: " + post);

        if(pre == 3 && post == 0){
            TurnRight(pre);
        } else if(pre == 0 && post == 3) {
            TurnLeft(pre);
        } else {
            int action = post - pre;
            if(action == 1)
                TurnRight(pre);
            else if(action == 0)
                DriveThrough(pre);
            else if(action == -1)
                TurnLeft(pre);
        }

        status = 3;

        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ")          crossing");
    }

    public void ExitIntersection(){
        System.out.println("Time  " + iteration + "." + cid + ": Car " + cid + "(" + dir_original + " " + dir_target + ")                   exiting");
        status = 5;
    }

    public void DriveThrough(int pre){
        switch (pre) {
            case 0:
                Semaphore.get(3).acquire(this);
                System.out.println(Semaphore.get(3).isActive(this));
                Semaphore.get(0);
                break;
            case 1:
                Semaphore.get(2);
                Semaphore.get(3);
                break;
            case 2:
                Semaphore.get(1);
                Semaphore.get(2);
                break;
            case 3:
                Semaphore.get(0);
                Semaphore.get(1);
                break;
            default:
                break;
        }
        waitTimer = 4;
    }

    public void TurnLeft(int pre){
        switch(pre){
            case 0:
                Semaphore.get(3);
                Semaphore.get(0);
                Semaphore.get(1);
                break;
            case 1:
                Semaphore.get(2);
                Semaphore.get(3);
                Semaphore.get(0);
                break;
            case 2:
                Semaphore.get(1);
                Semaphore.get(2);
                Semaphore.get(3);
                break;
            case 3:
                Semaphore.get(0);
                Semaphore.get(1);
                Semaphore.get(2);
                break;
            default:
                break;
        }
        waitTimer= 5;
    }

    public void TurnRight(int pre){
        switch(pre){
            case 0:
                Semaphore.get(3);
                break;
            case 1:
                Semaphore.get(2);
                break;
            case 2:
                Semaphore.get(1);
                break;
            case 3:
                Semaphore.get(0);
                break;
            default:
                break;
        }
        waitTimer= 3;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getArrival_time(){
        return arrival_time;
    }

    public boolean isActive(){
        return !(status == 5 || status == -1);
    }

    private void setIteration(int i){
        this.iteration = i;
    }
}
