package controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Ftp {
	private FTPClient ftp;
	
	private String host;
	private String username;
	private String password;
	
	private String workingDirectory;
	private boolean connected = false;
	
	//Connects to FTP when create
	public Ftp(String host, String username, String password){
		ftp = new FTPClient();
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	//Used to connect to Ftp 
	public void connect() {    
		try {
	        ftp.connect(host);
	        ftp.enterLocalPassiveMode();
	        ftp.login(username, password);
	        
	        int reply = ftp.getReplyCode();

	        if(!FTPReply.isPositiveCompletion(reply)) {
	          ftp.disconnect();
	        } else {
	        	workingDirectory = ftp.printWorkingDirectory();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	connected = true;
	        }
	        System.out.println("Connected.");
		} catch(IOException io) {
			io.getStackTrace();
		} 
	}
	
	//Change working directory
	public void changeWorkingDirectory(String newDirectory) {
		try {
			ftp.changeWorkingDirectory(newDirectory);
			workingDirectory = ftp.printWorkingDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Used to return list of files.folders on ftp site current directory
	public String[] getListing() {
		String[] listing = new String[0];
		FTPFile[] filesInDirectory;
		
		if(getConnected()) {
			try {
				filesInDirectory = ftp.listFiles("");
				listing = new String[filesInDirectory.length];
				
				for(int i = 0; i < filesInDirectory.length; i++) {
					listing[i] = filesInDirectory[i].getName();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listing;
	}
	
	//
	public boolean upload(String absolutePathToFile, String fileName) {
		try {	
			
			FileInputStream uploadFile = new FileInputStream(absolutePathToFile+fileName);
			boolean reply = ftp.storeFile(fileName, uploadFile);
			uploadFile.close();
			
			return reply;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//
	public boolean download(String localDownloadPath, String fileName) {
		try {	
			File downloadFile = new File(localDownloadPath);
			FileOutputStream downloadStream = new FileOutputStream(downloadFile);
			boolean reply = ftp.retrieveFile(fileName, downloadStream);
			downloadStream.close();
			
			return reply;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Deletes a file from the server
	public boolean deleteFile(String pathName) {
		try {
			if(ftp.deleteFile(pathName)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	//GETTERS
	public boolean getConnected() {
		return connected;
	}
	
	public String getWorkingDirectory() {
		return workingDirectory;
	}
	
}
