import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/*

    This program implements a turn based intersection where the intersection is broken into 4 quadrants,
    each quandrant is a semaphore that locks when a car object utilizes that semaphore.
    Cars are run once per turn and if a car does not perform an action that turn after all other cars have
    attempted to perform an action cars that have not done anything will attempt to run again.

 */

public class Main {

    public static void main(String args[]){

        List<Car> carList = Arrays.asList(new Car(1, 1, '^', '^'),
                                    new Car(2, 2, '^', '^'),
                                    new Car(3, 3, '^', '<'),
                                    new Car(4, 4, 'V', 'V'),
                                    new Car(5, 5, 'V', '>'),
                                    new Car(6, 6, '^', '^'),
                                    new Car(7, 7, '>', '^'),
                                    new Car(8, 8, '<', '^')
                            );

        List<Car> intersectionQueue = new LinkedList<>();
        intersectionQueue.sort(Comparator.comparingInt(Car::getArrival_time));

        // implements a turn style system
        for(int i = 1; (hasActiveCars(carList) || i == 1); i++){
            activateCars(carList, i);

            for (Car car : carList) { // runs all active cars once
                if(car.isActive())
                    car.run(i, intersectionQueue);
            }

            for(Car car : Car.retryQueue){ // attempts to re-run cars that were trying to cross but didn't
                if(car.isActive())
                    car.run(i, intersectionQueue);
            }

            // sorts cars based off of their priority in the intersection
            intersectionQueue = (intersectionQueue.stream().sorted(Comparator.comparingInt(Car::getPriority))).collect(Collectors.toList());

            // empty retry queue
            Car.retryQueue.clear();
        }
    }

    // check if list of cars has cars with statuses consisting of 0, 1, 2, 3, or 4
    public static boolean hasActiveCars(List<Car> carList){
        return carList.stream().anyMatch(car -> car.isActive() );
    }

    // changes a cars status from -1 (not ready) to 0 (ready)
    public static void activateCars(List<Car> carList, int index){
        carList.forEach(car -> {
            if(index == car.getArrival_time())
                car.setStatus(0);
        });
    }
}
