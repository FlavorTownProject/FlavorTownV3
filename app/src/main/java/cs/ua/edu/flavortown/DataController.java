package cs.ua.edu.flavortown;

public class DataController {
    // Singleton Information
    private static final DataController dataSingleton = new DataController();
    public static DataController getInstance(){return dataSingleton;}

    String dataTest = "Hi";

    public String getDataTest() {return dataTest;}
    public void setDataTest(String data) {this.dataTest = dataTest;}

    /* Restaurant Information to be Gathered */

    /* Food Information to be Gathered */

}
