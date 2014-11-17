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
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Repainter extends Thread
{
	GameClient gc;
	public Repainter(GameClient gc)
	{
		this.gc = gc;
	}
	
	public void run()
	{
		while(true){
		gc.revalidate();
		gc.repaint();}
	}
}

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
	
	private void parsecommand(String line)
	{
		if(line.equals("OVER"))
		{	
			um.reset();
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
			
		}
	}
	
	public void run()
	{
		try {
			while(true)
			{
				String temp = br.readLine();

				if(temp.equals("DONE"))
				{
					if(!gc.name.equals("HOST")){
					try {
						this.sleep(2000);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
					gc.CL.show(gc.jp,"User Messenger");
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
			
			String role = br.readLine();
			gc.role=role;
			gc.setTitle(gc.name+"-"+gc.role+"-DAY");

			
			while(true) // Receives the message from other players
			{
				String temp = br.readLine();
				parsecommand(temp);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class GameClient extends JFrame implements Runnable{

	public static Vector<String> names0;
	UserMessenger um = new UserMessenger(this);
	BufferedReader br;
	private PrintWriter pw;
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
	
	public void addName(String name) {
		waitRoom.addName(name);
	}
	public UserMessenger getMessenger() {
		return um;
	}
	
	public void sendMessage(String message, int votechoice) {
		String[] votechoices={"CHAT","VOTE"};
		String pisstemp = name + "~ALL~"+votechoices[votechoice]+"~" + message;
		pw.println(pisstemp);
		pw.flush();
	}
	
	private void GUIInit()
	{

		setSize(640,480);
		setLocation(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp = new JPanel();
		jp.setLayout(CL);

		JButton ok = new JButton("OKAY");
		ok.addActionListener(new ActionListener() // ok button leads to the wait room
		{
			public void actionPerformed(ActionEvent ae)
			{
				name = nameField.getText();
				pw.println(name);
				pw.flush();
				CardLayout CL1 = (CardLayout) jp.getLayout();
				CL1.show(jp,"Wait Room");
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
	
	
	public GameClient()
	{
		super("Mafia");
		try {
			Socket s = new Socket("localhost", 6789);
			this.pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		GUIInit();
	}	


	public void run()
	{
		Reader r = new Reader(this,um);
		Repainter rp = new Repainter(this);
		r.start();
		rp.start();		
	}
	
	public static void main(String[] args)
	{
		Thread gc = new Thread(new GameClient());
		
		gc.start();
	}
}
