package cs.ua.edu.flavortown;
public class RestaurantInfo {

    private double latitude;
    private double longitude;
    private String restName;
    private String dbKey;

    private Menu menu;
    private String googleID;

    //Added these in for pin info - Ian
    private String address;
    private String hours;

    public RestaurantInfo() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public double getLatitude() {
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
    public String getDbKey() { return  dbKey;}

    //added functions for new variables as well as setters for lat, lon , and name. -Ian
    public String getAddress(){ return address;}
    public String getHours(){return hours;}

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
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

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public void setMenu(Menu menu) {this.menu = menu;}

    public void setDbKey(String dbKey) {this.dbKey = dbKey;}

}