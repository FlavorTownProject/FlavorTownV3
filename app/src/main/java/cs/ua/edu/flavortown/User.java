package cs.ua.edu.flavortown;

public class User {
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRatingWeight(float ratingWeight) {
        this.ratingWeight = ratingWeight;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String email;
    String password;
    float ratingWeight;



    String userName;

    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public User(String email, String password, float ratingWeight, String userName) {
        this.email = email;
        this.password = password;
        this.ratingWeight = ratingWeight;
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public float getRatingWeight() {
        return ratingWeight;
    }
    public String getUserName() {return userName;}
}