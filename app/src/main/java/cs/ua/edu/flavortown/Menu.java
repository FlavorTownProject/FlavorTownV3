package cs.ua.edu.flavortown;
public class Menu {
    Food[] foodList;

    public Menu() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public Food[] getFoodList() {
        return foodList;
    }

}