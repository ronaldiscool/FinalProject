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


class Player{
	
	public boolean 		alive;
	public int 			tally;
	protected String name;
	
	public Player(String name) {
		super();
		alive=true;
		this.name = name;
	}
	
	
	public String getName(){return name;};
	public String getRole(){return null;};
	
}
