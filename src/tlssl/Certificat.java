package tlssl;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;


public class Certificat {
    static private BigInteger seqnum = BigInteger.ZERO;
    public X509Certificate x509;

    Certificat(String nom, PaireClesRSA cle, int validityDays) {
        PublicKey publicKey = cle.Publique();
        PrivateKey privateKey = cle.Privee();

        // On cree la structure qui va contenir la signature :
        try {
            ContentSigner sigGen = new
                    JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privateKey);


            // On cree la structure qui contient la cle publique a certifier
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

            // Nom du proprio et certifieur : (ici les mêmes car auto signé)
            X500Name issuer = new X500Name("CN=" + nom);
            X500Name subject = new X500Name("CN=" + nom);

            // Numero de serie du futur certificat
            seqnum = seqnum.add(BigInteger.ONE);

            /// Date de validité :
            // Début :
            Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            // Fin :
            Date endDate = new Date(System.currentTimeMillis() + validityDays * 24 * 60 * 60 * 1000);

            // Creer la structure qui permet de creer le certificat :
            X509v1CertificateBuilder v1CertGen = new X509v1CertificateBuilder(
                    issuer, seqnum, startDate, endDate, subject, subjectPublicKeyInfo);

            // Calculer la signature et creer un certificat :
            X509CertificateHolder x509CertificateHolder = v1CertGen.build(sigGen);

            // Transformer le x509holder en certificat :
            try {
                X509Certificate x509 = new JcaX509CertificateConverter()
                        .setProvider("BC").getCertificate(x509CertificateHolder);
            } catch (CertificateException e) {
                e.printStackTrace();
            }

        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
    }

    public boolean verifCertif(PublicKey pubKey) throws OperatorCreationException {
        ContentVerifierProvider verifier =
                new JcaContentVerifierProviderBuilder().setProvider("BC").build(pubKey);
        // Verification d’un certificat !
        if (!x509.isSignatureValid(verifier)) {
            System.err.println("signature invalid");
        }
        return x509.isSignatureValid(verifier);
    }
}
