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
	public Reader(GameClient gc)
	{
		super();
		this.gc=gc;
		this.br=gc.br;
	}
	
	public void run()
	{
		try {
			while(true)
			{
				String temp = br.readLine();
				if(temp.equals("DONE"))
					break;
				gc.concatNames = temp;
				String names[] = gc.concatNames.split("$");
				gc.addName(gc.concatNames);
				//System.out.println(temp);

				//gc.waitRoom.removeAll();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class GameClient extends JFrame implements Runnable{
	
	BufferedReader br;
	private PrintWriter pw;
	JPanel jp;
	JPanel su;
	PlayerPanel waitRoom = new PlayerPanel();
	public Thread t = new Thread(this);
	JTextField nameField = new JTextField(45);
	String concatNames = "";
	public void addName(String name) {
		waitRoom.addName(name);
	}
	private void GUIInit()
	{
		setSize(640,480);
		setLocation(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp = new JPanel();
		CardLayout CL = new CardLayout();
		jp.setLayout(CL);
		
		JButton ok = new JButton("OKAY");
		ok.addActionListener(new ActionListener() // ok button leads to the wait room
		{
			public void actionPerformed(ActionEvent ae)
			{
				String name = nameField.getText();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		GUIInit();

	}	


	public void run()
	{
		Reader r = new Reader(this);
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
