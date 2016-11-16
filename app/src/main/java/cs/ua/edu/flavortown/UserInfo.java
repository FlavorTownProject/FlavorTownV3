public class UserInfo {
    String email;
    String password;
    float ratingWeight;

    public UserInfo() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
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
}