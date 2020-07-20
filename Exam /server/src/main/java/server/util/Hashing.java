package server.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    private String salt;
    private static MessageDigest digester;



    public void setSalt(String salt) {
        this.salt = salt;
    }


    /**
     * Hash string AND salt with SHA256 hash
     * @param str input string
     * @return  hashed of string
     */
    public String hashWithSalt(String str){
        String salt = Config.getSALT();
        String hashed = str + salt;
        return Hashing.sha(hashed);
    }


    /**
     * Performing SHA256 hashing of string
     * @return  hash of string
     */

    public static String sha(String rawString){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashing = messageDigest.digest(rawString.getBytes(StandardCharsets.UTF_8));
            String sha256 = new String(HexBin.encode(hashing));
            return sha256;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return rawString;
    }



}
