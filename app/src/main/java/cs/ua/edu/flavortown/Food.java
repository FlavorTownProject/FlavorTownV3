package cs.ua.edu.flavortown;

public class Food {

    private String flag;
    private String foodItem;
    private float currRating;
    private int numOfRating;
    private float ratings[];
    private String iterTag;
    private String restTag;
    private String restaurant;
    private String restID;

    @SuppressWarnings("unused")
    public Food() {}

    public Food(String flag, String foodItem, float currRating, int numOfRating, float ratings[]) {
        this.flag = flag;
        this.foodItem = foodItem;
        this.currRating = currRating;
        this.numOfRating = numOfRating;
        this.ratings = ratings;
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
    public float[] getRatings(){return ratings;}
    public String getIterTag() {return iterTag;}
    public String getRestTag() {return restTag;}
    public String getRestaurant(){return restaurant;}
    public String getRestID(){return restID;}

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public void setNumOfRating(int numOfRating) {
        this.numOfRating = numOfRating;
    }

    public void setCurrRating(float currRating) {
        this.currRating = currRating;
    }

    public void setRatings(float[] ratings) {
        this.ratings = ratings;
    }

    public void setIterTag(String iterTag){ this.iterTag = iterTag;}

    public void setRestTag(String restTag){ this.restTag = restTag;}

    public void setRestaurant(String restaurant){ this.restaurant = restaurant;}

    public void setRestID(String restID){this.restID = restID;}
    public boolean calcCurrRating()//set as boolean for debugging and error checking | true = success ; false = failure
    {
        if( numOfRating > 0){
            float total = 0;
            for(int x = 0; x < numOfRating; x++)
                total += ratings[x];
            currRating = total/numOfRating;
            return  true;
        }
        else
            return false;
    }
    public void copyFood(Food copyItem)
    {
        this.flag = copyItem.getFlag();
        this.foodItem = copyItem.getFoodItem();
        this.numOfRating = copyItem.getNumOfRating();
        this.ratings = copyItem.getRatings();
        this.currRating = copyItem.getCurrRating();
        this.iterTag = copyItem.getIterTag();
    }
}