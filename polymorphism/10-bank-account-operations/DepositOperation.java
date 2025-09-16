public class DepositOperation implements BankOperation {
	@Override
	public void apply(BankSession session, double amount) {
		session.deposit(amount);
	}
}


