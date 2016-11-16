public class Food {
    String[] flag;
    String foodItem;
    int[] rating;
    float currRating;
    int numOfRating;

    public Food() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String[] getFlag() {
        return flag;
    }
    public String getFoodItem() {
        return foodItem;
    }
    public int[] getRating() {
        return rating;
    }
    public float getCurrRating(){
        return currRating;
    }
    public int getNumOfRating(){
        return numOfRating;
    }
}