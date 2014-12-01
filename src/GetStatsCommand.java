import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;


public class GetStatsCommand extends DatabaseCommand {
	
	String username;
	
	private int[] resultsArray;
	
	public GetStatsCommand(String dbUser, String dbPassword, ReentrantLock queryLock, String username) {
		super(dbUser, dbPassword, queryLock);
		
		this.username = username;
		resultsArray = new int[9];
	}

	protected boolean execute() {
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(
				DB_ADDRESS+DB_NAME, user, password
			);
			
			
			PreparedStatement pStmt = conn.prepareStatement(
				"SELECT * FROM user WHERE username=(?);"
			);
			
			pStmt.setString(1, username);
			pStmt.execute();
			
			ResultSet rSet = pStmt.getResultSet();
			
			
			if (rSet.next()) {
				resultsArray[0] = rSet.getInt("times_played");
				resultsArray[1] = rSet.getInt("overall_wins");
				resultsArray[2] = rSet.getInt("overall_losses");
				resultsArray[3] = rSet.getInt("times_as_mafia");
				resultsArray[4] = rSet.getInt("wins_as_mafia");
				resultsArray[5] = rSet.getInt("losses_as_mafia");
				resultsArray[6] = rSet.getInt("times_as_villager");
				resultsArray[7] = rSet.getInt("wins_as_villager");
				resultsArray[8] = rSet.getInt("losses_as_villager");
			}
			
			return true;
		}
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("SQL error: " + e.getMessage());
			System.out.println("Please ensure that the mafia table exists and that the database's username and password are correct");
			System.out.println("To create the mafia table, execute the included createDB.sql file in MySQL Workbench.");
			return false;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public int[] getResultsArray() {
		return resultsArray;
	}
}
