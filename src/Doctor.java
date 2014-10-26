
public class Doctor extends Villager {

	public Doctor(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	public void power(Player p) {
		p.alive = true;
	}
	
	public String getType() {
		return "Doctor";
	}
}
