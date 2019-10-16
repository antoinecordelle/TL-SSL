
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoDialog extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel theText;

    public InfoDialog( String text )
    {
        //setContentPane( contentPane );
        setModal( true );
        getRootPane().setDefaultButton( buttonOK );

        buttonOK.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                onOK();
            }
        } );

        theText.setText( text );
    }

    private void onOK()
    {
        dispose();
    }

    public static void print( String text )
    {
        InfoDialog dialog = new InfoDialog( text );
        dialog.pack();
        if (EquipementGUI.mainFrame != null)
            dialog.setLocationRelativeTo( EquipementGUI.mainFrame );
        dialog.setVisible( true );
    }

    public static void main (String[] args)
    {

        System.out.println ("Creation Google Pixel");

        InfoDialog.print("Test");
    }

}