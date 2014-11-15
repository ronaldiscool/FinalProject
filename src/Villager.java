// TODO figure out whether the other classes have to extend from villager or not
class Villager extends Player {
	public Villager(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	public String getRole() {
		return "Villager";
	}
	
}
