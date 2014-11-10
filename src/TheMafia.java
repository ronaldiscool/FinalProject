
abstract class TheMafia extends Player {
	// TODO indicate that because a person inherits from this class, they can see 
	// the other mafia members
	public TheMafia(String name) {
		super(name);
	}
	
	abstract String getRole();
}
