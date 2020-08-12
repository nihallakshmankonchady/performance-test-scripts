package io.mosip.registrationProcessor.perf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Component
public class SSHUtil {

	private String host;
	private Integer port;
	private String user;
	private String password;
	private String keyFile;

	private JSch jsch;
	// private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;
	private String remoteDir;

	public SSHUtil() {
		host = "13.71.92.43"; // public IP
		port = 22;
		user = "madmin";
		keyFile = "C:\\Putty\\Mosip_Private_key.ppk";
		remoteDir = "/home/ftp1/LANDING_ZONE";
		remoteDir = "/home/ftp1/test";
	}

	public Session connect() throws Exception {
		System.out.println("connecting..." + host);
		try {
			jsch = new JSch();
			jsch.addIdentity(keyFile);
			Session session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			// session.setPassword(password);

			session.connect();
			return session;

		} catch (JSchException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public void openSFTPChannel(Session session) {
		// connect();
		System.out.println("connecting..." + host + " using SFTP");
		try {

			channel = session.openChannel("sftp");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
			System.out.println("SFTP channel opened");

		} catch (JSchException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	public void executeCommand(Session session, String command) {
		// connect();
		try {
			System.out.println("connecting..." + host + " using exec");
			Channel channel;
			channel = session.openChannel("exec");

			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream input = channel.getInputStream();
			channel.connect();
			System.out.println("Channel Connected to machine " + host + " server with command: " + command);

			try {
				InputStreamReader inputReader = new InputStreamReader(input);
				BufferedReader bufferedReader = new BufferedReader(inputReader);
				String line = null;
				System.out.println("Output of the command " + command + " is:");
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
				}
				bufferedReader.close();
				inputReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

//			channel.disconnect();
//			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void uploadFile(String localFilepath) {

		FileInputStream fis = null;
		// connect();
		try {
			// Change to output directory
			sftpChannel.cd(remoteDir);

			// Upload file
			File file = new File(localFilepath);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());

			fis.close();
			System.out.println("File uploaded successfully - " + file.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		// disconnect();

	}

	public void disconnect(Session session) {
		System.out.println("disconnecting...");
		sftpChannel.disconnect();
		channel.disconnect();
		session.disconnect();
	}

}
