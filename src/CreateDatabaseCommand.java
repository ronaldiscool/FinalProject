import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;


public class CreateDatabaseCommand implements Runnable {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String DB_NAME = "mafia";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	protected static String user;
	protected static String password;	// set via server setup GUI
	
	protected ReentrantLock queryLock;
	
	public CreateDatabaseCommand(String username, String password, ReentrantLock queryLock) {
		this.queryLock = queryLock;
		CreateDatabaseCommand.user = username;
		CreateDatabaseCommand.password = password;
	}
	
	@Override
	public void run() {
		queryLock.lock();
		execute();
		queryLock.unlock();
	}
	
	protected boolean execute() { 
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(
				DB_ADDRESS, user, password
			);
			
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA IF NOT EXISTS `mafia`;");
			stmt.execute("USE `mafia`;");
			stmt.execute("DROP TABLE IF EXISTS `user`;");
			stmt.execute("CREATE TABLE `user` ("
					+ "`user_id` int(11) NOT NULL AUTO_INCREMENT,"
					+ "`username` varchar(100) DEFAULT NULL,"
					+ "`times_played` int(7) DEFAULT NULL,"
					+ "`overall_wins` int(7) DEFAULT NULL,"
					+ "`overall_losses` int(7) DEFAULT NULL,"
					+ "`times_as_mafia` int(7) DEFAULT NULL,"
					+ "`wins_as_mafia` int(7) DEFAULT NULL,"
					+ "`losses_as_mafia` int(7) DEFAULT NULL,"
					+ "`times_as_villager` int(7) DEFAULT NULL,"
					+ "`wins_as_villager` int(7) DEFAULT NULL,"
					+ "`losses_as_villager` int(7) DEFAULT NULL,"
					+ "PRIMARY KEY (`user_id`),"
					+ "UNIQUE (`username`)"
					+ ") ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=latin1;");
			stmt.close();
			conn.close();
			
			
			conn = DriverManager.getConnection(
					DB_ADDRESS+DB_NAME, user, password
			);
			stmt = conn.createStatement();
			stmt.execute("SELECT * FROM user LIMIT 0;");
			
			return true;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}

