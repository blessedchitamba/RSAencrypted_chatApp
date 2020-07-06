package rsa;

import java.io.*;
import java.net.*;
import java.util.*;

public class clientHandler extends Thread{
	private Socket clientSocket;
	//static ArrayList<Socket> socketList = new ArrayList<Socket>();
	static ArrayList<Socket> imageResponse = new ArrayList<Socket>();
	static Object lock = new Object();
	private Socket socket;
    static String filename;
	public RSA serverKeys;
  
	//constructor that takes a client socket
	public clientHandler(Socket socket) throws IOException {
		this.clientSocket = socket;
		serverKeys = new RSA();
		serverKeys.writePublicKeysToFile("serverPublicKeys.txt");
		//since the client socket object already exists, we know the keys file exists
		serverKeys.readPublicKeys("clientPublicKeys.txt");
	}

	// thread to send messages to client
	Thread sendMessage = new Thread(new Runnable()
	{
		@Override
		public void run() {
			final Scanner scn = new Scanner(System.in);
			byte[] encryptedMsg;
			try {
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				while (true) {
					// read the message to deliver.
					String msg = scn.nextLine();

					//check if message is quit and update variable
					if(msg.equals("quit")){
						break;
					}

					//encrypt the message with the server private key
					encryptedMsg = serverKeys.encrypt(msg.getBytes());
					System.out.println("Encrypted message in Bytes: "
							+ serverKeys.bytesToString(encryptedMsg));
					//toBeSent = new String(encryptedMsg);
					out.writeInt(encryptedMsg.length);
					out.write(encryptedMsg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	});

	public void run(){
		//start the send message thread
		sendMessage.start();
		boolean quit;

		try{
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			byte[] cipherText;
			String encrypted;
			String plaintext;
			quit = false;
			while(true){
				//encrypted = in.readLine();
				int length = in.readInt();                    // read length of incoming message
				cipherText = new byte[length];
				in.readFully(cipherText, 0, length); // read the message

				//decrypt the message with client public key
				cipherText = serverKeys.decrypt(cipherText);
				plaintext = new String(cipherText);
				//plaintext = serverKeys.bytesToString(cipherText);
        		if (plaintext == null || plaintext.equals("quit")){
        			break;
        		}
        		//print received message to server screen
        		System.out.println("Client:\n"+plaintext+'\n');
			}
			System.out.println("Client has left...");
			in.close();
			clientSocket.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

}
