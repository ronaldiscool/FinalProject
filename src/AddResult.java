import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;


public class AddResult extends DatabaseCommand {
	
	String username;

	boolean isMafia = false;
	boolean isVillager = false;
	boolean wonGame = false;
	
	public AddResult(ReentrantLock queryLock, String username, String role, boolean wonGame) {
		super(queryLock);
		
		this.username = username;


		if (role.equalsIgnoreCase("mafia")) {
			isMafia = true;
		}
		else if (role.equalsIgnoreCase("villager")) {
			isVillager = true;
		}
		
		if (wonGame) {
			this.wonGame = true;
		}
	}

	public boolean execute() {
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(
				DB_ADDRESS+DB_NAME, USER, PASSWORD
			);
			
			/* replaced by ON DUPLICATE KEY UPDATE
			Statement stmt = conn.createStatement();
			stmt.execute("SELECT * FROM user;");
			ResultSet rSet = stmt.getResultSet();
			
			String tempUsername = null;
			boolean userExists = false;
			while (rSet.next()) {
				tempUsername = rSet.getString("username");
				//check if username matches
			}
			*/
			
			PreparedStatement pStmt = conn.prepareStatement(
				//"INSERT INTO user (name,x) VALUES (?,?);"
				"INSERT INTO user (username, times_played, overall_wins, overall_losses,"
				+ " times_as_mafia, wins_as_mafia, losses_as_mafia,"
				+ " times_as_villager, wins_as_villager, losses_as_villager) VALUES (?,1,0,0,0,0,0,0,0,0)"
				+ " ON DUPLICATE KEY UPDATE times_played=times_played+1;"
			);
			
			pStmt.setString(1, username);
			pStmt.execute();
			
			updateStats(conn);
			
			return true;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void updateStats(Connection conn) {
		try {
			if (isMafia) {
				if (wonGame) {
					//Statement stmt = conn.createStatement();
					//stmt.execute("SELECT * FROM words ORDER BY RAND() LIMIT 2;");
					PreparedStatement pStmt = conn.prepareStatement(
							"UPDATE user "
							+ "SET overall_wins=overall_wins+1, "
							+ "times_as_mafia=times_as_mafia+1, "
							+ "wins_as_mafia=wins_as_mafia+1 "
							+ "WHERE username=(?);");
					pStmt.setString(1, username);
					pStmt.execute();
				}
				else {
					PreparedStatement pStmt = conn.prepareStatement(
							"UPDATE user "
							+ "SET overall_losses=overall_losses+1, "
							+ "times_as_mafia=times_as_mafia+1, "
							+ "losses_as_mafia=losses_as_mafia+1 "
							+ "WHERE username=(?);");
					pStmt.setString(1, username);
					pStmt.execute();
				}
			}
			
			else if (isVillager) {
				if (wonGame) {
					PreparedStatement pStmt = conn.prepareStatement(
							"UPDATE user "
							+ "SET overall_wins=overall_wins+1, "
							+ "times_as_villager=times_as_villager+1, "
							+ "wins_as_villager=wins_as_villager+1 "
							+ "WHERE username=(?);");
					pStmt.setString(1, username);
					pStmt.execute();
				}
				else {
					PreparedStatement pStmt = conn.prepareStatement(
							"UPDATE user "
							+ "SET overall_losses=overall_losses+1, "
							+ "times_as_villager=times_as_villager+1, "
							+ "losses_as_villager=losses_as_villager+1 "
							+ "WHERE username=(?);");
					pStmt.setString(1, username);
					pStmt.execute();
				}
			}
			else {
				System.out.println("logic fail");
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
