import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
			if(!gc.show)
			{
				gc.setVisible(false);
				break;
			}
			gc.revalidate();
			gc.repaint();}
	}
}

class ServerReader extends Thread
{
	BufferedReader br;
	Socket s;
	PrintWriter pw;
	public ServerReader(BufferedReader br, Socket s)
	{
		super();
		this.br=br;
		this.s = s;
	}

	public void run()
	{
		try {
			String line = br.readLine();
			GameServer.concatNames+=line+"~";
			GameServer.name1 = GameServer.concatNames.split("~");

			for(String n:GameServer.name1)

			{
				JLabel jl = new JLabel(n);
				GameServer.setup.playerPanel.add(jl);
			}
			GameServer.flag = false;
			GameServer.sendMessage(GameServer.concatNames, null);
			GameServer.lock.lock();
			GameServer.read.signalAll();
			GameServer.lock.unlock();
			try {
				//pw = new PrintWriter(s.getOutputStream());
				//while(true) {
					//line = br.readLine();
					//GameServer.sendMessage(line, true, null);
					//pw.println(line);
				//}

			}
			catch(Exception e) {System.out.println(e.getStackTrace());}

				//GameServer.received.signalAll();
		}			

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}





public class GameServer extends JFrame implements Runnable{
	public static String[] name1;
	public static int pCount = 0;
	public volatile static Vector<String> names = new Vector<String>(); 
	public static Vector<Player> players= new Vector<Player>();
	public static Vector<ServerThread> st = new Vector<ServerThread>();
	public static Vector<Villager> villagers= new Vector<Villager>();
	public static Vector<Cop> cops= new Vector<Cop>();
	public static Vector<TheMafia> mafia= new Vector<TheMafia>();
	public static Vector<Stripper> strippers= new Vector<Stripper>();
	public static Vector<Doctor> doctors= new Vector<Doctor>();
	public static Vector<ServerReader> readers = new Vector<ServerReader>();
	public static CardLayout c1=new CardLayout();
	public UserMessenger serverMessenger=new UserMessenger(this);
	public static JPanel serverPanel=new JPanel();

	static SetUp setup = new SetUp();
	public static ServerSocket ss;
	static String inBuffer = "";
	static boolean flag = false;
	static String concatNames = "";
	static ReentrantLock lock = new ReentrantLock();
	static Condition read = lock.newCondition();
	static Condition received = lock.newCondition();
	static Condition received2 = lock.newCondition();
	static Condition vectorsupdated = lock.newCondition();
	//static GameClient gc;
	static boolean show = true;
	static boolean initializing = true;
	static BufferedReader br0;
	public GameServer()
	{
		super("Mafia");


		// cardlayout for the messenger and the setup panel
		serverPanel.setLayout(c1);
		serverPanel.add(serverMessenger,"message");
		serverPanel.add(setup, "setup");
		c1.show(serverPanel, "setup");

		setSize(500, 500);
		setLocation(50, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(serverPanel);
		setVisible(true);

		try {
			ss = new ServerSocket(6789);
		} catch (IOException e) {
			e.printStackTrace();
		}



	}

	public static int findInVec(Vector<String> words, String s1)
	{
		for(int i = 0; i < words.size(); i++)
		{
			System.out.println(words.get(i)+" " +s1);
			if(words.get(i).equals(s1))
				return i;
		}
		return -1;
	}
	
	public static void parseTarget(String line) {
		StringTokenizer st = new StringTokenizer(line, "~", false);
		String name = st.nextToken();
		System.out.println("name: " + name);
		
		// read Target
		String target = st.nextToken();
		System.out.println("target: " + target);
		
		// read command
		String command = st.nextToken();
		System.out.println("command: " + command);
		// commands at end of this function
		
		String content = st.nextToken();
		System.out.println("content: " + content);
		
			content = name + "~" +command+"~"+ content;
			if (target.equalsIgnoreCase("all")) {
				sendMessage(content, null);
			}
			else if (target.equalsIgnoreCase("mafia")) {
				// targetMafia = true;
			}
			else if (target.equalsIgnoreCase("doctor")) {
				// targetDoctor = true;
			}
			else if (target.equalsIgnoreCase("cops")) {
				// targetCops = true;
			}
			else {
				System.out.println("Error: target cannot be parsed");
			}

		
		
	}

	public static void sendMessage(String line,  Vector<Player> receivers)
	{
		// set receivers to vector of players
		if(receivers == null)
			receivers = players;
			if(!initializing)
			{
				for(Player player : receivers)
				{
					ServerThread ct1 = player.st;
					ct1.send(line);
				}}
			if(initializing)
			{
				for(ServerThread ct1:st)
					ct1.send(line);
			}
		flag = true;
	}	


	public static void startup()
	{
		show = false;
		//Thread accept = new Thread(new AcceptSocket(ss));
		GameClient gc =new GameClient();
		Thread gcthread = new Thread(gc);
		gcthread.start();


		try {
			Socket sss = ss.accept();
			ServerThread ST0 = new ServerThread(sss);
			st.add(ST0);
			br0 = new BufferedReader(new InputStreamReader(sss.getInputStream()));
			ServerReader sr0 = new ServerReader(br0, sss);
			readers.add(sr0);
			sr0.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for(int i = 0; i <setup.numPlayers-1; i++)
		{

			try{

				Socket s = ss.accept();
				ServerThread ST = new ServerThread(s);
				st.add(ST);
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				ServerReader sr = new ServerReader(br, s);
				readers.add(sr);
				sr.start();

				if(i<0)
				{
					lock.lock();
					received.await();
					lock.unlock();}
			}

			//PLAYERNAME~ALL~CHAT~"MESSAGE"



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
		sendMessage(concatNames, null);
		for(ServerThread ST:st)
			ST.start();
		sendMessage("DONE",null);
		initializing=false;
		
		concatNames+="HOST";
		name1 = concatNames.split("~");
		long seed;
		seed = System.nanoTime();
		Collections.shuffle(Arrays.asList(name1), new Random(seed));
		Collections.shuffle(st, new Random(seed));
		Collections.shuffle(readers, new Random(seed));

		

		for(String s : name1)
		{
			System.out.println("NANME:"+s);
			GameServer.names.add(s);
		}
		
		GameClient.names0=names;
		
		for(int i = 0; i < setup.numVil; i++)
		{
			Villager p = new Villager(name1[pCount], st.get(pCount), readers.get(pCount));
			pCount++;
			players.add(p);
			villagers.add(p);
			st.get(pCount).send("VILLAGER");
		}
		for(int i = 0; i < setup.numCop; i++)
		{
			Cop p = new Cop(name1[pCount], st.get(pCount), readers.get(pCount));
			pCount++;
			players.add(p);
			cops.add(p);
			st.get(pCount).send("COP");
		}
		for(int i = 0; i < setup.numDoc; i++)
		{
			Doctor p = new Doctor(name1[pCount], st.get(pCount), readers.get(pCount));
			pCount++;
			players.add(p);
			doctors.add(p);
			st.get(pCount).send("DOCTOR");
		}

		for(int i = 0; i < setup.numMaf; i++)
		{

			TheMafia p = new Mafia(name1[pCount], st.get(pCount), readers.get(pCount));
			pCount++;
			players.add(p);
			mafia.add(p);
			st.get(pCount).send("MAFIA");
}
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