
class Stripper extends TheMafia {

	public Stripper(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	public void power(Player p) {
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "Stripper";
	}
	
}
