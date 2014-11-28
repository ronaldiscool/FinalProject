import java.util.concurrent.locks.ReentrantLock;


public abstract class DatabaseCommand implements Runnable {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String DB_NAME = "mafia";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "MediaPortal";
	
	protected ReentrantLock queryLock;
	
	public DatabaseCommand(ReentrantLock queryLock) {
		this.queryLock = queryLock;
	}
	
	@Override
	public void run() {
		queryLock.lock();
		System.out.print("Executing... ");
		execute();
		System.out.println("Done");
		queryLock.unlock();
	}
	
	public abstract boolean execute();
	
	// TODO remove this
	/* testing code -- to remove
	public static void main(String [] args) {
		ReentrantLock lock = new ReentrantLock();
		AddResult test = new AddResult(lock, "jamesbond", "mafia", true);
		test.execute();
	}
	*/
}

