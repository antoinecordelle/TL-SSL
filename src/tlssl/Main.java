package tlssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        Equipement equipement = new Equipement("Monolithe", 6666);

        BufferedReader input  = new BufferedReader(new InputStreamReader(System.in));

        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over"))
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
                case "start server":
                    equipement.startServer();
                    break;

                case "start client":
                    equipement.startClient();
                    break;

                }

        }

    }
}
