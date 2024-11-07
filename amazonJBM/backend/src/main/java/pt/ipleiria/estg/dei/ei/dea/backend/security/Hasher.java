package pt.ipleiria.estg.dei.ei.dea.backend.security;

import jakarta.enterprise.context.ApplicationScoped;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@ApplicationScoped
public class Hasher {
    public static String hash(String content) {
        try {
            ByteBuffer passwdBuffer =
                    Charset.defaultCharset().encode(CharBuffer.wrap(content));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("SHA-256");
            mdEnc.update(passwdBytes, 0, content.toCharArray().length);
            char[] encoded = new BigInteger(1,
                    mdEnc.digest()).toString(16).toCharArray();
            return new String(encoded);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utilizador.class.getName()).severe(ex.getMessage());
            return "";
        }
    }
}
