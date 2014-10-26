
abstract class TheMafia extends Player {
	
	public TheMafia(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	abstract void power(Player p);
}
