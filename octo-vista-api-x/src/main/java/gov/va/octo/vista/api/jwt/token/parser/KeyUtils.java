package gov.va.octo.vista.api.jwt.token.parser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utitlity to return java.security.PrivateKey or java.security.PublicKey
 * 
 * Private keys: String passed in should be in a private key in pkcs#8 format. This can be done with
 * the openssl command: openssl pkcs8 -topk8 -nocrypt -in $KEY_NAME -out $KEY_NAME.pk8
 * 
 * Public keys: String passed in should be in DER format and Base64 encoded. This can be done with
 * the openssl commands: openssl rsa -in $KEY_NAME -pubout -outform DER | base64 >
 * $KEY_NAME.pub.der.b64
 * 
 * 
 * @author william.mccarty@va.gov
 *
 */
public class KeyUtils {

    public static PublicKey publicKey(String str) throws GeneralSecurityException, IOException {

        String local = str.replace("\r", "").replace("\n", "");
        byte[] data = Base64.getDecoder().decode((local.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

    }

    public static PrivateKey privateKey(String str) throws GeneralSecurityException, IOException {

        String local = str.replace("\r", "").replace("\n", "");
        byte[] data = Base64.getDecoder().decode(local.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(data, (byte) 0);
        return priv;

    }

}
