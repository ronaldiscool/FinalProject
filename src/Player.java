
abstract class Player {
	
	public boolean 		alive;
	protected String 	name;
	public int 			tally;
	
	
	public Player(String name) {
		this.name = name;
		alive=true;
	}
	
	public abstract String getName();
	public abstract String getType();
	public abstract void vote(Player p);
	
}
