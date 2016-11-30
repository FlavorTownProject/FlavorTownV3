package cs.ua.edu.flavortown;

public class User {
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