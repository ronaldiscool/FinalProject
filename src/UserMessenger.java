import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class UserMessenger extends JPanel {
	//TODO : possible container to hold the current players in the game, the lyncher combobox will be updated to hold the names of the players
	// 
	JLabel timeCycle;
	JTextArea messageField;
	JTextArea inputField;
	JButton sendButton;
	JComboBox<String> lyncher;
	JTextArea votes;
	JPanel messagePanel;
	JScrollPane textFieldPane;
	JScrollPane inputFieldPane;
	private GameClient gc;
	private GameServer gs;
	
	public UserMessenger(GameClient gc){
		this.makeGUI();
		this.gc = gc;
	}	
	public UserMessenger(GameServer gs){
		this.makeGUI();
		this.gs = gs;
	}	
	
	public void addMessage(String message){
		messageField.setText(messageField.getText() + message);
	}
	
	public void makeGUI(){
		messagePanel=new JPanel();
		messagePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=2;
		gbc.ipady=55;
		gbc.anchor=GridBagConstraints.NORTH;
		timeCycle=new JLabel("Day");
		messagePanel.add(timeCycle,gbc);
		
		gbc.ipady=0;
		gbc.gridwidth=1;
		gbc.gridheight=3;
		gbc.gridy=1;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		messageField=new JTextArea(" ",18,25);
		messageField.setEditable(false);
		textFieldPane=new JScrollPane(messageField);
		messagePanel.add(textFieldPane,gbc);
	
		gbc.gridheight=1;
		gbc.weighty=1;
		gbc.gridy=4;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		inputField=new JTextArea(" ",5,25);
		inputFieldPane=new JScrollPane(inputField);
		messagePanel.add(inputFieldPane,gbc);
		
		gbc.weighty=0;
		gbc.weightx=1;
		gbc.gridx=1;
		gbc.anchor=GridBagConstraints.CENTER;
		sendButton=new JButton("Send");
		messagePanel.add(sendButton,gbc);
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String message = messageField.getText();
				
			}
		}
		);
		
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.ipadx=20;
		gbc.weighty=1;
		gbc.anchor=GridBagConstraints.NORTH;
		lyncher=new JComboBox<String>();
		messagePanel.add(lyncher,gbc);
		
		gbc.gridx=1;
		gbc.gridy=3;
		gbc.weighty=0;
		gbc.anchor=GridBagConstraints.CENTER;
		votes=new JTextArea(" ", 15,15);
		votes.setEditable(false);
		messagePanel.add(votes,gbc);
		
		this.add(messagePanel);
		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	// this method will remove the specified player's entry in the combobox
	public void updateLyncher(){
		// clear players, iterate through container of players adding each alive player back to lyncher
	}
	public void updateVotes() {
		for(String name0 : GameServer.concatNames.split("~"))
		{
			//Player p = GameServer.players.get(i);
			//String name = p.getName() + ":";
			System.out.println("Adolf" + name0);
			String textOfChat=votes.getText();
			textOfChat=name0+"\n";
			votes.setText(textOfChat);		
		}

	}
}

