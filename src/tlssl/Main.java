package tlssl;

public class Main {

    public static void main(String[] args) {
        try {
            Equipement equipement1 = new Equipement("Monolithe", 666);
            Equipement equipement2 = new Equipement("Bla", 666);
            equipement2.certify(equipement1);
            equipement2.affichage_ca();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
