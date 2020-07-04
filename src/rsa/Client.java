package rsa;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Client implements Runnable{
	static volatile DataInputStream in  = null;
	static String request = "";
	static Socket socket;
	//static PrintWriter out;
	static DataOutputStream out;
	static volatile boolean flag = false;
	static Object lock = new Object();
	static Object lock2 = new Object();
	static RSA clientKeys;

    // main method does writing in one thread
	public static void main(String[] args) throws IOException {
		//create the keys first
		clientKeys = new RSA();
		clientKeys.writePublicKeysToFile("clientPublicKeys.txt");
		System.out.println("RSA Keys generated and written to file.");

		//connect to server
		try{
			//the server's socket
			socket = new Socket("LocalHost", 1342);
			out = new DataOutputStream(socket.getOutputStream());
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your name");
			String name = sc.nextLine();
			in = new DataInputStream(socket.getInputStream());
			System.out.println("Type a message");
			String message;
			byte[] encryptedMsg;
			String toBeSent;
			boolean quit = false;
            // new thread to read messages
			new Thread(new Client()).start();
            while (!quit){ // user quits on client side
				message = sc.nextLine();

				//check if message is quit and update variable
				if(message.equals("quit")){
					quit = true;
				}
				//encrypt the message with the client private key
				encryptedMsg = clientKeys.encrypt(message.getBytes());
				System.out.println("Encrypted message in Bytes: "
						+ clientKeys.bytesToString(encryptedMsg));
				//toBeSent = new String(encryptedMsg);
				out.writeInt(encryptedMsg.length);
				out.write(encryptedMsg);
				//out.write(String.valueOf(encryptedMsg));
      		}
			//out.println("quit");
            out.close();
			socket.close();

		}
		catch (UnknownHostException e){
    		System.out.println("Hostname error");
    	}
		catch (IOException e){
    		System.out.println("IO error");
    	}
	}

        @Override
	public void run(){
		try{
			//read messages sent from the server
			//get the server public key
			clientKeys.readPublicKeys("serverPublicKeys.txt");
			byte[] cipherText;
			String plaintext;
			while(true){
				int length = in.readInt();                    // read length of incoming message
				cipherText = new byte[length];
				in.readFully(cipherText, 0, length); // read the message
				//decrypt the message with client public key
				cipherText = clientKeys.decrypt(cipherText);
				plaintext = new String(cipherText);
				System.out.println("Server:\n"+plaintext+'\n');
			}
		}
		catch(Exception e){

		}

	}

}
