
class Stripper extends TheMafia {

	public Stripper(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	public void power(Player p) {
		if(p.getStayAlive()){
			// tell player they failed in killing the person, or whoever its implemented
		}
		
		else{
			// kill the target
			p.setAlive(false);
		}
	}
	
	// block the power of another player
	public void power2(Player p){
		p.setPowerBlocked(true);
	}
	
	public String getRole() {
		return "Stripper";
	}
	
}
