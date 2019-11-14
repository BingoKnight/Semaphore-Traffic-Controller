import java.util.*;

/* TODO: proper priority not applied, 5 arrives behind 4 and sits behind 4 while 6, 7, and 8 arrive

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

        List<Semaphore> semaphores = List.of( new Semaphore(0),
                new Semaphore(1),
                new Semaphore(2),
                new Semaphore(3)
        );

        Queue<Car> intersectionQueue = new LinkedList<>();

        carList.sort(Comparator.comparingInt(Car::getArrival_time));

        for(int i = 1; (hasActiveCars(carList) || i == 1) && i < 999; i++){
            activateCars(carList, i);

            for (Car car : carList) {
                if(car.isActive())
                    car.run(i, semaphores, intersectionQueue);
            }
            Car.ResetBlocked();
        }

    }

    public static boolean hasActiveCars(List<Car> carList){
        return carList.stream().anyMatch(car -> car.isActive() );
    }

    public static void activateCars(List<Car> carList, int index){
        carList.forEach(car -> {
            if(index == car.getArrival_time())
                car.setStatus(0);
        });
    }
}
