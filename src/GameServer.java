import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GameServer {
	public static Vector<Player> players= new Vector<Player>();
	private static SetUp setup = new SetUp();
	public static void startup()
	{
		for(int i = 0; i < setup.numVil; i++)
		{
			//Socket s = ss.accept();
			Villager p = new Villager("ronaldiscool");
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numCop; i++)
		{
			//Socket s = ss.accept();
			Cop p = new Cop("ronaldiscop");
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numDoc; i++)
		{
			//Socket s = ss.accept();
			Doctor p = new Doctor("ronaldisdoc");
			players.add(p);
			setup.relist();
		}
		for(int i = 0; i < setup.numMaf; i++)
		{
			//Socket s = ss.accept();
			TheMafia p = new Stripper("ronaldismaf");
			players.add(p);
			setup.relist();

		}
		setup.revalidate();
		setup.repaint();
		System.out.println("HERE)");
	}
	public static void main(String[] args)
	{
		try
		{
			
			ServerSocket ss = new ServerSocket(6789);
			
		}
		catch(Exception e)
		{
		}
	}
}