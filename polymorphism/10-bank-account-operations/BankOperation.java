public interface BankOperation {
	void apply(BankSession session, double amount);

	default String getName() {
		return getClass().getSimpleName();
	}
}


