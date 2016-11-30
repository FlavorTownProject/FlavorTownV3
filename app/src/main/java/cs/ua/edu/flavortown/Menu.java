package cs.ua.edu.flavortown;
public class Menu {
    public Food[] foodList;
    public int length;

    private int currentIndex;

    public Menu() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public Menu(int length)
    {
        currentIndex = 0;
        this.length = length;
        foodList = new Food[length];
        for(int x = 0; x < length ; x++)
            foodList[x] = new Food();
    }
    public Food[] getFoodList() {
        return foodList;
    }
    public boolean addToFoodList(Food foodItem){
        if(currentIndex < length)
        {
            foodList[currentIndex] = foodItem;
            currentIndex++;
            return true;
        }
        else
            return false;
    }

}