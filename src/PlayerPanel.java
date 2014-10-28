import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class PlayerPanel extends JPanel {
	public PlayerPanel()
	{
		super();
		setBackground(Color.gray);
		Object values[] = {"Item 1", "Item 2"};
		
		JScrollPane scroll = new JScrollPane();
		JList playersList = new JList(values);
		scroll.setViewportView(playersList);
		scroll.setPreferredSize(new Dimension(200, 250));
		
		playersList.setBackground(Color.white);
		
		JPanel panel1 = new JPanel();
		panel1.add(scroll);
		panel1.setBackground(Color.gray);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createGlue());
		add(panel1);
		add(Box.createGlue());
		
	}
	void relist()
	{
		removeAll();
		System.out.println(GameServer.players.size());

		for(Player p :GameServer.players)
		{
			JLabel jl = new JLabel(p.getName());
			add(jl);
		}
		revalidate();
		repaint();
	}
}
