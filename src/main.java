import org.w3c.dom.ls.LSOutput;

import java.util.*;

// TODO: cars running out of order
// TODO: cars utilizing all semaphores when crossing
// TODO: implement mutexes

public class main {

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

        carList.sort(Comparator.comparingInt(Car::getArrival_time));

        List<Semaphore> semaphores = List.of( new Semaphore(0),
                new Semaphore(1),
                new Semaphore(2),
                new Semaphore(3)
        );

        /* Direction Queue
            North side queue = 0
            East side queue = 1
            South side queue = 2
            West side queue  = 3
         */

        ArrayList<Queue<Car>> directionQueue = new ArrayList<>();


        for(int i = 1; (hasActiveCars(carList) || i == 1) && i < 999; i++){
            activateCars(carList, i);

            for (Car car : carList) {
                if(car.isActive())
                    car.run(i, semaphores);
            }

//            System.out.println("hasActiveCars: " + hasActiveCars(carList));
//            System.out.println();

//            System.out.println("Car " + ((semaphores.get(0).getActiveThread() != null) ? semaphores.get(0).getActiveThread().getCid() : "null") + " has sem 0");
//            semaphores.get(0).getAccessQueue().stream().forEach(car -> System.out.print("Car: " + car.getCid() + ", "));
//            System.out.println();
//            System.out.println("Car " + ((semaphores.get(1).getActiveThread() != null) ? semaphores.get(1).getActiveThread().getCid() : "null") + " has sem 1");
//            semaphores.get(1).getAccessQueue().stream().forEach(car -> System.out.print("Car: " + car.getCid() + ", "));
//            System.out.println();
//            System.out.println("Car " + ((semaphores.get(2).getActiveThread() != null) ? semaphores.get(2).getActiveThread().getCid() : "null") + " has sem 2");
//            semaphores.get(2).getAccessQueue().stream().forEach(car -> System.out.print("Car: " + car.getCid() + ", "));
//            System.out.println();
//            System.out.println("Car " + ((semaphores.get(3).getActiveThread() != null) ? semaphores.get(3).getActiveThread().getCid() : "null") + " has sem 3");
//            semaphores.get(3).getAccessQueue().stream().forEach(car -> System.out.print("Car: " + car.getCid() + ", "));
//            System.out.println();
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
