package com.polarisbanklimited.ng.fileExtractor;

//import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

public class SFTPExtraction {

	public static void main(String[] args)throws Exception 
	{
		
		SSHClient sshClient = setupSshj();
	    SFTPClient sftpClient = sshClient.newSFTPClient();
	    List <RemoteResourceInfo> allFilesNDir = sftpClient.ls(".");
	    ResourceBundle config = ResourceBundle.getBundle("application");
	    String dest = config.getString("file.dest.dir");
	    
	    for(int i = 0; i < allFilesNDir.size(); i++)
	    {
	    	RemoteResourceInfo fileInfo = allFilesNDir.get(i);
	    	if(allFilesNDir.get(i).isRegularFile())
	    	{
	    		System.out.println("Scr File Path: " + fileInfo.getPath());
	    		System.out.println("Dest File Path: " +  dest + " /" + fileInfo.getName());
	    		sftpClient.get(fileInfo.getPath(),  dest);
	    	}
	    }
	 
		sftpClient.close();
	}
	
	
	private static SSHClient setupSshj() throws Exception {
	    
		ResourceBundle config = ResourceBundle.getBundle("application");
		String remoteHost = config.getString("remoteHost");
		String username = config.getString("SFTP.user");
		String password = config.getString("SFTP.pass");
		SSHClient client = new SSHClient();
	    client.addHostKeyVerifier(new PromiscuousVerifier());
	    client.connect(remoteHost);
	    client.authPassword(username, password);
	    return client;
	}

}
