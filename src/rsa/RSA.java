/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Room-1
 */
public class RSA {

    /**
     * Integer values for the sender public and private keys etc.
     */
    public BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int        bitlength = 1024;
    private Random     r;

    //N and e values for the recipient Public Key
    public BigInteger Nrecipient;
    public BigInteger eRecipient;

    /**
     *
     * No argument constructor. Call this constructor and
     */
    public RSA()
    {
        makeKeys();
    }

    /**
     * Getters and setters
     */
    public BigInteger getQ(){
        return q;
    }

    public BigInteger getE(){
        return e;
    }

    /**
     * Make keys method to create the big integers needed for keys
     */
    public void makeKeys(){
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
        /**
        System.out.println("-prime numbers, public key and private key-");
        System.out.println("--p: " + String.valueOf(p));
        System.out.println("--q: " + String.valueOf(q));
        System.out.println("--N: " + String.valueOf(N));
        //System.out.println("phi: " + String.valueOf(phi));
        System.out.println("--e: " + String.valueOf(e));
        System.out.println("--d: " + String.valueOf(d));
         */
    }

    //method to write public key ints N and e to file
    synchronized void writePublicKeysToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(String.valueOf(N)+'\n');
        writer.write(String.valueOf(e)+'\n');
        writer.close();
    }

    //method to read public key components from recipient file
    synchronized void readPublicKeys(String filename) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            this.Nrecipient = new BigInteger(reader.readLine());
            this.eRecipient = new BigInteger(reader.readLine());
            reader.close();
    }
    
    public static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }
    
    // Encrypt message using Private Key (d, N)
    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
 
    // Decrypt message using Public Key (e, N)
    public byte[] decrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(eRecipient, Nrecipient).toByteArray();
    }
}
