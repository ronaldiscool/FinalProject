import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
				gc.concatNames = br.readLine();
				System.out.println(gc.concatNames);
				if(gc.concatNames.equals("DONE"))
					break;
			String[] namel = gc.concatNames.split("\n");
				//gc.waitRoom.removeAll();
				for(String n:namel)
				{
					JLabel jl = new JLabel(n);
					gc.waitRoom.add(jl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class GameClient extends JFrame implements Runnable{
	
	BufferedReader br;
	private PrintWriter pw;
	JPanel jp = new JPanel();
	JPanel su = new JPanel();
	PlayerPanel waitRoom = new PlayerPanel();
	public Thread t = new Thread(this);
	JTextField nameField = new JTextField(45);
	String concatNames = "LINE";

	private void GUIInit()
	{
		setSize(640,480);
		setLocation(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CardLayout CL = new CardLayout();
		jp.setLayout(CL);
		JButton ok = new JButton("OKAY");
		ok.addActionListener(new ActionListener()
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
		su.add(nameField);
		su.add(ok);
		jp.add(su,"login");
		jp.add(waitRoom, "Wait Room");
		add(jp);
		setVisible(true);
	}
	
	
	public GameClient()
	{
		super("Mafia");
		try {
			Socket s  = new Socket("localhost",6789);
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
		Thread gc =new Thread(new GameClient());
		gc.start();
		
	}
}
