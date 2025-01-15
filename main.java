import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try {
            ATMSystem atmSystem = new ATMSystem();
            atmSystem.start();
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
