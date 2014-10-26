
class GodFather extends TheMafia {
	
	public GodFather(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	public void power(Player p) {
		p.alive = false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRole() {
		return "GodFather";
	}
	
}