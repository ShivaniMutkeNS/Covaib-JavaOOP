public class WithdrawOperation implements BankOperation {
	@Override
	public void apply(BankSession session, double amount) {
		session.withdraw(amount);
	}
}


