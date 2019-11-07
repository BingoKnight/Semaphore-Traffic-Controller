import java.util.concurrent.Semaphore;

public class Semaphores {
    private Semaphore sem00, sem01, sem10, sem11;

    public Semaphores(){
        sem00 = new Semaphore(1, true);
        sem01 = new Semaphore(1, true);
        sem10 = new Semaphore(1, true);
        sem11 = new Semaphore(1, true);
    }
}
