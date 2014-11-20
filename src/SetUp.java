import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SetUp extends JPanel {
	static final long serialVersionUID = 1;
	private JComboBox<String> citizenBox;
	private JComboBox<String> doctorBox;
	private JComboBox<String> mafiaBox;
	private JComboBox<String> copBox;
	private JComboBox<String> hookerBox;
	private JLabel numLabel; // message that signals whether the options are valid
	private JLabel messageLabel; // message that signals whether the options are valid
	public int numPlayers;
	public int numCop;
	public int numVil;
	public int numDoc;
	public int numMaf;
	public int numHook;
	public int goodNum;
	public int badNum;
	PlayerPanel playerPanel = new PlayerPanel();
	public boolean start = false;
	
	void relist()
	{
		playerPanel.relist();
		revalidate();
		repaint();
	}
	
	public SetUp() {
		JLabel citizenLabel = new JLabel("How many citizens?");
		JLabel doctorLabel = new JLabel("How many doctors?");
		JLabel mafiaLabel = new JLabel("How many mafias?");
		JLabel copLabel = new JLabel("How many cops?");
		JLabel hookerLabel = new JLabel("How many hookers?");
		
		citizenBox = new JComboBox<String>();
		doctorBox = new JComboBox<String>();
		mafiaBox = new JComboBox<String>();
		copBox = new JComboBox<String>();
		hookerBox = new JComboBox<String>();

		for(int i=0; i<11; ++i) { // Adds the numbers for the boxes
			copBox.addItem("" + i);
			doctorBox.addItem("" + i);
			hookerBox.addItem("" + i);
		}

		for(int i=1; i<15; ++i) { // Adds the numbers for the box
			mafiaBox.addItem("" + i);
		}
		
		for(int i=2; i<15; ++i) { // Adds the numbers for the boxes
			citizenBox.addItem("" + i);
		}
		
		ItemListener il = new ItemListener() { // Account for leap year
			public void itemStateChanged(ItemEvent ie) {
				int num = 0;
				num+= citizenBox.getSelectedIndex()+2;
				num+= copBox.getSelectedIndex();
				num+= doctorBox.getSelectedIndex();
				num+= mafiaBox.getSelectedIndex()+1;
				num+= hookerBox.getSelectedIndex();
				numLabel.setText("Number of Players: " + num);
			}
		};

		citizenBox.addItemListener(il);
		doctorBox.addItemListener(il);
		mafiaBox.addItemListener(il);
		copBox.addItemListener(il);
		hookerBox.addItemListener(il);

		
		JButton okButton = new JButton("Okay"); // Confirmation button
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				numVil= citizenBox.getSelectedIndex()+2;
				goodNum=numVil;
				numCop = copBox.getSelectedIndex();
				goodNum+=numCop;
				numDoc = doctorBox.getSelectedIndex();
				goodNum+=numDoc;
				numMaf = mafiaBox.getSelectedIndex()+1;
				badNum = numMaf;
				numHook = hookerBox.getSelectedIndex();
				badNum+=numHook;
				GameServer.allvotesSem = new Semaphore(goodNum+badNum);
				GameServer.allmafSem = new Semaphore(numMaf);
				GameServer.alldocSem=new Semaphore(numDoc);
				if(goodNum+1 < badNum) // checks ratio NOTE: modify accordingly
					messageLabel.setText("Error. You need to more citizens/cop/doctors.");
				else
				{
					numPlayers = goodNum+badNum;
					messageLabel.setText("Processing...");
					GameServer.startup();
				}
			}
		}
		);
		
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		numLabel = new JLabel("Number of Players: 3");
		
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0;		gbc.gridy = 0;      gbc.gridwidth = 2;
		selectPanel.add(numLabel, gbc);
		
		gbc.gridx = 0;		gbc.gridy = 1;		gbc.gridwidth = 1;
		selectPanel.add(citizenBox, gbc);
		gbc.gridx = 1;
		selectPanel.add(citizenLabel, gbc);
		gbc.gridx = 0;		gbc.gridy = 2;
		selectPanel.add(copBox, gbc);
		gbc.gridx = 1;
		selectPanel.add(copLabel, gbc);
		gbc.gridx = 0;		gbc.gridy = 3;
		selectPanel.add(doctorBox, gbc);
		gbc.gridx = 1;
		selectPanel.add(doctorLabel, gbc);
		gbc.gridx = 0;		gbc.gridy = 4;
		selectPanel.add(mafiaBox, gbc);
		gbc.gridx = 1;
		selectPanel.add(mafiaLabel, gbc);
		gbc.gridx = 0;		gbc.gridy = 5;
		selectPanel.add(hookerBox, gbc);
		gbc.gridx = 1;
		selectPanel.add(hookerLabel, gbc);
		gbc.gridy = 6;
		gbc.gridx = 1;
		selectPanel.add(okButton, gbc);
		
		JPanel numPanel = new JPanel();
		numPanel.add(new JLabel("Choose the number of roles for this round."));
		numPanel.setBackground(Color.white);

		messageLabel = new JLabel("Choose the number of roles for this round.");
		JPanel messagePanel = new JPanel();
		messagePanel.add(messageLabel);
		messagePanel.setBackground(Color.white);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 2));
		mainPanel.add(selectPanel);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		add(numPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
}