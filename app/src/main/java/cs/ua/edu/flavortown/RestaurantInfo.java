package cs.ua.edu.flavortown;
public class RestaurantInfo {

    int latitude;
    int longitude;
    String restName;
    Menu menu;

    //Added these in for pin info - Ian
    String address;
    String hours;

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

    //added functions for new variables as well as setters for lat, lon , and name. -Ian
    public String getAddress(){ return address;}
    public String getHours(){return hours;}

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }


}