import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JLabel;

class ServerRepainter extends Thread
{
	GameServer gc;
	public ServerRepainter(GameServer gc)
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

class ServerReader extends Thread
{
	BufferedReader br;
	public ServerReader(BufferedReader br)
	{
		super();
		this.br=br;
	}
	
	public void run()
	{
		try {

				String line = br.readLine();
				GameServer.concatNames+=line+"$";
				String[] namel = GameServer.concatNames.split("$");
				for(String n:namel)
				{
					JLabel jl = new JLabel(n);
					GameServer.setup.playerPanel.add(jl);
				}
			GameServer.flag = false;
System.out.println(GameServer.concatNames+"READDING");
GameServer.sendMessage(GameServer.concatNames,true);
				GameServer.read.signalAll();
		}			
			
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class GameServer extends JFrame implements Runnable{
	public static Vector<Player> players= new Vector<Player>();
	public static Vector<GameClient> clients= new Vector<GameClient>();
	public static Vector<ServerThread> st = new Vector<ServerThread>();
	static SetUp setup = new SetUp();
	public static ServerSocket ss;
	static String inBuffer = "";
	static boolean flag = false;
	static String concatNames = "";
	static ReentrantLock lock = new ReentrantLock();
	static Condition read = lock.newCondition();
	
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
	
	public static void sendMessage(String line, boolean send)
	{
		if(send)
		{
		for(ServerThread ct1 : st)
		{
				ct1.send(line);
		}
		}
		else
			inBuffer= line;
		flag = true;
	}	
	
	
	public static void startup()
	{
		for(int i = 0; i <setup.numPlayers-1; i++)
		{
			try{
			//	lock.lock();
			//	read.await();
			Socket s = ss.accept();
			//lock.unlock();
			ServerThread ST = new ServerThread(s);
			st.add(ST);
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ServerReader sr = new ServerReader(br);
			sr.start();
			}
				/*String line = br.readLine();
				concatNames+=line+"\n";
				sendMessage(concatNames,true);
				String[] namel = concatNames.split("\n");
				for(String n:namel)
				{
					JLabel jl = new JLabel(n);
					GameServer.setup.playerPanel.add(jl);
					setup.revalidate();
					setup.repaint();
				}

			flag = false;
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
			}*/
			catch(Exception e){e.printStackTrace();}
		}
		lock.lock();
		try {
			read.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.unlock();
		sendMessage(concatNames,true);
		System.out.println(concatNames+"AAAAAAAA");
		sendMessage("DONEe",true);
		for(ServerThread ST:st)
			ST.start();
		sendMessage("DONE",true);
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
		
				Thread gs =new Thread(new GameServer());	
				gs.start();
	}

	public void run()
	{
		ServerRepainter rp = new ServerRepainter(this);
		rp.start();

	}
}