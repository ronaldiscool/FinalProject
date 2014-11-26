import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.Semaphore;
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
	String name;
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
			name = line;
			GameServer.concatNames+=line+"~";
			GameServer.name1 = GameServer.concatNames.split("~");

			for(String n:GameServer.name1)

			{
				JLabel jl = new JLabel(n);
				GameServer.setup.playerPanel.add(jl);
			}
			GameServer.sendMessage(GameServer.concatNames, null);
			++GameServer.numReg;
			System.out.println("RERGISTERED:"+GameServer.numReg+"  "+GameServer.setup.numPlayers);
			if(GameServer.numReg==GameServer.setup.numPlayers)
			{GameServer.lock.lock();
			GameServer.read.signalAll();
			GameServer.lock.unlock();}
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
	public static  Vector<Player> players= new Vector<Player>();
	public static  Vector<ServerThread> st = new Vector<ServerThread>();
	public static Vector<Villager> villagers= new Vector<Villager>();
	public static Vector<Cop> cops= new Vector<Cop>();
	public static Vector<Mafia> mafia= new Vector<Mafia>();
	//public static Vector<Stripper> strippers= new Vector<Stripper>();
	public static Vector<Doctor> doctors= new Vector<Doctor>();
	public static Vector<ServerReader> readers = new Vector<ServerReader>();
	public static CardLayout c1=new CardLayout();
	public static JPanel serverPanel=new JPanel();

	static SetUp setup = new SetUp();
	public static ServerSocket ss;
	static String inBuffer = "";
	static String concatNames = "";
	static ReentrantLock lock = new ReentrantLock();
	static Condition read = lock.newCondition();
	static Condition allvotes = lock.newCondition();
	static Condition released = lock.newCondition();
	static Condition allmafvotes = lock.newCondition();
	static Condition mafreleased = lock.newCondition();
	static Condition alldocvotes = lock.newCondition();
	static Condition doctorDone2 = lock.newCondition();
	static Condition allcopvotes = lock.newCondition();
	static Condition copDone2 = lock.newCondition();
	static Semaphore allvotesSem;
	static Semaphore allmafSem;
	static Semaphore alldocSem;
	static Semaphore allcopSem;
	static boolean show = true;
	static boolean initializing = true;
	static int numReg = 0;
	static BufferedReader br0;
	volatile static boolean doctorDone = false;
	volatile static boolean copDone = false;
	
	public GameServer()
	{
		super("Mafia");


		// cardlayout for the messenger and the setup panel
		serverPanel.setLayout(c1);
		serverPanel.add(setup, "setup");
		c1.show(serverPanel, "setup");

		setSize(500, 500);
		setLocation(50, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(serverPanel);
		setVisible(true);

		try {
			ss = new ServerSocket(6789);
			//System.out.println("ADDRESS"+Inet4Address.getLocalHost().getHostAddress());
			} 
			catch (IOException e) {
			e.printStackTrace();
		}



	}

	static Player find_name(String s)
	{
		for(Player p : players)
			if(p.name.equals(s))
				return p;
		return null;
	}
	public static void parseTarget(String line) {
		System.out.println("PSSMYTEMP"+line);
		StringTokenizer st = new StringTokenizer(line, "~", false);
		String name = st.nextToken();
		name=name.trim();
		System.out.println("name: " + name);
		
		// read Target
		String target = st.nextToken();
		target=target.trim();
		
		System.out.println("target: " + target);
		
		// read command
		String command = st.nextToken();
		command=command.trim();
		System.out.println("command: " + command);
		// commands at end of this function
		
		String content = st.nextToken();
		System.out.println("content: " + content);
		
			String content1 = name + "~" +command+"~"+ content;
			if (command.equalsIgnoreCase("vote")) {
							if (target.equalsIgnoreCase("all")) {
							sendMessage(content1,null);
							Player ptarget;
							Player	p = find_name(name);
							System.out.println(name);
							if(content.equals("NOBODY"))
								ptarget=null;
							else
								ptarget = find_name(content);
							p.vote(ptarget);
							}
							/*else if (target.equalsIgnoreCase("mafia")) {
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
							}*/
						}
						else if (command.equalsIgnoreCase("chat")) {
							if (target.equalsIgnoreCase("all")) {
								System.out.println(content1);
								sendMessage(content1,null);
								}
								else if (target.equalsIgnoreCase("mafia")) {
									sendMessage(content1,mafia);
								}
								else if (target.equalsIgnoreCase("doctor")) {
									sendMessage(content1,doctors);
								}
								else if (target.equalsIgnoreCase("cops")) {
									sendMessage(content1,cops);
								}
								else {
									System.out.println("Error: target cannot be parsed");
								}
						}
						else if (command.equalsIgnoreCase("power")) {
							if (target.equalsIgnoreCase("mafia")) {
									sendMessage(content1,mafia);
								}
								else if (target.equalsIgnoreCase("doctor")) {
									sendMessage(content1,doctors);
								}
								else if (target.equalsIgnoreCase("cops")) {
									sendMessage(content1,cops);
								}
							Player ptarget;
							Player	p = find_name(name);
							if(content.equals("NOBODY"))
								ptarget=null;
							else
								ptarget = find_name(content);
							System.out.println(p.getName()+"ENTERED");
							p.power(ptarget);
						}
						else {
							System.out.println("Error: command cannot be parsed");
						}

		
		
	}

	synchronized public static <T extends Player> void sendMessage(String line,  Vector<T> receivers)
	{
		// set receivers to vector of players
			if(!initializing)
			{
				System.out.println("SENDPLZZZ"+line);
				if(receivers==null)
				{
					for(Player player : players)
					{
						System.out.println("SENT");
						ServerThread ct1 = player.st;
						ct1.send(line);
					}
				}
			else
			{
				for(T player : receivers)
				{
					ServerThread ct1 = player.st;
					ct1.send(line);
				}}
			}
			if(initializing)
			{
				for(ServerThread ct1:st)
					ct1.send(line);
			}
	}	

	public static void removeThread(ServerThread ct) {
		st.remove(ct);
	}

	public static void startup()
	{
		show = false;
		GameClient gc =new GameClient(true);
		Thread gcthread = new Thread(gc);
		gcthread.start();


		try {
			Socket sss = ss.accept();
			System.out.println("ADDD"+sss.getInetAddress());
			ServerThread ST0 = new ServerThread(sss);
			st.add(ST0);
			//ST0.start();
			br0 = new BufferedReader(new InputStreamReader(sss.getInputStream()));
			ServerReader sr0 = new ServerReader(br0, sss);
			readers.add(sr0);
			sr0.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		gc.pw.println("HOST");
		gc.pw.flush();
		for(int i = 0; i <setup.numPlayers-1; i++)
		{

			try{

				Socket s = ss.accept();
				ServerThread ST = new ServerThread(s);
				st.add(ST);
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				ServerReader sr = new ServerReader(br,s);
				readers.add(sr);
				sr.start();


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
		for(ServerThread ST:st)
			ST.start();
		sendMessage("DONE",null);
		
		concatNames=concatNames;
		System.out.println("concatname"+concatNames);
		name1 = concatNames.split("~");
		long seed;
		seed = System.nanoTime();
		Vector<Integer> indices = new Vector<Integer>();
		for(int i  = 0; i < setup.numPlayers; i++)
		{
			indices.add(i);
		}
		Collections.shuffle(indices);
		//Collections.shuffle(Arrays.asList(name1), new Random(seed));
		//Collections.shuffle(st, new Random(seed));
		//Collections.shuffle(readers, new Random(seed));

				
		for(int i = 0; i < setup.numVil; i++)
		{
			int index=indices.get(pCount);
			Villager p = new Villager(readers.get(index).name, st.get(index), readers.get(index));
			players.add(p);
			villagers.add(p);
			st.get(index).send("VILLAGER");
			pCount++;

		}
		for(int i = 0; i < setup.numCop; i++)
		{
			int index=indices.get(pCount);
			Cop p = new Cop(readers.get(index).name, st.get(index), readers.get(index));
			players.add(p);
			cops.add(p);
			st.get(index).send("COP");
			pCount++;

		}
		for(int i = 0; i < setup.numDoc; i++)
		{
			int index=indices.get(pCount);

			Doctor p = new Doctor(readers.get(index).name, st.get(index), readers.get(index));
			players.add(p);
			doctors.add(p);
			st.get(index).send("DOCTOR");
			pCount++;

		}

		for(int i = 0; i < setup.numMaf; i++)
		{
			int index=indices.get(pCount);

			Mafia p = new Mafia(readers.get(index).name, st.get(index), readers.get(index));
			players.add(p);
			mafia.add(p);
			st.get(index).send("MAFIA");
			pCount++;
}
		initializing=false;

	}



	public static void main(String[] args)
	{
		try {
			System.out.println(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread gs =new Thread(new GameServer());	
		gs.start();

	}

	public void run()
	{
		ServerRepainter rp = new ServerRepainter(this);
		rp.start();

	}
}
