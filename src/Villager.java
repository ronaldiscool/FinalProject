
class Villager extends Player {
	
	public boolean 	alive;
	public String 	name;
	
	
	public Villager(String name) {
		super(name);
	}
	
	public void vote(Player p) {
		p.tally++;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "Villager";
	}
	
}
