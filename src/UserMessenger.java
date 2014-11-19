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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class UserMessenger extends JPanel {
	//TODO : possible container to hold the current players in the game, the lyncher combobox will be updated to hold the names of the players
	// 
	JLabel timeCycle=new JLabel("Day");
	JTextArea messageField;
	JTextArea inputField;
	JButton sendButton, voteButton;
	JComboBox<String> lyncher;
	JTextArea votes;
	String lynchvotes="";
	JPanel messagePanel;
	JScrollPane textFieldPane;
	JScrollPane inputFieldPane;
	private GameClient gc;
	int dayCount = 1;
	
	public void reset(String deadPerson)
	{
		if(timeCycle.getText().substring(0,3).equals("Day"))
			timeCycle.setText("Night" + dayCount);
		else
		{
			timeCycle.setText("Day " + ++dayCount);
		}
		messageField.setText(messageField.getText()+"\n--------------------\n"+timeCycle.getText()+"\n"+deadPerson+" was killed.\n");
		inputField.setText("");
		voteButton.setEnabled(true);
		lyncher.removeAllItems();
		updateLyncher();
		votes.setText("");
		lynchvotes="";
	}
	
	
	public UserMessenger(GameClient gc){
		this.makeGUI();
		this.gc = gc;
	}	
	public void addMessage(String message){
		messageField.setText(messageField.getText() + message + "\n");
		JScrollBar vertical = textFieldPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public void makeGUI(){
		messagePanel=new JPanel();
		messagePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=4;
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
		gbc.weightx=2;
		gbc.gridx=2;
		gbc.anchor=GridBagConstraints.CENTER;
		sendButton=new JButton("Send");
		messagePanel.add(sendButton,gbc);
		
		gbc.gridx=3;
		voteButton = new JButton("Vote");
		messagePanel.add(voteButton,gbc);

		
		voteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String target = lyncher.getSelectedItem().toString();
				gc.sendMessage(target, 1);
				UserMessenger.this.voteButton.setEnabled(false);
			}
		}
		);
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String message = inputField.getText();
				System.out.println("MESSAGE"+message);
				if (!message.equals("")) {
					gc.sendMessage(message,0);
				}
				inputField.setText("");
			}
		}
		);
		
		gbc.gridx=2;
		gbc.gridy=1;
		gbc.ipadx=50;
		gbc.weighty=1;
		gbc.anchor=GridBagConstraints.NORTH;
		lyncher=new JComboBox<String>();
		messagePanel.add(lyncher,gbc);
		
		gbc.gridwidth=2;
		gbc.gridx=2;
		gbc.gridy=3;
		gbc.weighty=0;
		gbc.ipady=30;
		gbc.anchor=GridBagConstraints.CENTER;
		votes=new JTextArea(" ", 15,15);
		votes.setEditable(false);
		messagePanel.add(votes,gbc);
		
		this.add(messagePanel,gbc);
		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	// this method will remove the specified player's entry in the combobox
	public void updateLyncher(){
		// clear players, iterate through container of players adding each alive player back to lyncher
		lyncher.addItem("NOBODY");
		for(String s : gc.names0)
		{
			lyncher.addItem(s);
		}
	}
	public void updateVotes(String voter, String target) {
		
			String textOfChat=votes.getText();
			textOfChat+=voter+ " votes for "+target+"\n";
			votes.setText(textOfChat);		

	}
}

