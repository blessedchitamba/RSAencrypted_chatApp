package rsa;

import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class RSATest {
    //private static RSA rsa;
    private RSA rsa;
    private final String testMessage = "This is a test string to be encrypted and decrypted.";

    @Before
    public void init() throws IOException {
        rsa = new RSA();
        rsa.writePublicKeysToFile("testKeys.txt");
        rsa.readPublicKeys("testKeys.txt");
    }

    @Test
    public void testMakeKeys(){
        //assert if p and q are prime
        assertEquals(true, rsa.p.isProbablePrime(1), "P must be prime!");
        assertEquals(true, rsa.getQ().isProbablePrime(1), "Q must be prime!");

        //assert if e>1 and < (p-1)(q-1)
        BigInteger pMinusOne = rsa.p.subtract(new BigInteger("1"));
        BigInteger qMinusOne = rsa.getQ().subtract(new BigInteger("1"));
        assertEquals(1, rsa.getE().compareTo(new BigInteger("1")), "E must be greater than 1!");
        assertEquals(-1,rsa.getE().compareTo(pMinusOne.multiply(qMinusOne)), "E must be < (p-1)(q-1)");

    }

    @Test
    public void testEncryption() {
        //encrypt the string
        byte [] encrypted = rsa.encrypt(testMessage.getBytes());

        //test this by testing that the encrypted byte array is not equal to the original message byte array
        try {
            assertArrayEquals(encrypted, testMessage.getBytes());
        } catch (AssertionError e) {
            return;
        }
        fail("The arrays are equal");
    }

    @Test
    public void testDecryption(){
        //encrypt and decrypt the string
        byte [] encrypted = rsa.encrypt(testMessage.getBytes());
        byte [] decrypted = rsa.decrypt(encrypted);

        assertEquals(testMessage, new String(decrypted), "The decrypted and original strings must be equal!");
    }

    /**
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        RSA rsa = new RSA();
        rsa.writePublicKeysToFile("testKeys.txt");
        rsa.readPublicKeys("testKeys.txt");
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
     */
}
