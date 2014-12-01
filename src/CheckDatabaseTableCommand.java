import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;


public class CheckDatabaseTableCommand extends DatabaseCommand {

	private boolean error;
	
	public CheckDatabaseTableCommand(String dbUser, String dbPassword, ReentrantLock queryLock) {
		super(dbUser, dbPassword, queryLock);

		error = false;
	}

	protected boolean execute() {
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(
				DB_ADDRESS+DB_NAME, user, password
			);
			
			Statement stmt = conn.createStatement();
			stmt.execute("SELECT * FROM user LIMIT 0;");
			
			return true;
		}
		catch (SQLException e) {
			error = true;
			return false;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean errorCheck() {
		return error;
	}
}
