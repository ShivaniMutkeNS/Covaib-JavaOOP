public class GameCharacterAttackDemo {
	public static void main(String[] args) {
		AttackStrategy[] strategies = new AttackStrategy[] {
			new MeleeAttack(),
			new RangedAttack(),
			new MagicAttack()
		};

		for (AttackStrategy s : strategies) {
			System.out.println(s.getName() + " deals " + s.attackDamage() + " damage");
		}
	}
}


