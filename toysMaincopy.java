import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class toysMaincopy {
    public static void main(String[] args) {
        Toys toyOne = new Toys("Bear", 2);
        Toys toyTwo = new Toys("Doll", 3);
        Toys toyThree = new Toys("Car", 2);
        Toys toyFour = new Toys("Constructor", 3);

        List<Toys> toys = new ArrayList<>(); // первоначальный список игрушек
        toys.add(toyOne);
        toys.add(toyTwo);
        toys.add(toyThree);
        toys.add(toyFour);

        System.out.println("Список имеющихся игрушек для розыгрыша: ");
        showToys(toys);
        menuToy(toys);
    }

    static void showToys(List<Toys> toys) {
        for (Toys thing : toys) {
            System.out.println(thing);
        }
    }

    static void menuToy(List<Toys> toys) {
        Scanner inputToy = new Scanner(System.in);
        Boolean menu = true;
        String choice;

        String name = "";
        int weight = 0;

        while (menu) {

            System.out.println("\nВведите число, соответствующее пункту меню:");
            System.out.println("1. Добавить новую игрушку для розыгрыша");
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
                    if (toys.size() == 0) System.out.println("\nИгрушки закончились :(\n");
                    else {
                        System.out.println("\nСписок игрушек: ");
                        showToys(toys);
                    }
                    break;

                case "3": // Разыграть игрушку
                    Random random = new Random();

                    // сортировка списка игрушек по весу по убыванию
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

                    // выбор 2/3 отсортированного списка, чтобы точно попали игрушки с наибольшим весом, 
                    // но была вероятность выигрыша игрушким с меньшим весом, 
                    // при этом список игрушек с меньшим весом был все таки ограничен
                    List<Toys> toysCopy = new ArrayList<>();
                    for (int i = 0; i < copyToys.size() / 3 * 2; i++) {
                        toysCopy.add(copyToys.get(i));
                    }

                    Toys prize; 
                    if (toysCopy.size() == 1) prize = toysCopy.get(0);
                    if (toysCopy.size() == 0) {
                        System.out.println("\nИгрушки закончились :(\n");
                        toys.clear();
                    }
                    else {
                        prize = toysCopy.get(random.nextInt(toysCopy.size())); // выбор приза случайным образом 
                        System.out.println("\nВыпала игрушка:");
                        System.out.println(prize);

                        try (FileWriter fw = new FileWriter("prizelist.txt", true)) {
                            fw.write(prize.toString());
                            fw.write("\n");
                            fw.close();
                        } catch (Exception ex) {
                            System.out.println("Что-то пошло не так");
                        }
                    
                        // уменьшение веса на 1 или удаление из списка, если вес уже = 1. 
                        // Здесь вес выступает также в роли количества - чем больше вес, тем больше игрушек с таким названием участвует в розыгрыше, значит больше вероятность ее выигрыша
                        for (Toys thing : toys) {
                            if (thing.containsID(prize.getID())) {
                                int w = thing.getWeightToy();
                                w--;
                                thing.setWeight(w);
                                if (thing.getWeightToy() == 0) toys.remove(thing);
                                break;
                            }
                        }

                        System.out.println("\nОстались игрушки: ");
                        showToys(toys);
                    }

                    break;
                
                default:
                    menu = false;
                    inputToy.close();
                    break;
            }
        }
    }
}
