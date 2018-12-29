package views;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import controllers.Ftp;

public class MainView extends Panel{
	Frame mainFrame;
	
	private Ftp ftp;
	private String selectedDestination = new String("");
	private String selectedRelativePath = new String(".");
	private String selectedFileDirectory = new String();
	private String selectedFileName = new String();
	private Label ftpSelectedFile = new Label("Selected Destination: Current Directory");
	private List clientFilesList = new List(22);	
	private List ftpList = new List(22);
	private FileDialog fileDialog = new FileDialog(mainFrame);
	
	public MainView(MainWindow window, Frame mainFrame, Ftp ftp) {
		this.ftp = ftp;
		this.mainFrame = mainFrame;
		
		GridLayout layout = new GridLayout(1,2);
		setLayout(layout);
	
		
		/**
		 * LEFT PANEL COMPONENTS
		 * **CLIENT SIDE**
		 * **/
		Panel leftPanel = new Panel();
		GridBagLayout leftPanelLayout = new GridBagLayout();
		leftPanel.setLayout(leftPanelLayout);
		
		GridBagConstraints leftConstraints = new GridBagConstraints();
		leftConstraints.gridx = 0;
		leftConstraints.gridy = 0;
		leftConstraints.gridwidth = 1;
		leftConstraints.weightx = .5;
		leftConstraints.fill = GridBagConstraints.HORIZONTAL;
		

		Button openButton = new Button("Find File");
		Label selectedClientFileLabel = new Label("Selected File:");
		Button uploadButton = new Button("Upload");	
		Button downloadButton = new Button("Download");	
		Button deleteButton = new Button("Delete File");	


		//Opens Dialog to select a specified file
		openButton.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   
				   fileDialog.setVisible(true);
				   
				   if(!(fileDialog.getFile().isEmpty()) && !(fileDialog.getDirectory().isEmpty())){
					   selectedFileDirectory = fileDialog.getDirectory();
					   selectedFileName = fileDialog.getFile();
					   
					   if(!(selectedFileName.isEmpty()) && !(selectedFileDirectory.isEmpty())) {
						   selectedClientFileLabel.setText("Selected File:" + selectedFileName);
						   updateClientList(selectedFileDirectory);
					   }
				   }
			   }
		});
		leftPanel.add(openButton, leftConstraints);
		
		
		
		//Upload File Button
		leftConstraints.gridx = 1;
		leftConstraints.gridy = 0;
		uploadButton.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				  if(selectedFileName.isEmpty()) {
					  selectedClientFileLabel.setText("You must select a file first.");
				  }
				  
				  
				  if(ftp.upload(selectedFileDirectory, selectedFileName)) {
					  ftpSelectedFile.setText("File Uploaded Successfully");
					  updateFtpList(selectedDestination);
				  } else {
					  ftpSelectedFile.setText("Upload Failed");
				  }
			   }
			   
		});		
		
		
		
		
		leftPanel.add(uploadButton, leftConstraints);
		
		//Selected Client File Label
		leftConstraints.gridx = 0;
		leftConstraints.gridy = 1;
		leftConstraints.gridwidth = 2;
		leftPanel.add(selectedClientFileLabel, leftConstraints);
		
		
		//Client Files List
		leftConstraints.gridx = 0;
		leftConstraints.gridy = 2;
		leftConstraints.gridwidth = 2;
		
		updateClientList("");
		selectedFileDirectory = "";
		clientFilesList.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   
				   String tmpRelativePath = selectedRelativePath + "/" +clientFilesList.getSelectedItem();
				   File testFile = new File(tmpRelativePath);
				   
				   if(testFile.isDirectory()) {
					   selectedRelativePath += "/" + clientFilesList.getSelectedItem();
					   updateClientList(selectedRelativePath);
					   selectedRelativePath = testFile.getAbsolutePath();
				   } else {
					  //set selected file 
					   selectedFileName = clientFilesList.getSelectedItem();
					   selectedFileDirectory = "/" + selectedRelativePath;
					   File newFile = new File(selectedFileDirectory);
					   selectedFileDirectory = newFile.getAbsolutePath()+'/';
					   System.out.println(newFile.getAbsolutePath()+'/'+selectedFileName);
					   selectedClientFileLabel.setText("Selected file:" + clientFilesList.getSelectedItem());
				   }
				  
			   }
		});
		
		leftPanel.add(clientFilesList, leftConstraints);
		
		
		/**
		 * RIGHT PANEL COMPONENTS
		 * **FTP SIDE**
		 * **/
		Panel rightPanel = new Panel();
		GridBagLayout rightPanelLayout = new GridBagLayout();
		rightPanel.setLayout(rightPanelLayout);
		
		
		//Destination Button
		GridBagConstraints rightConstraints = new GridBagConstraints();
		rightConstraints.gridx = 0;
		rightConstraints.gridy = 0;
		rightConstraints.gridwidth = 1;
		rightConstraints.weightx = 1;
		rightConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		downloadButton.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   if(selectedDestination.isEmpty()) {
					   ftpSelectedFile.setText("You must select a file to download first.");
				   }
				   
				   if(selectedFileDirectory.isEmpty()) {
					   ftpSelectedFile.setText("You must select a directory to download the file too.");
				   }
				   
				   if(ftp.download(selectedFileDirectory + "/" + selectedDestination, selectedDestination)) {
					   ftpSelectedFile.setText("Download successful!");
					   updateClientList(selectedFileDirectory);
				   } else {
					   ftpSelectedFile.setText("Download failed.");
				   }
			   }
		});
		
		rightPanel.add(downloadButton, rightConstraints);
		


		rightConstraints.gridx = 1;
		rightConstraints.gridy = 0;
		rightConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		deleteButton.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   if(selectedDestination.isEmpty()) {
					   ftpSelectedFile.setText("You must select a file to delete first.");
				   }
				   
				   System.out.println(selectedDestination);
				   
				  if(ftp.deleteFile(selectedDestination)) {
					   ftpSelectedFile.setText("Deleted successful!");
					   updateFtpList(selectedDestination);
				   } else {
					   ftpSelectedFile.setText("Delete failed.");
				   }
			   }
		});
		
		rightPanel.add(deleteButton, rightConstraints);
		
		
		//Ftp Selected File
		rightConstraints.gridx = 0;
		rightConstraints.gridy = 1;
		rightConstraints.gridwidth = 2;
		rightPanel.add(ftpSelectedFile, rightConstraints);
		
		//Ftp List
		//Populate List Field
		String ftpListing[] = ftp.getListing();
		for(int i = 0; i < ftpListing.length; i++) {
			ftpList.add(ftpListing[i]);
		}
		
		//FTP List 
		ftpList.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				  updateFtpList(ftpList.getSelectedItem());
			   }
		});
		rightConstraints.gridx = 0;
		rightConstraints.gridy = 2;
		rightConstraints.gridwidth = 2;
		rightPanel.add(ftpList, rightConstraints);
		
		
		add(leftPanel);
		add(rightPanel);
		setVisible(true);
	}
	
	//Removes all list items from ftpList and replaces them with the new directory's 
	public void updateFtpList(String newDirectory) {
		ftpList.removeAll();
		selectedDestination = newDirectory;
		ftpSelectedFile.setText("Selected Destination: " + selectedDestination);
		ftp.changeWorkingDirectory(newDirectory);
		String ftpListing[] = ftp.getListing();
		for(int i = 0; i < ftpListing.length; i++) {
			ftpList.add(ftpListing[i]);
		}
	}
	
	
	public void updateClientList(String currentClientDirectory) {
		clientFilesList.removeAll();
		
		//Populate the client folder list
		File folder = new File(currentClientDirectory);
		
		//System.out.println(folder.getAbsolutePath());
		folder = new File(folder.getAbsolutePath());
		File[] listOfFiles = folder.listFiles();
		
		//Add current and Parent directories
		clientFilesList.add(".");
		clientFilesList.add("..");
		
		for(int i = 0; i < listOfFiles.length; i++) {
			clientFilesList.add(listOfFiles[i].getName());
		}
		
	}
	
}
