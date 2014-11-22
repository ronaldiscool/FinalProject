import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


abstract class Player{
	
	static int nobodyVote = 0;
	ServerThread st;
	private boolean alive = true;
	public int 	tally;
	protected String name;
	protected ServerReader reader;
	// doctor has chosen this player to stay alive, negating any attempts to kill
	private boolean stayAlive;
	// roleblocker has blocked this person from using powers
	private boolean powerBlocked;
	public Player(String name, ServerThread st, ServerReader reader) {
		super();
		alive=true;
		this.name = name;
		this.st = st;
		this.reader = reader;
		stayAlive=false;
		powerBlocked=false;
	}
	
	public void vote(Player p) {
		try {
			System.out.println("PERMITSSSS00000"+GameServer.allvotesSem.availablePermits());

			GameServer.allvotesSem.acquire();
			if(p==null){nobodyVote++;}
			else
				p.tally++;
			System.out.println("PERMITSSSS"+GameServer.allvotesSem.availablePermits());
			if(GameServer.allvotesSem.availablePermits()!=0)
			{
			GameServer.lock.lock();
			GameServer.allvotes.await();
			GameServer.lock.unlock();
			}
			else
			{
				int maxTally = 0;
				Player mostVoted = null;
				for(Player p0:GameServer.players)
				{		
					if(maxTally == p0.tally && maxTally!=0)
					{
						mostVoted=null;
						p0.tally=0;
						break;
					}
					else if(maxTally<p0.tally)
					{	maxTally=p0.tally;
					mostVoted=p0;}
					p0.tally=0;
				}
				if(maxTally<nobodyVote)
					mostVoted=null;
				if(mostVoted!=null)
				{
					String targetrole = mostVoted.getRole();
					switch(targetrole)
					{
					case "Villager":
						GameServer.villagers.remove(mostVoted);
						break;
					case "Mafia":
						GameServer.mafia.remove(mostVoted);
						break;
					case "Cop":
						GameServer.cops.remove(mostVoted);
						break;
					case "Doctor":
						GameServer.doctors.remove(mostVoted);
						break;
					}
					Vector<Player> killmv = new Vector<Player>();
					killmv.add(mostVoted);
					GameServer.sendMessage("~~DIE~~", killmv);
					GameServer.players.remove(mostVoted);
					for(Player p0 :GameServer.players)
						System.out.println("TEST"+p0.name);
					}
				GameServer.lock.lock();
				GameServer.allvotes.signalAll();
				GameServer.lock.unlock();
				GameServer.sendMessage("~~KILLED~~", null);
				if(mostVoted!=null)
				{
					GameServer.sendMessage(mostVoted.getName(), null);
					GameServer.sendMessage(mostVoted.getRole(),null);
				}
				else
					GameServer.sendMessage("~~~~~", null);
				if(GameServer.players.size()<=(2*GameServer.mafia.size()))
				{
					GameServer.sendMessage("~~GAME OVER~~", null);
					GameServer.sendMessage("MAFIA",null);
					return;
				}
			if(GameServer.mafia.size()==0)
			{
				GameServer.sendMessage("~~GAME OVER~~", null);
				GameServer.sendMessage("VILLAGERS",null);
				return;
			}
			if(null!=mostVoted)
			{
				GameServer.lock.lock();
				GameServer.released.await();
				GameServer.lock.unlock();
				GameServer.allvotesSem.acquire();
				String targetrole = mostVoted.getRole();
				System.out.println("ROLE"+targetrole);
				switch(targetrole)
				{
				case "Mafia":
					GameServer.allmafSem.acquire();
					break;
				//case "Cop":
					//GameServer.cops.remove(mostVoted);
				case "Doctor":
					GameServer.alldocSem.acquire();
					break;
				}
			}
			}                                
			GameServer.lock.lock();
			GameServer.allvotesSem.release();
			GameServer.released.signalAll();
			GameServer.lock.unlock();
			nobodyVote=0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	abstract void power(Player p);

	
	public String getName(){
		return name;
	}
	
	public boolean getAlive(){
		return alive;
	}
	
	public void setAlive(boolean b){
		alive=b;
	}
	
	public boolean getStayAlive(){
		return stayAlive;
	}
	
	public void setStayAlive(boolean b){
		stayAlive=b;
	}
	
	public void setPowerBlocked(boolean b){
		powerBlocked=b;
	}
	
	public boolean getPowerBlocked(){
		return powerBlocked;
	}
	
	abstract String getRole();
}
