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


public class UserMessenger extends JFrame {
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
		gbc.ipady=10;
		gbc.anchor=GridBagConstraints.NORTH;
		timeCycle=new JLabel("Day");
		messagePanel.add(timeCycle,gbc);
		

		gbc.gridwidth=1;
		gbc.gridheight=2;
		gbc.gridx=0;
		gbc.gridy=1;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		messageField=new JTextArea(" ",35,45);
		textFieldPane=new JScrollPane(messageField);
		messagePanel.add(textFieldPane,gbc);
	
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.anchor=GridBagConstraints.FIRST_LINE_START;
		inputField=new JTextArea(" ",5,45);
		inputFieldPane=new JScrollPane(inputField);
		messagePanel.add(inputFieldPane,gbc);
		
		gbc.weightx=1;
		gbc.gridx=1;
		gbc.gridy=3;
		gbc.anchor=GridBagConstraints.CENTER;
		sendButton=new JButton("Send");
		messagePanel.add(sendButton,gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
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
		gbc.gridy=2;
		gbc.anchor=GridBagConstraints.CENTER;
		votes=new JTextArea(" ", 20,15);
		messagePanel.add(votes,gbc);
		
		this.setTitle("Messenger");
		this.add(messagePanel);
		this.setSize(750, 750);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String args[])
	{
		new UserMessenger();
	}
	}

