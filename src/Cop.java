
class Cop extends Villager{
	
	public Cop(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	public void power(Player p) {
		p.alive = false;
	}
	
	public String getRole() {
		return "Detective";
	}
	
	
}
