package com.endless.tools;

import java.math.BigInteger;
import java.util.Random;
import java.io.*;

// com.endless.tools.RSA Implementation
// Generate the keys and contain the function to
// encrypt and decrypt data with specified keys
public class RSA {

    // Public and private keys
    public class Key {
      public BigInteger N;
      public BigInteger e;
      public BigInteger d;
    }

    public Key key;

     // Default constructor generate the keys
     public RSA() {
        BigInteger p;
        BigInteger q;
        BigInteger phi;
        int bitlength = 1024;
        Random r;

        key = new Key();
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        key.N = p.multiply(q);

        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        key.e = BigInteger.probablePrime(bitlength/2, r);

        while (phi.gcd(key.e).compareTo(BigInteger.ONE) > 0 && key.e.compareTo(phi) < 0 ) {
            key.e.add(BigInteger.ONE);
        }
        key.d = key.e.modInverse(phi);
    }

    public RSA(BigInteger e, BigInteger d, BigInteger N) {
        key = new Key();
        this.key.e = e;
        this.key.d = d;
        this.key.N = N;
    }

    //  Encrypt a message
    public byte[] encrypt(byte[] message, BigInteger e, BigInteger N) {
           return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    // Decrypt a message
    public byte[] decrypt(byte[] message, BigInteger d, BigInteger N) {
           return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

    // Return a string with the byte message
    public static String bytesToString(byte[] message) {
         String out = "";
         for (byte b : message) {
             out += Byte.toString(b);
         }
         return out;
    }

    public static void main (String[] args) throws IOException {
        RSA rsa = new RSA();
        DataInputStream in = new DataInputStream(System.in);
        System.out.println("Message to encrypt: ");
        String message = in.readLine();
        byte[] byteMessage = message.getBytes();

        byte[] encrypted = rsa.encrypt(byteMessage, rsa.key.e, rsa.key.N);
        System.out.println("Encrypted message (Bytes): " + bytesToString(encrypted));

        byte[] decrypted = rsa.decrypt(encrypted, rsa.key.d, rsa.key.N);
        System.out.println("Decrypted message (Bytes): " +  bytesToString(decrypted));
        System.out.println("Decrypted message (String): " + new String(decrypted));
    }
}
