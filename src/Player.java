import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


abstract class Player{
	
	ServerThread st;
	private boolean alive;
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
					if(maxTally<p0.tally)
						maxTally=p0.tally;
					mostVoted=p0;
				}
				System.out.println("FFFFF"+mostVoted.name);
				GameServer.lock.lock();
				GameServer.allvotes.signalAll();
				GameServer.lock.unlock();
				GameServer.sendMessage("OVER", null);
			}
			GameServer.allvotesSem.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
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
