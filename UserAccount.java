
public class UserAccount {
    private String accountNumber;
    private String fullName;
    private String phoneNumber;
    private String password;
    private double balance;

    public UserAccount(String accountNumber, String fullName, String phoneNumber, String password, double balance) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public String toString() {
        return accountNumber + "," + fullName + "," + phoneNumber + "," + password + "," + balance;
    }

}
