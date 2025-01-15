
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class FileHandler {
    private static final String USER_DATA_FILE = "users.txt";
    private static final String TRANSACTION_HISTORY_FILE = "transactions.txt";

    public static List<UserAccount> loadUserAccounts() throws IOException {
        List<UserAccount> userAccounts = new ArrayList<>();
        File file = new File(USER_DATA_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    userAccounts.add(new UserAccount(data[0], data[1], data[2], data[3], Double.parseDouble(data[4])));
                }
            }
        }
        return userAccounts;
    }

    public static void saveUserAccounts(List<UserAccount> userAccounts) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (UserAccount user : userAccounts) {
                bw.write(user.toString());
                bw.newLine();
            }
        }
    }

    public static void logTransaction(String transaction) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTION_HISTORY_FILE, true))) {
            bw.write(transaction);
            bw.newLine();
        }
    }
}
