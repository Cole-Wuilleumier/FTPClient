package views;
import java.awt.*;
import java.awt.event.*;


/*
 * MainWindow - The main view of the application
 * In charge of switching between views
 */
public class MainWindow {
	private Frame mainFrame;
	private Panel currentPanel;
	private boolean startup = true; 
	
	private int windowWidth = 600;
	private int windowHeight = 500;
	
	
	public MainWindow(){
		setupGUI();
	}
	
	//Setups up the window without setting up panels
	public void setupGUI() {
		GridBagConstraints constraints = new GridBagConstraints();
		
		//Only create frame on startup
		if(startup == true) {
			mainFrame = new Frame("FTP Client");
		}
		
		GridBagLayout layout = new GridBagLayout();
		mainFrame.setLayout(layout);
		mainFrame.setSize(windowWidth, windowHeight);
		
		//Close window when exit button clicked
		mainFrame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent we) {
	        	System.exit(0);
	         }
	     });
		
		Label headerLabel = new Label("FTP Client");
		headerLabel.setAlignment(Label.CENTER);
		
		Panel headerPanel = new Panel();
		headerPanel.setSize(windowWidth, 100);
		headerPanel.setLayout(new BorderLayout());
		headerPanel.add(headerLabel, BorderLayout.CENTER);
		headerPanel.setVisible(true);
		
		
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = .5;
		constraints.weighty = .1;
		mainFrame.add(headerPanel, constraints);
		
		if(startup == true) {
			//Display ConnectView
			ConnectView connectview = new ConnectView(this, mainFrame);
			currentPanel = connectview;	
			startup = false;
		}

		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = .5;
		constraints.weighty = .9;
		mainFrame.add(currentPanel, constraints);

		System.out.println(currentPanel.toString());
		mainFrame.setVisible(true);
	}
	
	public void setCurrentPanel(Panel newPanel) {
		currentPanel = newPanel;
	}

}
