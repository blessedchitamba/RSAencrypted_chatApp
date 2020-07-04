package rsa;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

public class RSATest {
    //private static RSA rsa;


    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        RSA rsa = new RSA();
        Scanner in = new Scanner(System.in);
        String teststring;
        System.out.println("Enter the plain text:");
        teststring = in.nextLine();
        System.out.println("Encrypting String: " + teststring);
        System.out.println("String in Bytes: "
                + rsa.bytesToString(teststring.getBytes()));
        // encrypt
        byte[] encrypted = rsa.encrypt(teststring.getBytes());
        // decrypt
        byte[] decrypted = rsa.decrypt(encrypted);
        System.out.println("Decrypting Bytes: " + rsa.bytesToString(teststring.getBytes()));
        System.out.println("Decrypted String: " + new String(decrypted));

        //write public keys to file
        rsa.writePublicKeysToFile("testPublicKey.txt");

        //read them from file
        rsa.readPublicKeys("testPublicKey.txt");
    }
}
