package cs.ua.edu.flavortown;

import android.util.Log;

public class Menu {
    public Food[] foodList;
    public int length;

    private int currentIndex;//used for generation in MenuActivity/addToFoodList
    private int highestRatedItem; //index of highest rated item in foodList

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
            Food temp = new Food();
            temp.setCurrRating(foodItem.getCurrRating());
            temp.setFoodItem(foodItem.getFoodItem());
            temp.setNumOfRating(foodItem.getNumOfRating());
            temp.setFlag(foodItem.getFlag());
            temp.setRatings(foodItem.getRatings());
            Log.v("addToFoodList", "currentIndex = "+ currentIndex+ " | length = "+ length);
            Log.v("addToFoodList", "foodList["+currentIndex+"] = " + foodList[currentIndex].getFoodItem()) ;
            Log.v("addToFoodList", "fooditem to be added: "+foodItem.getFoodItem());
            foodList[currentIndex] = temp;
            Log.v("addToFoodList","foodList["+currentIndex+"] is now " + foodList[currentIndex].getFoodItem());
            currentIndex = currentIndex + 1;
            return true;
        }
        else
            return false;
    }
    public Food getHighestRatedItem()
    {
        sortItemsByRating();
        return foodList[0];
        //calcHighestRatedItem();
        //return foodList[highestRatedItem];
    }


    public void calcHighestRatedItem() {

        int currHighestIndex = 0;
        float currHighestRating = 0;
        for(int x = 0; x < foodList.length ; x++)
        {
            Log.v("calcHighest", "food "+ x+" = "+foodList[x].getFoodItem());
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


    public void sortItemsByRating() {
        if(foodList.length > 1) {
            int currCompIndex = 0;
            Food temp = new Food();
            for(currCompIndex = 0; currCompIndex < foodList.length; currCompIndex++) {
                Log.v("Menu","currCompIndex = "+String.valueOf(currCompIndex));
                for (int x = currCompIndex; x < foodList.length; x++) {
                    Log.v("Menu","x = "+String.valueOf(x));
                    if (x != currCompIndex && foodList[x].getCurrRating() > foodList[currCompIndex].getCurrRating()) {
                            Log.v("Menu", "found higher rating");
                            Log.v("Menu", "FoodList[curr] = " + foodList[currCompIndex].getFoodItem());
                            Log.v("Menu", "FoodList[x] = " + foodList[x].getFoodItem());
                            temp.copyFood(foodList[currCompIndex]); //temp = foodList[currCompIndex];
                            foodList[currCompIndex].copyFood(foodList[x]); //foodList[currCompIndex] = foodList[x];
                            foodList[x].copyFood(temp); //foodList[x] = temp;
                        Log.v("Menu", "After switch");
                        Log.v("Menu", "FoodList[curr] = " + foodList[currCompIndex].getFoodItem());
                        Log.v("Menu", "FoodList[x] = " + foodList[x].getFoodItem());
                    }
                    else if (x != currCompIndex && foodList[x].getCurrRating() == foodList[currCompIndex].getCurrRating() && foodList[x].getNumOfRating() > foodList[currCompIndex].getNumOfRating()) {
                        Log.v("Menu","Found equal rating but higher # of ratings");
                        temp.copyFood(foodList[currCompIndex]); //temp = foodList[currCompIndex];
                        foodList[currCompIndex].copyFood(foodList[x]); //foodList[currCompIndex] = foodList[x];
                        foodList[x].copyFood(temp); //foodList[x] = temp;
                    }

                }//end of x loop
            }//end of curCompIndex loop
        }
    }
}