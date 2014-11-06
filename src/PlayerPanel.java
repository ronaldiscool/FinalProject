import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class PlayerPanel extends JPanel {
	private DefaultListModel<String> list;
	public PlayerPanel()
	{
		super();
		setBackground(Color.gray);
		
		JScrollPane scroll = new JScrollPane();
		list = new DefaultListModel<String>();
		JList<String> playersList = new JList<String>(list);
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
	public void addName(String name) {
		for(int i=0; i<list.size(); ++i) // prevents copies of old names
			if(list.get(i).equals(name))
				return;
		list.addElement(name);
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
