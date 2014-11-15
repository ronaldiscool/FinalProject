
public class Doctor extends Villager {
	
	public Doctor(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	// the player this person picks will be saved for one night
	public void power(Player p) {
		// check if power has been blocked, don't need to inform player
		if(!getPowerBlocked()){
			p.setStayAlive(true);
		}
	}
	
	public String getRole() {
		return "Doctor";
	}
}
