package tlssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;

public class Main {

    public static void main(String[] args) throws CertificateException {

        BufferedReader input  = new BufferedReader(new InputStreamReader(System.in));

        String line = "";

        System.out.println("Entrez le nom de l'équipement : ");
        try
        {
            line = input.readLine();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        Equipement equipement = new Equipement(line, 6666);

        String help = "Liste des commandes :\n" +
                "i => Informations concernant l'équipement\n" +
                "r => Liste des équipements de RD\n" +
                "u => Liste des équipements de UT\n" +
                "s => Initialisation de l'insertion (en tant que serveur)\n" +
                "c => Initialisation de l'insertion (en tant que client)\n" +
                "h => Afficher cette aide\n" +
                "q => Quitter";
        System.out.println(help);


        while (!line.equals("q"))
        {
            try
            {
                line = input.readLine();
            }
            catch(IOException i)
            {
                System.out.println(i);
            }

            switch (line) {

                case "i":
                    System.out.println(equipement.monNom());
                    System.out.println(equipement.maClePub());
                    System.out.println(equipement.monCertif().getX509Certificate());
                    break;

                case "r":
                    equipement.affichage_ca();
                    break;

                case "u":
                    equipement.affichage_da();
                    break;

                case "s":
                    equipement.startServer();
                    break;

                case "c":
                    equipement.startClient();
                    break;

                case "q":
                    break;

                default:
                    System.out.println(help);
                    break;

                }

        }

    }
}
