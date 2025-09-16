public interface AttackStrategy {
	int attackDamage();

	default String getName() {
		return getClass().getSimpleName();
	}
}


