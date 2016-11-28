package cs.ua.edu.flavortown;

import android.app.Notification;


public class Chat {
    public String message;
    public String author;
    public String flag;

    @SuppressWarnings("unused")
    public Chat() {}

    Chat(String message, String author, String flag) {
        this.message = message;
        this.author = author;
        this.flag = flag;
    }

    public String getMessage() { return message; }
    public String getAuthor() {
        return author;
    }
    public String getFlag() { return flag; }
}
