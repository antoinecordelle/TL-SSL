package tlssl;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class Triplet implements java.io.Serializable {
    public String id;
    public PublicKey pubKey;
    public X509Certificate cert;

    Triplet (String id, PublicKey pubKey, X509Certificate cert){
        this.id = id;
        this.pubKey = pubKey;
        this.cert = cert;
    }
}
