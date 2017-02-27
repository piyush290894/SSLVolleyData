package com.example.user.volleydata;

import android.util.Base64;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

/**
 * Created by User on 27-02-2017.
 */
public class Encrypt {
    private String encodedString=null;
    private String decodedString=null;

    public Encrypt(String encodedString, String decodedString){
        this.encodedString=encodedString;
        this.decodedString=decodedString;
    }

    public String getEncodedString(){
        return encodedString;
    }
    public String getDecodedString() {
        return decodedString;
    }

    public static Encrypt RSA(String string){
        Key publicKey=null;
        Key privateKey=null;

        try {
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair kp=keyPairGenerator.genKeyPair();
            publicKey=kp.getPublic();
            privateKey=kp.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedBytes=null;
        try{
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            encodedBytes=cipher.doFinal(string.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        String encodedString= Base64.encodeToString(encodedBytes,Base64.DEFAULT);

        byte[] decodedBytes=null;
        try{
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,publicKey);
            decodedBytes=cipher.doFinal(encodedBytes);
        }catch (Exception e){
            e.printStackTrace();
        }

        String decodedString=new String(decodedBytes);
        return new Encrypt(encodedString,decodedString);
    }
}
