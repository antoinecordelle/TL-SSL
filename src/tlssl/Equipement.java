package tlssl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class Equipement {
    private PaireClesRSA maCle; // La paire de cle de l’equipement.
    private Certificat monCert; // Le certificat auto-signe.
    private String monNom; // Identite de l’equipement.
    private int monPort; // Le numéro de port d’ecoute.
    private ArrayList<Triplet> ca;
    private ArrayList<Triplet> da;

    private Socket socket = null;

    Equipement (String nom, int port) {
        monNom = nom;
        monPort = port;
        maCle = new PaireClesRSA();
        monCert = new Certificat(monNom, maCle.Privee(), maCle.Publique(), 365);
        ca = new ArrayList<Triplet>();
        da = new ArrayList<Triplet>();

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

    public void startServer() {
// starts server and waits for a connection
        try
        {
            ServerSocket serverSocket = new ServerSocket(monPort);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = serverSocket.accept();
            System.out.println("Client accepted");

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            String nom_c = (String) objectInputStream.readObject();

            System.out.println("Insérer l'équipement " + nom_c + " ? oui/non");

            BufferedReader input  = new BufferedReader(new InputStreamReader(System.in));
            String test = input.readLine();
            if (test.equals("oui"))
            {
                // Sending confirmation
                objectOutputStream.writeObject(true);

                PublicKey publicKey_c = (PublicKey) objectInputStream.readObject();

                Certificat cert_s = new Certificat(nom_c, maCle.Privee(), publicKey_c, 365);
                objectOutputStream.writeObject(cert_s.getX509Certificate());
                objectOutputStream.writeObject(monNom());
                objectOutputStream.writeObject(maClePub());

                X509Certificate cert_c = (X509Certificate) objectInputStream.readObject();
                // add certificate to CA
                Triplet triplet = new Triplet(nom_c, publicKey_c, cert_c);
                ca.add(triplet);

                ArrayList<Triplet> da_c = new ArrayList<Triplet>();
                da_c.addAll(ca);
                da_c.addAll(da);
                objectOutputStream.writeObject(da_c);
            }
            else
            {
                // Sending refusal
                objectOutputStream.writeObject(false);
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            serverSocket.close();
        }
        catch(IOException | ClassNotFoundException | CertificateException i)
        {
            System.out.println(i);
        }
    }

    public void startClient(){
        try {
            // establish a connection
            socket = new Socket("localhost", monPort);
            System.out.println("Connected");

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            System.out.println("Demande d'insertion...");
            objectOutputStream.writeObject(monNom());

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            boolean test = (boolean)objectInputStream.readObject();
            if (test)
            {
                objectOutputStream.writeObject(maClePub());

                X509Certificate cert_s = (X509Certificate) objectInputStream.readObject();
                String nom_s = (String) objectInputStream.readObject();
                PublicKey publicKey_s = (PublicKey) objectInputStream.readObject();
                System.out.println("Insertion terminée");

                // add certificate to CA
                Triplet triplet = new Triplet(nom_s, publicKey_s, cert_s);
                ca.add(triplet);

                Certificat cert_c = new Certificat(nom_s, maCle.Privee(), publicKey_s, 365);
                objectOutputStream.writeObject(cert_c.getX509Certificate());

                da = (ArrayList<Triplet>) objectInputStream.readObject();
            }
            else
            {
                System.out.println("Insertion refusée");
            }

            // close the connection
            socket.close();
            System.out.println("Closing connection");
        }
        catch(IOException | ClassNotFoundException | CertificateException i)
        {
            System.out.println(i);
        }

    }
}
