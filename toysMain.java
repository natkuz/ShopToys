import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class toysMain {
    public static void main(String[] args) {
        
        Toys toyOne = new Toys("Bear", 2);
        Toys toyTwo = new Toys("Doll", 3);
        Toys toyThree = new Toys("Car", 2);
        Toys toyFour = new Toys("Constructor", 3);

        List<Toys> toys = new ArrayList<>();
        toys.add(toyOne);
        toys.add(toyTwo);
        toys.add(toyThree);
        toys.add(toyFour);

        System.out.println(toys);

        // whatToy(toys);

    }

    static void showToys(List<Toys> toys) {
        for (Toys thing : toys) {
            System.out.println(thing);
        }
    }


    static void whatToy(List<Toys> toys) {
        Scanner inputToy = new Scanner(System.in);
        Boolean menu = true;
        String choice;

        String name = "";
        int weight = 0;

        while (menu) {

            System.out.println("Введите число, соответствующее пункту меню:");
            System.out.println("1. Добавить новую игрушку на розыгрыш");
            System.out.println("2. Вывести список оставшихся игрушек");
            System.out.println("3. Разыграть игрушку");
            System.out.println("Для выхода - любой другой символ");

            choice = inputToy.nextLine();

            switch (choice) {
                
                case "1": // Добавить новую игрушку на розыгрыш
                    System.out.print("Введите наименование новой игрушки: ");
                    name = inputToy.nextLine();
                    System.out.println("Введите 'вес' (вероятность выпадения) игрушки: " );
                    weight = inputToy.nextInt();
                    inputToy.nextLine();
                    Toys newToy = new Toys(name, weight);
                    toys.add(newToy);
                    
                    System.out.println("\nТеперь список игрушек стал таким: ");
                    showToys(toys);
                    break;

                case "2": // Вывести список оставшихся игрушек
                    System.out.println("\nСписок игрушек: ");
                    showToys(toys);
                    break;

                case "3": // Разыграть игрушку
                    Random random = new Random();
                    // отсортировать по весу по убыванию, разделить список пополам, рандом в первой половине, вес уменьшается на 1
                    List<Toys> copyToys = new ArrayList<>(toys);
                    Toys temp;
                    Toys tempNext;
                    for (int i = 0; i < copyToys.size(); i++) {
                        for (int j = 1; j < copyToys.size(); j++) {
                            if (copyToys.get(j).getWeights(copyToys) > copyToys.get(j - 1).getWeights(copyToys)) {
                                temp = copyToys.get(j);
                                tempNext = copyToys.get(j - 1);
                                copyToys.set(j, tempNext);
                                copyToys.set(j - 1, temp);
                            }
                        }
                    }
                    
                    List<Toys> copycopyToys = new ArrayList<>();
                    for (int i = 0; i < copyToys.size() / 2; i++) {
                        copycopyToys.add(copyToys.get(i));
                    }
                    
                    Toys prize = copycopyToys.get(random.nextInt(copycopyToys.size()));
                    System.out.println("Выпала игрушка:");
                    System.out.println(prize);

                    for (Toys thing : toys) {
                        if (thing.containsID(prize.getID())) {
                            thing.getWeightToy();
                            if (thing.getWeightToy() == 0) {
                                toys.remove(thing);
                            }
                        }
                    }

                    try (FileWriter fw = new FileWriter("prizelist.txt", false)) {
                        fw.write(prize.toString());
                        fw.close();
                    } catch (Exception ex) {
                        System.out.println("Что-то пошло не так");
                    }

                    System.out.println("\nОстались игрушки: ");
                    showToys(toys);
                    break;

                default:
                    menu = false;
                    inputToy.close();
                    break;
            }
        }
    }   
}
    
