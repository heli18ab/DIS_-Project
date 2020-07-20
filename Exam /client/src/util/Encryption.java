package util;

/**
 * A class representing encryption in the system
 *
 */
public final class Encryption {

    public static String encryptDecrypt(String rawString) {

        //encryptionkey
        String keyString = "XEHIJKLMONIN";
        boolean encryption = true;

        if(encryption){

         char[] key = keyString.toCharArray();


            StringBuilder thisIsEncrypted = new StringBuilder();
            for (int i = 0; i < rawString.length(); i++) {
                thisIsEncrypted.append((char) (rawString.charAt(i) ^ key[i % key.length]));
            }


            // We return the encrypted string
            return thisIsEncrypted.toString();
        }
        else {
            return rawString;        }
    }
}


