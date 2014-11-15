
class Mafia extends TheMafia {
	
	public Mafia(String name) {
		super(name);
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
	
	public String getRole() {
		return "Mafia";
	}
	
}