import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class UserMessenger extends JPanel {
	JLabel timeCycle;
	JTextArea messageField;
	JTextArea inputField;
	JButton sendButton;
	JComboBox<String> lyncher;
	JTextArea votes;
	JPanel messagePanel;
	JScrollPane textFieldPane;
	JScrollPane inputFieldPane;
	
	
	
	public UserMessenger(){
		this.makeGUI();
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
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		messageField=new JTextArea(" ",18,25);
		messageField.setEditable(false);
		textFieldPane=new JScrollPane(messageField);
		messagePanel.add(textFieldPane,gbc);
	
		gbc.gridheight=1;
		gbc.weighty=1;
		gbc.gridx=0;
		gbc.gridy=4;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		inputField=new JTextArea(" ",5,25);
		inputFieldPane=new JScrollPane(inputField);
		messagePanel.add(inputFieldPane,gbc);
		
		gbc.weighty=0;
		gbc.weightx=1;
		gbc.gridx=1;
		gbc.gridy=4;
		gbc.anchor=GridBagConstraints.CENTER;
		sendButton=new JButton("Send");
		messagePanel.add(sendButton,gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
		gbc.ipadx=20;
		gbc.weighty=1;
		gbc.anchor=GridBagConstraints.NORTH;
		lyncher=new JComboBox<String>();
		lyncher.addItem("Stripper");
		lyncher.addItem("Cop");
		lyncher.addItem("Doctor");
		lyncher.addItem("GodFather");
		lyncher.addItem("Detective");
		lyncher.addItem("GodFather");
		lyncher.addItem("Villager");
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
	
	//public static void main(String args[])
	//{
	//	new UserMessenger();
	//}
	}

