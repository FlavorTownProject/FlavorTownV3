package cs.ua.edu.flavortown;

public class Food {
    public String flag;
    public String foodItem;
    public float currRating;
    public int numOfRating;

    @SuppressWarnings("unused")
    public Food() {}

    public Food(String flag, String foodItem, float currRating, int numOfRating) {
        this.flag = flag;
        this.foodItem = foodItem;
        this.currRating = currRating;
        this.numOfRating = numOfRating;
    }

    public String getFlag() {
        return flag;
    }
    public String getFoodItem() {
        return foodItem;
    }
    public float getCurrRating(){
        return currRating;
    }
    public int getNumOfRating(){
        return numOfRating;
    }
}