package tlssl;

import java.math.BigInteger;
import java.security.PublicKey;

public class Certificat {
    static private BigInteger segnum = BigInteger.ZERO;
    public x509Certificate x509;

    Certificat(String nom, PaireClesRSA cle, int validityDays) {
        //
    }

    public boolean verifCertif(PublicKey pubKey) {

    }
}
