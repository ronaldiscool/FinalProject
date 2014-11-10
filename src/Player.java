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
	
	private boolean alive;
	public int 	tally;
	protected String name;
	// doctor has chosen this player to stay alive, negating any attempts to kill
	private boolean stayAlive;
	// roleblocker has blocked this person from using powers
	private boolean powerBlocked;
	public Player(String name) {
		super();
		alive=true;
		this.name = name;
		stayAlive=false;
		powerBlocked=false;
	}
	
	public void vote(Player p) {
		p.tally++;
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
