import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

class Reader extends Thread
{
	GameClient gc;
	BufferedReader br;
	UserMessenger um;
	
	public Reader(GameClient gc, UserMessenger um)
	{
		super();
		this.gc=gc;
		this.br=gc.br;
		this.um = um;
	}
	
	private void parsecommand(String line, BufferedReader br)
	{
		System.out.println("LINE:"+line);
		
		if (line.startsWith("~STATS~")) {
			System.out.println("printing stats");
			gc.convertToStatsArray(line);
			return;
		}
		
		if(line.equals("~~GAME OVER~~"))
		{
			um.addMessage("Game Over");
			um.sendButton.setEnabled(false);
			um.voteButton.setEnabled(false);
			try {
				String winner = br.readLine();
				um.addMessage(winner+" WON");
				gc.getUserStats();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			return;
		}
		if(line.equals("~~REPORT~~"))
		{
			String line0;
			try {
				line0 = br.readLine();
				um.addMessage(line0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if(line.equals("~~SAVED~~"))
		{
			try {
				String saved= br.readLine();
				um.reset("Nobody");
				um.addMessage(saved+" was attacked by the mafia but saved by the doctor");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		if(line.equals("~~DIE~~"))
		{
			um.addMessage("You died.");
			um.sendButton.setEnabled(false);
			um.voteButton.setEnabled(false);
			gc.getUserStats();
			return;
		}
		if(line.equals("~~KILLED~~"))
		{	
			boolean daytime = true;
			try {
				if(um.timeCycle.getText().substring(0,3).equals("Day"))
					daytime=false;
				String nextLine = br.readLine();
				if(nextLine.equals("~~~~~"))
					um.reset("Nobody");
				else
				{
					System.out.println(nextLine);
					gc.names0.remove(nextLine);
					String role = br.readLine();
					um.reset(nextLine+", the "+ role+",");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				if(!daytime&&gc.role.equalsIgnoreCase("villager"))
				{
					//System.out.println(gc.role+gc.name);
					um.voteButton.setEnabled(false);
					um.sendButton.setEnabled(false);
				}
				else
					um.voteButton.setEnabled(true);
			return;}
		StringTokenizer st = new StringTokenizer(line, "~", false);
		String name = st.nextToken();
				
		
		// read command
		String command = st.nextToken();
		// commands at end of this function
		
		String content = st.nextToken();
		if (command.equalsIgnoreCase("chat")) {
			gc.getMessenger().addMessage(name+": "+content);

		}
		else if (command.equalsIgnoreCase("vote")) {
			
			gc.getMessenger().updateVotes(name,content);
		}
		else if (command.equalsIgnoreCase("power")) {
			gc.getMessenger().updateVotes(name,content);

		}
		
		
	}
	
	public void run()
	{
		try {
			while(true)
			{
				String temp = br.readLine();
				
				System.out.println("TEMP: " + temp);

				if(temp.equals("DONE"))
				{
					if(!gc.name.equals("HOST")){
					try {
						this.sleep(1000);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
					gc.CL.show(gc.jp,"User Messenger");
					gc.names0 = new HashSet<String>(Arrays.asList(gc.concatNames.split("~")));
					//gc.names0.add("HOST");
					um.updateLyncher();
					break;
				}	
				else
					gc.concatNames = temp;


				String names[] = gc.concatNames.split("~");
				for(int i=0; i<names.length; ++i)
					gc.addName(names[i]);
				

				//gc.waitRoom.removeAll();

			}
			
			//String dbCommand = br.readLine();
			//parseDBCommand(dbCommand);
			
			String role = br.readLine();
			
			/*
			while (role.startsWith("~DB~")) {
				role = br.readLine();
			}
			*/
			
			gc.role=role;
			gc.setTitle(gc.name+"-"+gc.role+"-DAY");
			
			while(true) // Receives the message from other players
			{
				String temp = br.readLine();
				if(temp==null)
					return;
				parsecommand(temp,br);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();	//testing
			System.out.println("The server has been disconnected.");
			gc.showExitMessage();
			
			
				
			
			
			
			//System.exit(0); // or exit the game for them, but change the above message first
		}
	}
}

public class GameClient extends JFrame implements Runnable{

	public static HashSet<String> names0;
	UserMessenger um = new UserMessenger(this);
	BufferedReader br;
	PrintWriter pw;
	// panel with cardlayout
	JPanel jp;
	// initial user name chooser
	JPanel su;
	PlayerPanel waitRoom = new PlayerPanel();
	public Thread t = new Thread(this);
	JTextField nameField = new JTextField(45);
	String concatNames = "";
	CardLayout CL = new CardLayout();
	String name="HOST";
	String role;
	Socket s;
	String ip;
	int port;
	boolean isHost;
	
	public void addName(String name) {
		waitRoom.addName(name);
	}
	public UserMessenger getMessenger() {
		return um;
	}
	
	public void sendMessage(String message, int votechoice) {
		String[] votechoices={"CHAT","VOTE", "POWER"};
			String pisstemp = null;
		if(um.timeCycle.getText().substring(0,3).equals("Day"))
			pisstemp = name + "~ALL~"+votechoices[votechoice]+"~" + message;
		if(um.timeCycle.getText().substring(0,3).equals("Nig"))
		{
			pisstemp = name + "~"+role+"~"+votechoices[votechoice]+"~" + message;
			if(votechoice==1)
				pisstemp = name + "~"+role+"~POWER~" + message;
		}
		System.out.println("pisstempgonnapiss"+pisstemp);
		pw.println(pisstemp);
		pw.flush();
	}
	
	private void GUIInit()
	{
		if(isHost){
			try{
				this.s = new Socket("localhost", 6789);
				this.pw = new PrintWriter(s.getOutputStream());
				this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		else{
			try {			
				JTextField ipField = new JTextField(30);
			    JTextField portField = new JTextField(30);
	
			      JPanel myPanel = new JPanel();
			      myPanel.add(new JLabel("IP Address:"));
			      myPanel.add(ipField);
			      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			      myPanel.add(new JLabel("Port:"));
			      myPanel.add(portField);
			      ipField.setText("localhost");
			      portField.setText("6789");
	
			      int result = JOptionPane.showConfirmDialog(null, myPanel, 
			               "Please Enter IP Address and Port Number", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			         ip=ipField.getText();
			         port=Integer.parseInt(portField.getText());
			      }
			      
			      else{
			    	  System.exit(0);
			      }
			      
				this.s = new Socket(ip, port);
				this.pw = new PrintWriter(s.getOutputStream());
				this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("No server was found.");
				showErrorDialog("No server was found.\nPlease check that a server is running and the IP address is correct.");
				System.exit(0);
			} 
		}

		setSize(640,525);
		setLocation(100,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp = new JPanel();
		jp.setLayout(CL);
		
		// pressing Enter key while focus is in input box will submit
		InputMap input = nameField.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		input.put(enter, "text-submit");

		ActionMap actions = nameField.getActionMap();
		actions.put("text-submit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String temp = nameField.getText();
				if (!temp.equals("")) {
					name = temp;
					pw.println(name);
					pw.flush();
					CardLayout CL1 = (CardLayout) jp.getLayout();
					CL1.show(jp,"Wait Room");
				}
			}
		});

		JButton ok = new JButton("OKAY");
		ok.addActionListener(new ActionListener() // ok button leads to the wait room
		{
			public void actionPerformed(ActionEvent ae)
			{
				name = nameField.getText();
				boolean nameDoesNotExist = true;
				
				// NOTE: this does not yet check against list of names as they aren't visible yet
				// TODO: fix this, add to actions' actionPerformed when completed
				for (Player p : GameServer.players) {
					if (p.getName().equalsIgnoreCase(name)) {
						nameDoesNotExist = false;
					}
				}
				if (nameDoesNotExist) {
					pw.println(name);
					pw.flush();
					CardLayout CL1 = (CardLayout) jp.getLayout();
					CL1.show(jp,"Wait Room");
				}
				else {
					// TODO display message to try a different name
				}
			}
		});
		
		JPanel centerPanel = new JPanel();
		JPanel northPanel = new JPanel();
		
		northPanel.setBackground(Color.white);
		
		northPanel.add(new JLabel("Enter a username to be used in the game.") ); // adds instruction
		centerPanel.add(nameField);
		centerPanel.add(ok);

		su = new JPanel();
		su.setLayout(new BorderLayout());
		su.add(northPanel, BorderLayout.NORTH);
		su.add(centerPanel, BorderLayout.CENTER);
		
		
		jp.add(su,"login");
		jp.add(waitRoom, "Wait Room");
		jp.add(um,"User Messenger");
		CL.show(jp,"login");
		add(jp);

		setVisible(true);	
	
	}
	
	
	public GameClient(boolean isHost)
	{
		super("Mafia");
		this.isHost=isHost;
		GUIInit();
	}	


	public void run()
	{
		Reader r = new Reader(this,um);
		r.start();
	}
	
	// sends command to server to get player stats
	public void getUserStats() {
		pw.println("~" + name + "~NULL~GETSTATS~" + name + "~");
		pw.flush();
	}
	
	// this converts a stats message line to an array to be used to display user stats
	public void convertToStatsArray(String statsLine) {
		StringTokenizer st = new StringTokenizer(statsLine, "~", false);
		String[] stats = new String[9];
		st.nextToken();	//skip STATS token
		for (int i = 0; i < 9; i++) {
			stats[i] = st.nextToken();
		}
		showUserStats(stats);
	}
	
	public void showUserStats(String[] stats) {
		JOptionPane.showMessageDialog(
				GameClient.this,
				"Here are your current stats:\n"
				+ "\nTotal times played: " + stats[0] 
				+ "\n               Total wins: " + stats[1] 
				+ "\n           Total losses: " + stats[2] + "\n"
				+ "\n      Times as Mafia: " + stats[3] 
				+ "\n        Wins as Mafia: " + stats[4] 
				+ "\n    Losses as Mafia: " + stats[5] + "\n"
				+ "\n  Times as Villager: " + stats[6] 
				+ "\n    Wins as Villager: " + stats[7] 
				+ "\nLosses as Villager: " + stats[8] + "\n\n"
				,
				"Your Stats",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	public void showWarningDialog(String message) {
		JOptionPane.showMessageDialog(
				GameClient.this,
				message,
				"Warning",
				JOptionPane.WARNING_MESSAGE
		);
	}
	
	public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(
				GameClient.this,
				message,
				"Error",
				JOptionPane.ERROR_MESSAGE
		);
	}
	
	public void showExitMessage() {
		Object [] options = {"Exit", "Dismiss"};
		int selection = JOptionPane.showOptionDialog(
				GameClient.this,
				"The server has been disconnected.\n"
					+ "You should probably exit the game unless you like looking at a nonfunctional window.", 
				"Warning",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null,
				options,
				options[0]
		);
		if (selection == JOptionPane.OK_OPTION) {
	         System.exit(0);
		}
	}
	
	public static void main(String[] args)
	{
		Thread gc = new Thread(new GameClient(false));
		
		gc.start();
	}
}
