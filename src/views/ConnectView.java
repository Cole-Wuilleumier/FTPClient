package views;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.MainWindow;

import controllers.Ftp;

public class ConnectView extends Panel {
	
	private Ftp ftp;
	
	public ConnectView(MainWindow window, Frame mainFrame){
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		Panel inputFields = new Panel();
		GridBagLayout innerLayout = new GridBagLayout();
		inputFields.setLayout(innerLayout);
		
		Panel leftBuffer = new Panel();
		Panel rightBuffer = new Panel();
		
		
		/**
		 * Add Components Below
		 */
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = .5;
		gbc.weighty = .2;
		gbc.insets = new Insets(0,1,3,3);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		Label hostLabel = new Label("Host");
		hostLabel.setAlignment(Label.CENTER);
		inputFields.add(hostLabel, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		TextField hostInput = new TextField();
		inputFields.add(hostInput, gbc);
		

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		Label usernameLabel = new Label("Username");
		usernameLabel.setAlignment(Label.CENTER);
		inputFields.add(usernameLabel, gbc);
		
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		TextField usernameInput = new TextField();
		inputFields.add(usernameInput, gbc);

		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		Label passwordLabel = new Label("Password");
		passwordLabel.setAlignment(Label.CENTER);
		inputFields.add(passwordLabel, gbc);
		
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		TextField passwordInput = new TextField();
		passwordInput.setEchoChar('*');
		inputFields.add(passwordInput, gbc);
		


		
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label statusLabel = new Label("");
		inputFields.add(statusLabel, gbc);
		
		Button connectButton = new Button("Connect");
		connectButton.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   //Get values from input fields
				   String host = hostInput.getText();
				   if(host.isEmpty()) {
					   statusLabel.setText("Host field cannot be empty.");
				   }
				   
				   String username = usernameInput.getText();
				   if(username.isEmpty()) {
					   statusLabel.setText("Username field cannot be empty.");
				   }
				   
				   String password = passwordInput.getText();
				   if(password.isEmpty()) {
					   statusLabel.setText("Password field cannot be empty.");
				   }
				   
				   
				   ftp = new Ftp(host, username, password);
				   ftp.connect();
				   if(ftp.getConnected()) {
					   statusLabel.setText("");
					   mainFrame.removeAll();
					   window.setCurrentPanel(new MainView(window, mainFrame, ftp));
					   window.setupGUI();
					   mainFrame.repaint();
					   
				   } else {
					   statusLabel.setText("Connection to server failed.");
				   }

			   }
		});
		

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = .1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		inputFields.add(connectButton, gbc);
		
		
		
		//Alignment Columns
		Panel col1 = new Panel();
		Panel col2 = new Panel();
		Panel col3 = new Panel();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 0;
		gbc.weighty = .5;
		inputFields.add(col1, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		inputFields.add(col2, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 5;
		inputFields.add(col3, gbc);
		
		//Left Buffer
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = .1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		add(leftBuffer, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = .8;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		add(inputFields, gbc);
		
		//Right Buffer
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = .1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		add(rightBuffer, gbc);
		
		setVisible(true);
		
	}
	
}
