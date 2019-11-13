package tlssl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;

public class Equipement {
    private PaireClesRSA maCle; // La paire de cle de l’equipement.
    private Certificat monCert; // Le certificat auto-signe.
    private String monNom; // Identite de l’equipement.
    private int monPort; // Le numéro de port d’ecoute.
    private HashSet<Triplet> ca;
    private HashSet<Triplet> da;

    private Socket socket = null;

    Equipement(String nom, int port) {
        monNom = nom;
        monPort = port;
        maCle = new PaireClesRSA();
        monCert = new Certificat(monNom, maCle.Privee(), maCle.Publique(), 365);
        ca = new HashSet<Triplet>();
        da = new HashSet<Triplet>();

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

    public String monNom() {
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
        try {
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

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String test = input.readLine();
            if (test.equals("oui")) {
                // Sending confirmation
                objectOutputStream.writeObject(true);
                objectOutputStream.writeObject(monNom());

                boolean test2 = (boolean) objectInputStream.readObject();
                if (test2) {
                    X509Certificate cert_auto_c = (X509Certificate) objectInputStream.readObject();
                    PublicKey publicKey_c = cert_auto_c.getPublicKey();

                    Certificat cert_s = new Certificat(nom_c, maCle.Privee(), publicKey_c, 365);
                    objectOutputStream.writeObject(cert_s.getX509Certificate());
                    objectOutputStream.writeObject(monCert.getX509Certificate());

                    X509Certificate cert_c = (X509Certificate) objectInputStream.readObject();

                    // verify certificate
                    cert_c.verify(publicKey_c);

                    // add certificate to CA
                    Triplet triplet = new Triplet(nom_c, publicKey_c, cert_c);
                    ca.add(triplet);

                    HashSet<Triplet> da_c = new HashSet<Triplet>();
                    da_c.addAll(ca);
                    da_c.addAll(da);
                    objectOutputStream.writeObject(da_c);
                    System.out.println("Insertion terminée");
                }
                else {
                    System.out.println("Insertion refusée");
                }
            } else {
                // Sending refusal
                objectOutputStream.writeObject(false);
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException | CertificateException i) {
            System.out.println(i);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void startClient() {
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

            boolean test = (boolean) objectInputStream.readObject();
            if (test) {

                String nom_s = (String) objectInputStream.readObject();
                System.out.println("S'insérer sur le réseau de l'équipement " + nom_s + " ? oui/non");

                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String test2 = input.readLine();

                if (test2.equals("oui")) {
                    objectOutputStream.writeObject(true);

                    objectOutputStream.writeObject(monCert.getX509Certificate());

                    X509Certificate cert_s = (X509Certificate) objectInputStream.readObject();
                    X509Certificate cert_auto_s = (X509Certificate) objectInputStream.readObject();
                    PublicKey publicKey_s = cert_auto_s.getPublicKey();
                    System.out.println("Insertion terminée");

                    // verify certificate
                    cert_s.verify(publicKey_s);

                    // add certificate to CA
                    Triplet triplet = new Triplet(nom_s, publicKey_s, cert_s);
                    ca.add(triplet);

                    Certificat cert_c = new Certificat(nom_s, maCle.Privee(), publicKey_s, 365);
                    objectOutputStream.writeObject(cert_c.getX509Certificate());

                    da = (HashSet<Triplet>) objectInputStream.readObject();
                }
                else {
                    // Sending refusal
                    objectOutputStream.writeObject(false);
                }
            } else {
                System.out.println("Insertion refusée");
            }

            // close the connection
            socket.close();
            System.out.println("Closing connection");
        } catch (IOException | ClassNotFoundException | CertificateException i) {
            System.out.println(i);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
        }

    }

    public void synchronizeServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(monPort);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = serverSocket.accept();
            System.out.println("Client accepted");

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            X509Certificate cert_auto_c = (X509Certificate) objectInputStream.readObject();
            PublicKey publicKey_c = cert_auto_c.getPublicKey();

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(monCert.getX509Certificate());

            boolean isKnown = isKnownCertificate(cert_auto_c);
            objectOutputStream.writeObject(isKnown);
            isKnown = (boolean)objectInputStream.readObject() || isKnown;


            if (isKnown) {
                objectOutputStream.writeObject(ca);
                objectOutputStream.writeObject(da);

                HashSet<Triplet> ca_c = (HashSet<Triplet>) objectInputStream.readObject();
                HashSet<Triplet> da_c = (HashSet<Triplet>) objectInputStream.readObject();
                for (Triplet t : ca_c) {
                    t.cert.verify(t.pubKey);
                    if (!isInCAorDA(t)) {
                        da.add(t);
                    }
                }
                for (Triplet t : da_c) {
                    t.cert.verify(t.pubKey);
                    if (!isInCAorDA(t)) {
                        da.add(t);
                    }
                }
                System.out.println("Synchronisation done");
            }
            else {
                System.out.println("Unknown client");
            }
            socket.close();
            serverSocket.close();

        } catch (IOException | ClassNotFoundException | CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void synchronizeClient() {
        try {
            socket = new Socket("localhost", monPort);
            System.out.println("Connecté");

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(monCert.getX509Certificate());

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            X509Certificate cert_auto_s = (X509Certificate) objectInputStream.readObject();
            PublicKey publicKey_s = cert_auto_s.getPublicKey();


            boolean isKnown = (boolean)objectInputStream.readObject();
            isKnown = isKnownCertificate(cert_auto_s) || isKnown;
            objectOutputStream.writeObject(isKnown);

            if (isKnown) {
                HashSet<Triplet> ca_s = (HashSet<Triplet>) objectInputStream.readObject();
                HashSet<Triplet> da_s = (HashSet<Triplet>) objectInputStream.readObject();

                objectOutputStream.writeObject(ca);
                objectOutputStream.writeObject(da);

                for (Triplet t : ca_s) {
                    t.cert.verify(t.pubKey);
                    if (!isInCAorDA(t)) {
                        da.add(t);
                    }
                }
                for (Triplet t : da_s) {
                    t.cert.verify(t.pubKey);
                    if (!isInCAorDA(t)) {
                        da.add(t);
                    }
                }
                System.out.println("Synchronisation done");
            }
            else {
                System.out.println("Unknown client");
            }


            socket.close();


        } catch (IOException | ClassNotFoundException | CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    private boolean isInCAorDA(Triplet triplet) {
        for (Triplet t : ca) {
            if (t.equals(triplet)) {
                return true;
            }
        }
        for (Triplet t : da) {
            if (t.equals(triplet)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKnownCertificate(X509Certificate cert_auto_c) {
        for (Triplet tc : ca) {
            try {
                if (tc.pubKey.equals(cert_auto_c.getPublicKey())) {
                    tc.cert.verify(tc.pubKey);
                    return true;
                }
                else {
                    HashSet<String> visitedNodes = new HashSet<String>();
                    if (dfs(tc, cert_auto_c, visitedNodes))
                        return true;
                }
            } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean dfs(Triplet t, X509Certificate cert_auto_c, HashSet<String> visitedNodes) {
        visitedNodes.add(t.id);
        for (Triplet td : da) {
            if (!visitedNodes.contains(td.id) && td.cert.getPublicKey().equals(t.pubKey))
            {
                try {
                    td.cert.verify(td.pubKey);

                    if (td.pubKey.equals(cert_auto_c.getPublicKey())) {
                        return true;
                    }
                    return dfs(td, cert_auto_c, visitedNodes);
                } catch (CertificateException | SignatureException | NoSuchProviderException | InvalidKeyException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}

