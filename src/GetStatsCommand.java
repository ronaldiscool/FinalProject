import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;


public class GetStatsCommand extends DatabaseCommand {
	
	String username;
	Player player;
	
	public GetStatsCommand(String dbUser, String dbPassword, ReentrantLock queryLock, String username, Player player) {
		super(dbUser, dbPassword, queryLock);
		
		this.username = username;
		this.player = player;
		//resultsArray = new int[9];
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
			
			String line = "0";
			
			if (rSet.next()) {
				line = 
						"~STATS~"
						+ rSet.getInt("times_played") + "~"
						+ rSet.getInt("overall_wins") + "~"
						+ rSet.getInt("overall_losses") + "~"
						+ rSet.getInt("times_as_mafia") + "~"
						+ rSet.getInt("wins_as_mafia") + "~"
						+ rSet.getInt("losses_as_mafia") + "~"
						+ rSet.getInt("times_as_villager") + "~"
						+ rSet.getInt("wins_as_villager") + "~"
						+ rSet.getInt("losses_as_villager") + "~";
			}
			System.out.println("Sending stats to: " + player.getName());
			
			Vector<Player> playerVector = new Vector<Player>();
			playerVector.add(player);
			GameServer.sendMessage(line, playerVector);
			
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
}
