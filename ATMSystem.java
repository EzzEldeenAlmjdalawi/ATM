
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;
public class ATMSystem {
    private List<UserAccount> userAccounts;

    public ATMSystem() throws IOException {
        this.userAccounts = FileHandler.loadUserAccounts();
    }

    public UserAccount login(String accountNumber, String password) {
        for (UserAccount user : userAccounts) {
            if (user.getAccountNumber().equals(accountNumber) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public UserAccount createNewAccount(String fullName, String phoneNumber, String password, double initialBalance) throws IOException {
        String accountNumber = generateAccountNumber();
        UserAccount newUser = new UserAccount(accountNumber, fullName, phoneNumber, password, initialBalance);
        userAccounts.add(newUser);
        FileHandler.saveUserAccounts(userAccounts);
        return newUser;
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%07d", rand.nextInt(10000000));
        } while (findUserByAccountNumber(accountNumber) != null);
        return accountNumber;
    }

    public UserAccount findUserByAccountNumber(String accountNumber) {
        for (UserAccount user : userAccounts) {
            if (user.getAccountNumber().equals(accountNumber)) {
                return user;
            }
        }
        return null;
    }

    public void performTransaction(UserAccount user, String type, double amount) throws IOException {
        switch (type) {
            case "Deposit":
                user.deposit(amount);
                break;
            case "Withdraw":
                if (!user.withdraw(amount)) {
                    System.out.println("Insufficient balance.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid transaction type.");
                return;
        }
        FileHandler.saveUserAccounts(userAccounts);
        String transaction = user.getAccountNumber() + "," + type + "," + amount + "," + new Date();
        FileHandler.logTransaction(transaction);
    }

    public void start(){
        while (true) {
            System.out.println("Welcome to the ATM System");
            System.out.println("1. Login\n2. Create New Account\n3. Exit");
            System.out.print("Choose an option: ");
            Scanner s = new Scanner(System.in);
            int choice = s.nextInt();
            s.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter Account Number: ");
                String accountNumber = s.nextLine();
                System.out.print("Enter Password: ");
                String password = s.nextLine();

                UserAccount user = login(accountNumber, password);
                if (user != null) {
                    System.out.println("Login Successful!");
                    while (true) {
                        System.out.println("=====================================");
                        System.out.println("1. Deposit\n2. Withdraw\n3. Check Balance\n4. View Transactions History\n5. Change Password\n6. Logout");
                        System.out.println("=====================================");
                        System.out.print("Choose an option: ");
                         int ch = s.nextInt();
                        s.nextLine();

                        if (ch == 1) {
                            System.out.print("Enter amount to deposit: ");
                            double amount = s.nextDouble();
                            s.nextLine();
                            try {
                                performTransaction(user, "Deposit", amount);
                            } catch (IOException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else if (ch == 2) {
                            System.out.print("Enter amount to withdraw: ");
                            double amount = s.nextDouble();
                            s.nextLine();
                            try {
                                performTransaction(user, "Withdraw", amount);
                            } catch (IOException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else if (ch == 3) {
                            System.out.println("Current Balance: " + user.getBalance());
                        } else if (ch == 4) {
                            try {
                                viewTransactionHistory(user.getAccountNumber());
                            } catch (IOException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else if (ch == 5) {
                            System.out.print("Enter new password: ");
                            String newPassword = s.nextLine();
                            user.setPassword(newPassword);
                            try {
                                FileHandler.saveUserAccounts(userAccounts);
                            } catch (IOException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            System.out.println("Password changed successfully.");
                        } else if (ch== 6) {
                            break;
                        } else {
                            System.out.println("Invalid option.");
                        }
                    }
                } else {
                    System.out.println("Invalid account number or password.");
                }
            } else if (choice == 2) {
                System.out.println("=====================================");
                System.out.print("Enter Full Name: ");
                String fullName = s.nextLine();
                System.out.print("Enter Phone Number: ");
                String phoneNumber = s.nextLine();
                System.out.print("Set Password: ");
                String password = s.nextLine();
                System.out.print("Enter Initial Balance (0 if none): ");
                double initialBalance = s.nextDouble();
                s.nextLine();
                System.out.println("=====================================");

                UserAccount newUser = null;
                try {
                    newUser = createNewAccount(fullName, phoneNumber, password, initialBalance);
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                System.out.println("Account Created! Your Account Number: " + newUser.getAccountNumber());
            } else if (choice == 3) {
                System.out.println("Thank you for using the ATM System.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    public void viewTransactionHistory(String accountNumber) throws IOException {
        File file = new File("transactions.txt");
        if (!file.exists()) {
            System.out.println("No transaction history available.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("Transaction History for Account " + accountNumber + ":");
            boolean hasTransactions = false;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountNumber)) {
                    System.out.println(data[1] + ": $" + data[2] + " on " + data[3]);
                    hasTransactions = true;
                }
            }
            if (!hasTransactions) {
                System.out.println("No transactions found for this account.");
            }
        }
    }




}
