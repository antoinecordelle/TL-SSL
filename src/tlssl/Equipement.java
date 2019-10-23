package tlssl;

import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class Equipement {
    private PaireClesRSA maCle; // La paire de cle de l’equipement.
    private Certificat monCert; // Le certificat auto-signe.
    private String monNom; // Identite de l’equipement.
    private int monPort; // Le numéro de port d’ecoute.
    private ArrayList<Triplet> ca;
    private ArrayList<Triplet> da;

    Equipement (String nom, int port) throws Exception {
        monNom = nom;
        maCle = new PaireClesRSA();
        monCert = new Certificat(monNom, maCle.Privee(), maCle.Publique(), 365);
        ca = new ArrayList<Triplet>();
    }

    public void affichage_da() {
        for (Triplet i : da) {
            System.out.println(i.id);
            System.out.println(i.pubKey);
            System.out.println(i.cert);
        }
    }

    public void affichage_ca() {
        for (Triplet i : ca) {
            System.out.println(i.id);
            System.out.println(i.pubKey);
            System.out.println(i.cert);
        }
    }

    public void affichage() {
        System.out.println(monNom);
        System.out.println(monCert);
        affichage_ca();
        affichage_da();
    }

    public String monNom (){
        return monNom;
    }

    public PublicKey maClePub() {
        return maCle.Publique();
    }

    public Certificat monCertif() {
        return monCert;
    }

    public void certify(Equipement equipement) throws CertificateException {
        String nom = equipement.monNom();
        PublicKey pubKey = equipement.maClePub();
        Certificat cert = new Certificat(nom, maCle.Privee(), pubKey, 365);
        Triplet triplet = new Triplet(nom, pubKey, cert.getX509Certificate());
        ca.add(triplet);
    }
}
