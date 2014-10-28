import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;

public class GameServer extends JFrame{
	public static Vector<Player> players= new Vector<Player>();
	public static Vector<GameClient> clients= new Vector<GameClient>();
	public static Vector<ServerThread> st = new Vector<ServerThread>();
	private static SetUp setup = new SetUp();
	public static ServerSocket ss;
	String concatNames = "";

	
	public GameServer()
	{
		super("Mafia");
		setSize(500, 500);
		setLocation(50, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(setup);
		setVisible(true);
		
		try {
			ss = new ServerSocket(6789);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String line,ServerThread ct)
	{
		for(ServerThread ct1 : st)
		{
			if(ct==null||!ct.equals(ct1))
				ct1.send(line);
		}
	}	
	
	
	public static void startup()
	{
		for(int i = 0; i <setup.numPlayers-1; i++)
		{
			try{
			Socket s = ss.accept();
			ServerThread ST = new ServerThread(s);
			st.add(ST);
			ST.start();
				//BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				//String name = br.readLine();
				
				//System.out.println(i+name);
				//Player p = new Player(name);
				//players.add(p);
				//concatNames= concatNames+name+"\n";
				//sendMessage(concatNames,null);
				setup.relist();
				setup.revalidate();
				setup.repaint();
			//ServerThread sthread = new ServerThread( s, this);
			//GameClient client = new GameClient();
			//clients.add(client);
			}
			catch(Exception e){e.printStackTrace();}
		}
		System.out.println(GameServer.players.size());
		sendMessage("DONE",null);
		
		/*for(int i = 0; i < setup.numVil; i++)
		{
			Socket s = ss.accept();
			Villager p = new Villager();
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numCop; i++)
		{
			Socket s = ss.accept();
			Cop p = new Cop();
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numDoc; i++)
		{
			Socket s = ss.accept();
			Doctor p = new Doctor();
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numMaf; i++)
		{
			//Socket s = ss.accept();
			TheMafia p = new GodFather("ronaldismaf");
			players.add(p);
			setup.relist();

		}*/

	}
	public static void main(String[] args)
	{
		
				new GameServer();	

	}
}