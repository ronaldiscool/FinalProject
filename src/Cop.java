
class Cop extends Player{

	public Cop(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	public void power(Player p) {
		if(!getPowerBlocked()){
			// must tell cop that his power has been blocked
		}
		
		else{
			// otherwise, get the role of the investigated player
			p.getRole();
			// print this out only to his screen or some other indication
		}
	}
	
	public String getRole() {
		return "Detective";
	}
}
