public class BankOperationsDemo {
	public static void main(String[] args) {
		BankSession alice = new BankSession(500.0);
		BankSession bob = new BankSession(200.0);

		BankOperation[] ops = new BankOperation[] {
			new DepositOperation(),
			new WithdrawOperation(),
			new TransferOperation(bob)
		};

		System.out.println("Alice opening balance: " + alice.getBalance());
		ops[0].apply(alice, 150.0);
		System.out.println("After deposit: " + alice.getBalance());
		ops[1].apply(alice, 100.0);
		System.out.println("After withdraw: " + alice.getBalance());
		ops[2].apply(alice, 200.0);
		System.out.println("After transfer to Bob: Alice=" + alice.getBalance() + ", Bob=" + bob.getBalance());
	}
}


