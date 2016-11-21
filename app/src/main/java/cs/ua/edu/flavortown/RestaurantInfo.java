public class RestaurantInfo {
    int latitude;
    int longitude;
    String restName;
    Menu menu;

    public RestaurantInfo() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public int getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getRestName() {
        return restName;
    }
    public Menu getMenu(){
        return menu;
    }
}