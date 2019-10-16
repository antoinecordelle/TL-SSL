package tlssl;

import java.security.*;

public class PaireClesRSA {
    private KeyPair key;

    PaireClesRSA() {
        SecureRandom rand = new SecureRandom();
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpg.initialize(512, rand);
        this.key = kpg.generateKeyPair();
    }

    public PublicKey Publique() {
        return this.key.getPublic();
    }

    public PrivateKey Privee() {
        return this.key.getPrivate();
    }
}
