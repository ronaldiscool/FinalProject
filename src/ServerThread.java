/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread{
	private Socket s;
	private GameServer cs;
	private PrintWriter pw;
	public ServerThread(Socket s, GameServer cs)
	{
		super();
		this.s = s;
		this.cs = cs;
		try {
			this.pw = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String message)
	{
		pw.println(message);
		pw.flush();
	}
	
	public void run()
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while(true)
				{
				String line = br.readLine();
				cs.sendMessage(line, this);
				
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}*/
