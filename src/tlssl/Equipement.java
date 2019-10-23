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

            // takes input from the client socket
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            X509Certificate cert = (X509Certificate) objectInputStream.readObject();
            System.out.println(cert);

            System.out.println("Closing connection");

            // close connection
            socket.close();
        }
        catch(IOException | ClassNotFoundException i)
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

            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(monCert.getX509Certificate());

            // close the connection
            socket.close();
        }
        catch(IOException | CertificateException i)
        {
            System.out.println(i);
        }

    }
}
