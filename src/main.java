import java.util.List;

public class main {

    public static void main(String args[]){

        List<Car> carList = List.of(new Car(1, 1, '^', '^'),
                        new Car(2, 2, '^', '^'),
                        new Car(3, 3, '^', '<'),
                        new Car(4, 4, 'V', 'V'),
                        new Car(5, 5, 'V', '>'),
                        new Car(6, 6, '^', '^'),
                        new Car(7, 7, '>', '^'),
                        new Car(8, 8, '<', '^')
        );

        for(int i = 1; (hasActiveCars(carList) || i == 1) && i < 99; i++){
            activateCars(carList, i);

            for (Car car : carList) {
                if(car.isActive())
                    car.run(i);
            }
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
