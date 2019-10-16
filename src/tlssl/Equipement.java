package tlssl;

import java.security.cert.Certificate;
import java.security.PublicKey;

public class Equipement {
    private PaireClesRSA maCle; // La paire de cle de l’equipement.
    private Certificat monCert; // Le certificat auto-signe.
    private String monNom; // Identite de l’equipement.
    private int monPort; // Le numéro de port d’ecoute.

    Equipement (String nom, int port) throws Exception {
        monNom = nom;
        maCle = new PaireClesRSA();
        monCert = new Certificat(monNom, maCle, 365);
    }

    public void affichage_da() {
        // Affichage de la liste des équipements de DA.
    }

    public void affichage_ca() {
        // Affichage de la liste des équipements de CA.
    }

    public void affichage() {
        // Affichage de l’ensemble des informations // de l’équipement.
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
}
