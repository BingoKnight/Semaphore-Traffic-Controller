import java.util.LinkedList;
import java.util.Queue;

public class DirectionalQueues {
    public static Queue<Car> north = new LinkedList<>();
    public static Queue<Car> east = new LinkedList<>();
    public static Queue<Car> west = new LinkedList<>();
    public static Queue<Car> south = new LinkedList<>();

    public static void add(Car car){
        if(car.getDir_original() == '^')
            south.add(car);
        else if(car.getDir_original() == '<')
            east.add(car);
        else if(car.getDir_original() == '>')
            west.add(car);
        else if(car.getDir_original() == 'V')
            north.add(car);
    }
}
