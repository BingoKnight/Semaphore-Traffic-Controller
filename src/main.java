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
                        new Car(8, 8, '<', '^'));

        while(true){
            carList.forEach(car -> {

            });
        }

        //implement turns
    }
}
