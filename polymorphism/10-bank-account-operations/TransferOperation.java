public class TransferOperation implements BankOperation {
	private final BankSession toAccount;

	public TransferOperation(BankSession toAccount) {
		this.toAccount = toAccount;
	}

	@Override
	public void apply(BankSession fromAccount, double amount) {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
	}
}


