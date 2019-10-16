package tlssl;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class Triplet {
    public int id;
    public PublicKey pubKey;
    public X509Certificate cert;

    Triplet (int id, PublicKey pubKey, X509Certificate cert){
        this.id = id;
        this.pubKey = pubKey;
        this.cert = cert;
    }
}
