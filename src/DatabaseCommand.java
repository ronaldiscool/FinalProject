import java.util.concurrent.locks.ReentrantLock;


public abstract class DatabaseCommand implements Runnable {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String DB_NAME = "mafia";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	protected static String user ;
	protected static String password;	// set via server setup GUI
	
	protected ReentrantLock queryLock;
	
	public DatabaseCommand(String username, String password, ReentrantLock queryLock) {
		this.queryLock = queryLock;
		DatabaseCommand.user = username;
		DatabaseCommand.password = password;
	}
	
	@Override
	public void run() {
		queryLock.lock();
		execute();
		queryLock.unlock();
	}
	
	protected abstract boolean execute();
	
	// TODO remove this
	/* testing code -- to remove
	public static void main(String [] args) {
		ReentrantLock lock = new ReentrantLock();
		AddResult test = new AddResult(lock, "jamesbond", "mafia", true);
		test.run();
	}
	*/
}

