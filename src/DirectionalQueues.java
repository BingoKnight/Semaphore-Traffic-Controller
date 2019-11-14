import java.util.LinkedList;

/*
    The Directional Queue is the queue for each cardinal direction in respect to the intersection.
    Car objects populate these queues based on the direction then came from and are removed from
    the queue once they initiate crossing the intersection
 */

public class DirectionalQueues {
    public static LinkedList<Car> north = new LinkedList<>();
    public static LinkedList<Car> east = new LinkedList<>();
    public static LinkedList<Car> west = new LinkedList<>();
    public static LinkedList<Car> south = new LinkedList<>();

    // adds car to appropriate queue based on its original direction
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

    // sets the priority of a car object based on it's position in the queue
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

    // removes car object from appropriate queue
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
