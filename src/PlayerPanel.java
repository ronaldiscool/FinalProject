import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class PlayerPanel extends JPanel {
	public PlayerPanel()
	{
		super();
		setBackground(Color.gray);

	}
	void relist()
	{
		removeAll();
		for(Player p :GameServer.players)
		{
			JLabel jl = new JLabel(p.getName());
			add(jl);
		}
		revalidate();
		repaint();
	}
}
