public class BankSession {
	private double balance;

	public BankSession(double openingBalance) {
		this.balance = openingBalance;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		if (amount > 0) balance += amount;
	}

	public void withdraw(double amount) {
		if (amount > 0) balance = Math.max(0.0, balance - amount);
	}
}


