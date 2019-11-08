import java.util.Arrays;

public class Car extends Thread {
    private int cid, arrival_time;
    private char dir_original, dir_target;
    private int waitTimer;
    private int status;
    private String[] statuses = new String[]{"Ready", "Waiting"};
    private char[] directions = new char[]{'^', '>', 'V', '<'};

    public Car(int cid, int arrival_time, char dir_original, char dir_target) {
        this.cid = cid;
        this.arrival_time = arrival_time;
        this.dir_original = dir_original;
        this.dir_target = dir_target;
        this.status = -1;
    }

    @Override
    public void run() {
        super.run();

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
        System.out.println("Arriving");
    }

    public void CrossIntersection(){ // acquire semaphores on turns
        int from = Arrays.asList(directions).indexOf(dir_original);
        int to = Arrays.asList(directions).indexOf(dir_target);

        if(from == 3 && to == 0){
            TurnRight();
        } else if(from == 0 && to == 3) {
            TurnLeft();
        } else {
            int action = to - from;
            if(action == 1)
                TurnRight();
            else if(Math.abs(action) == 2)
                DriveThrough();
            else if(action == -1)
                TurnLeft();
        }

        status = 3;

        System.out.println("Crossing");
    }

    public void ExitIntersection(){
        System.out.println("Exiting");
    }

    public void DriveThrough(){
        waitTimer= 4;
    }

    public void TurnLeft(){
        waitTimer= 5;
    }

    public void TurnRight(){
        waitTimer= 3;
    }

    public int getStatus(){
        return status;
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
}
