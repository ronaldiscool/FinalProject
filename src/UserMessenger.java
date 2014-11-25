import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;


public class UserMessenger extends JPanel {
	//TODO : possible container to hold the current players in the game, the lyncher combobox will be updated to hold the names of the players
	// 
	JLabel timeCycle=new JLabel("Night 1");
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
	
	JPanel textPanel;
	JLabel charLabel;
	JLabel voteLabel;
	
	public void reset(String deadPerson)
	{
		if(timeCycle.getText().substring(0,3).equals("Day")) {
			timeCycle.setText("Night " + dayCount);
			changeNight();
		}
		else
		{
			timeCycle.setText("Day " + ++dayCount);
			changeDay();
		}
		messageField.setText(messageField.getText()+"\n--------------------\n"+timeCycle.getText()+"\nLast round "+deadPerson+" was killed.\n");
		inputField.setText("");
		voteButton.setEnabled(true);
		sendButton.setEnabled(true);
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
		gbc.ipady=40;
		gbc.anchor=GridBagConstraints.NORTH;
		timeCycle=new JLabel("Day 1");
		timeCycle.setFont(new Font("Dialog", Font.BOLD, 20));
		messagePanel.add(timeCycle,gbc);
		
		gbc.ipady=0;
		gbc.gridwidth=1;
		gbc.gridheight=3;
		gbc.gridy=1;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		messageField=new JTextArea(" ",18,25);
		messageField.setEditable(false);
		textFieldPane=new JScrollPane(messageField);
		
		textPanel = new JPanel();
		charLabel = new JLabel("Chatroom");
		textPanel.setLayout(new BorderLayout() );
		textPanel.add(textFieldPane, BorderLayout.CENTER);
		textPanel.add(charLabel, BorderLayout.NORTH);
		messagePanel.add(textPanel,gbc);
	
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
		
		
		// pressing Enter key while focus is in input box will send message
		InputMap input = inputField.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		input.put(enter, "text-submit");
		
		/* uncomment this to allow newline in input box when pressing Shift+Enter
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(shiftEnter, "insert-break");
		*/
		
		ActionMap actions = inputField.getActionMap();
		actions.put("text-submit", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = inputField.getText();
				if (!message.equals("")) {
					gc.sendMessage(message,0);
				}
				inputField.setText("");
			}
		});

		
		voteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String target = lyncher.getSelectedItem().toString();
				UserMessenger.this.voteButton.setEnabled(false);
				UserMessenger.this.sendButton.setEnabled(false);
				gc.sendMessage(target, 1);
			}
		}
		);
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String message = inputField.getText();
				if (!message.equals("")) {
					gc.sendMessage(message,0);
				}
				inputField.setText("");
			}
		}
		);
		
		gbc.gridx=2;
		gbc.gridy=1;
		gbc.ipadx=40;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.weighty=1;
		gbc.anchor=GridBagConstraints.NORTH;

		voteLabel = new JLabel("Votes");
		messagePanel.add(voteLabel,gbc);

		gbc.gridx=3;
		gbc.anchor=GridBagConstraints.NORTH;
		lyncher=new JComboBox<String>();
		messagePanel.add(lyncher,gbc);

		gbc.gridwidth=2;
		gbc.gridx=2;
		gbc.gridy=3;
		gbc.weighty=0;
		gbc.ipady=30;
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		votes=new JTextArea(" ", 15,15);
		votes.setEditable(false);
				
		messagePanel.add(votes,gbc);
		
		this.add(messagePanel,gbc);
		this.setSize(500, 500);
		this.setVisible(true);
		
		
		charLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		voteLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		
		// Colors for the panels
		timeCycle.setForeground(Color.WHITE);
		charLabel.setForeground(Color.WHITE);
		voteLabel.setForeground(Color.WHITE);

		textPanel.setBackground(Color.DARK_GRAY);
		messagePanel.setBackground(Color.DARK_GRAY);
		this.setBackground(Color.DARK_GRAY);
		
		
		messageField.setBackground(Color.LIGHT_GRAY);
		inputField.setBackground(Color.LIGHT_GRAY);
		votes.setBackground(Color.LIGHT_GRAY);
		lyncher.setBackground(Color.LIGHT_GRAY);

		lyncher.setBorder(BorderFactory.createLineBorder(Color.black));
		textFieldPane.setBorder(BorderFactory.createLineBorder(Color.black));
		inputFieldPane.setBorder(BorderFactory.createLineBorder(Color.black));
		votes.setBorder(BorderFactory.createLineBorder(Color.black));
		
		sendButton.setForeground(Color.BLACK);
		voteButton.setForeground(Color.BLACK);


	}

	public void changeDay() {
		timeCycle.setForeground(Color.BLACK);
		charLabel.setForeground(Color.BLACK);
		voteLabel.setForeground(Color.BLACK);

		textPanel.setBackground(Color.LIGHT_GRAY);
		messagePanel.setBackground(Color.LIGHT_GRAY);
		this.setBackground(Color.LIGHT_GRAY);
		
		
		messageField.setBackground(Color.WHITE);
		inputField.setBackground(Color.WHITE);
		votes.setBackground(Color.WHITE);
		lyncher.setBackground(Color.WHITE);

		lyncher.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		textFieldPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		inputFieldPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		votes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
	}

	public void changeNight() {
		timeCycle.setForeground(Color.WHITE);
		charLabel.setForeground(Color.WHITE);
		voteLabel.setForeground(Color.WHITE);

		textPanel.setBackground(Color.DARK_GRAY);
		messagePanel.setBackground(Color.DARK_GRAY);
		this.setBackground(Color.DARK_GRAY);
		
		
		messageField.setBackground(Color.LIGHT_GRAY);
		inputField.setBackground(Color.LIGHT_GRAY);
		votes.setBackground(Color.LIGHT_GRAY);
		lyncher.setBackground(Color.LIGHT_GRAY);

		lyncher.setBorder(BorderFactory.createLineBorder(Color.black));
		textFieldPane.setBorder(BorderFactory.createLineBorder(Color.black));
		inputFieldPane.setBorder(BorderFactory.createLineBorder(Color.black));
		votes.setBorder(BorderFactory.createLineBorder(Color.black));
		
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
/*	
	public static void main(String []args) {
		JFrame j = new JFrame();
		j.setSize(640, 525);
		j.setLocation(50, 50);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.add(new UserMessenger());
		j.setVisible(true);
	}*/
}

