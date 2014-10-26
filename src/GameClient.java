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



public class GameClient extends JFrame implements Runnable{
	
	private BufferedReader br;
	private PrintWriter pw;
	JPanel jp = new JPanel();
	JPanel su = new JPanel();
	SetUp waitRoom = new SetUp();
	public Thread t = new Thread(this);
	JTextField nameField = new JTextField(45);

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
		waitRoom.relist();
		jp.add(waitRoom, "Wait Room");
		add(jp);
		setVisible(true);
	}
	
	
	public GameClient()
	{
		super("Mafia");
		GUIInit();
		try {
			Socket s  = new Socket("localhost",6789);
			this.pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void run()
	{
		while(true)
		{
			try {
				String line = br.readLine();
				if(line!=null)
				{}
					revalidate();
					repaint();
					break;
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		}
	}
	
	public static void main(String[] args)
	{
		new GameClient();
	}
}
