package cs.ua.edu.flavortown;
public class Menu {
    public Food[] foodList;
    public int length;

    private int currentIndex;//used for generation in MenuActivity/addToFoodList
    //private int highestRatedItem; //index of highest rated item in foodList

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
    public Food getHighestRatedItem()
    {
        sortItemsByRating();
        return foodList[0];
        //return foodList[highestRatedItem];
    }

    /*
    public void calcHighestRatedItem() {

        int currHighestIndex = 0;
        float currHighestRating = 0;
        for(int x = 0; x < foodList.length ; x++)
        {
            if(foodList[x].getCurrRating() >= currHighestRating ){
                if(foodList[x].getCurrRating() == currHighestRating && foodList[x].getNumOfRating() > foodList[currHighestIndex].getNumOfRating()){
                    currHighestIndex = x;//A more popular item with the same rating has been discovered. List as top item.
                }
                else{
                    currHighestIndex = x;
                    currHighestRating = foodList[x].getCurrRating();
                }
            }
        }
        highestRatedItem = currHighestIndex;
    }
    */

    public void sortItemsByRating() {
        if(foodList.length > 1) {
            int currCompIndex = 0;
            Food temp;
            for(currCompIndex = 0; currCompIndex < foodList.length; currCompIndex++) {
                for (int x = 0; x < foodList.length; x++) {
                    if (x != currCompIndex && foodList[x].getCurrRating() > foodList[currCompIndex].getCurrRating()) {
                            temp = foodList[currCompIndex];
                            foodList[currCompIndex] = foodList[x];
                            foodList[x] = temp;
                    }
                    else if (x != currCompIndex && foodList[x].getCurrRating() == foodList[currCompIndex].getCurrRating() && foodList[x].getNumOfRating() > foodList[currCompIndex].getNumOfRating()) {
                            temp = foodList[currCompIndex];
                            foodList[currCompIndex] = foodList[x];
                            foodList[x] = temp;
                    }

                }//end of x loop
            }//end of curCompIndex loop
        }
    }
}