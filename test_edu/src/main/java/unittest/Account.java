package unittest;

public class Account {
    private int balance;

    public Account() {
    }

    public Account(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(Account other, int amount) {
        if (balance - amount < 0) {
            throw new ValidWithdrawException();
        }

        other.balance += amount;
        balance -= amount;
    }

    private void hello() {

    }
}
