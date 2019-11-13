package tlssl;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;


public class Certificat {
    static private BigInteger seqnum = BigInteger.ZERO;
    public X509CertificateHolder x509CertificateHolder;

    Certificat(String missuer, String msubject, PrivateKey privateKey, PublicKey publicKey, int validityDays) {
        // On cree la structure qui va contenir la signature :
        try {
            Security.addProvider(new BouncyCastleProvider());
            ContentSigner sigGen = new
                    JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privateKey);


            // On cree la structure qui contient la cle publique a certifier
            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());

            // Nom du proprio et certifieur : (ici les mêmes car auto signé)
            X500Name issuer = new X500Name("CN=" + missuer);
            X500Name subject = new X500Name("CN=" + msubject);

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
            x509CertificateHolder = v1CertGen.build(sigGen);

        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
    }

    public X509Certificate getX509Certificate() throws CertificateException {
        // Transformer le x509holder en certificat :
        return new JcaX509CertificateConverter()
                .setProvider("BC").getCertificate(x509CertificateHolder);
    }

    public boolean verifCertif(PublicKey pubKey) throws OperatorCreationException, CertException {
        ContentVerifierProvider verifier =
                new JcaContentVerifierProviderBuilder().setProvider("BC").build(pubKey);

        // Verification d’un certificat !
        if (!x509CertificateHolder.isSignatureValid(verifier)) {
            System.err.println("signature invalid");
        }
        return x509CertificateHolder.isSignatureValid(verifier);
    }
}
