import java.util.concurrent.Semaphore;
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
public class Semaphores {
    private static Semaphore sem[];
    private Semaphores(){
        sem = new Semaphore[]{new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true)};
    }

    public static Semaphore get(int index){
        try {
            if(index >= sem.length)
                throw new ArrayIndexOutOfBoundsException();

            if(sem[index] == null)
                sem[index] = new Semaphore(1, true);
            return sem[index];

        } catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
    }

}
