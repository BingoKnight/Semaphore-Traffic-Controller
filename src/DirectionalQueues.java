import java.util.LinkedList;
import java.util.Queue;

public class DirectionalQueues {
    public static LinkedList<Car> north = new LinkedList<>();
    public static LinkedList<Car> east = new LinkedList<>();
    public static LinkedList<Car> west = new LinkedList<>();
    public static LinkedList<Car> south = new LinkedList<>();

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

    public static int getPriority(Car car){
        if(car.getDir_original() == '^')
            for(int i = 0; i < south.size(); i++){
                if(car.isEqual(south.get(i)))
                    return i;
            }
        else if(car.getDir_original() == '<')
            for(int i = 0; i < east.size(); i++){
                if(car.isEqual(east.get(i)))
                    return i;
            }
        else if(car.getDir_original() == '>')
            for(int i = 0; i < west.size(); i++){
                if(car.isEqual(west.get(i)))
                    return i;
            }
        else if(car.getDir_original() == 'V')
            for(int i = 0; i < north.size(); i++){
                if(car.isEqual(north.get(i)))
                    return i;
            }

        return 0;
    }

    public static void remove(Car car){
        if(car.getDir_original() == '^')
            south.remove(car);
        else if(car.getDir_original() == '<')
            east.remove(car);
        else if(car.getDir_original() == '>')
            west.remove(car);
        else if(car.getDir_original() == 'V')
            north.remove(car);
    }
}
