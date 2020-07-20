package server.util;

public final class Encryption {

    public static String encryptDecrypt(String rawString) {



        /**
         * Runs the encryption. Gets the key from config. .
         *
         * @return a rawString.
         */
        if(Config.getENCRYPTION()){


            char[] key = Config.getEncryptionKey();


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