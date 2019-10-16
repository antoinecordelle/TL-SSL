package tlssl;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.X509Certificate;


public class Certificat {
    static private BigInteger segnum = BigInteger.ZERO;
    public X509Certificate x509;

    Certificat(String nom, PaireClesRSA cle, int validityDays) {

    }

    public boolean verifCertif(PublicKey pubKey) {

    }
}
