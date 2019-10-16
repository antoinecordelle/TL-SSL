
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.beans.*; //Property change stuff

public class EquipementGUI
{

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;
    private JTextArea caDisp;
    private JTextArea daDisp;
    private JList caList;
    private JList daList;
    private JButton serverConnexionButton;
    private JButton clientConnexionButton;
    private JTextField adresseField;
    private JTextField portField;

    public static JFrame mainFrame = null;

    public EquipementGUI ( )
    {

        this.textArea1.setText ("Test");

        tabbedPane1.addChangeListener (new ChangeListener ()
        {
            @Override
            public void stateChanged (ChangeEvent changeEvent)
            {

                // Update the content of the displayed list to make sure we are up to date.

                String title = tabbedPane1.getTitleAt (tabbedPane1.getSelectedIndex ());
                if (title.equals ("CA"))
                {

                } else if (title.equals ("DA"))
                {

                }
            }
        });

		this.serverConnexionButton = new JButton();
        serverConnexionButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

            }
        });
        
        this.clientConnexionButton = new JButton();
        clientConnexionButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                String hostname = adresseField.getText();
                int port;
                try
                {
                    port = Integer.parseUnsignedInt(portField.getText());
                }
                catch ( Exception excpt )
                {
                    InfoDialog.print( "Le port est invalide !" );
                    return;
                }
                if ( hostname.equals("") )
                {
                    InfoDialog.print( "Vous devez entrer une adresse pour l'équipement serveur." );
                    return;
                }

            }
        });
    }

    public static void main (String[] args)
    {

        displayGUI( );
    }

    public static void displayGUI( )
    {
        mainFrame = new JFrame( "Equipement - Test" );

        mainFrame.setPreferredSize( new Dimension( 580, 540 ) );
        // frame.setResizable( false );
        JMenuBar mb = new JMenuBar();

        JMenu m = new JMenu( "Aide" );
        JMenuItem mi = new JMenuItem( "A propos" );
        m.add( mi );

        mi.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent actionEvent )
            {
                    JOptionPane.showMessageDialog(mainFrame,"TL Cryptographie");
            }
        } );

        mb.add( m );
        mainFrame.setJMenuBar( mb );
        mainFrame.setContentPane( new EquipementGUI( ).panel1 );
        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        mainFrame.pack();
        mainFrame.setVisible( true );

    }

    {
        setupUI();
    }

    private void setupUI()
    {

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout( ) );
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setTabLayoutPolicy( 0 );
        tabbedPane1.setTabPlacement( 1 );
        panel1.add( tabbedPane1 );

		// Le premier "onglet" Informations.
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy( 31 );
        tabbedPane1.addTab( "Informations", scrollPane1 );
        textArea1 = new JTextArea();
        textArea1.setEditable( false );
        textArea1.setEnabled( true );
        textArea1.setLineWrap( true );
        textArea1.setWrapStyleWord( true );
        scrollPane1.setViewportView( textArea1 );
        
        // Le second "onglet" CA.
        final JPanel panel2 = new JPanel();
        panel2.setLayout( new GridBagLayout() );
        tabbedPane1.addTab( "CA", panel2 );
        
        GridBagConstraints c = new GridBagConstraints();
		
        final JLabel label1 = new JLabel();
        label1.setText( "Selectionner un certificate" );
        c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
        panel2.add( label1, c);
        
        final JPanel panel3 = new JPanel();
        panel3.setLayout( new GridBagLayout() );
        c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
        panel2.add( panel3,c);
        
        final JScrollPane scrollPane2 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add( scrollPane2, gbc );
        caList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement( "Certif 1" );
        defaultListModel1.addElement( "Certif 2" );
        caList.setModel( defaultListModel1 );
        caList.setSelectionMode( 0 );
        scrollPane2.setViewportView( caList );

        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setEnabled( true );
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add( scrollPane3, gbc );
        caDisp = new JTextArea();
        caDisp.setEditable( false );
        caDisp.setLineWrap( true );
        caDisp.setText( "Informations sur le certificat" );
        caDisp.setWrapStyleWord( false );
        scrollPane3.setViewportView( caDisp );
        
 	   	// Le troisieme "onglet" DA.
        final JPanel panel4 = new JPanel();
        panel4.setLayout( new GridBagLayout() );
        tabbedPane1.addTab( "DA", panel4 );
        
        c = new GridBagConstraints();
        
        final JLabel label2 = new JLabel();
        label2.setText( "Selectionner un certificate" );
        c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
        panel4.add( label2,c);

        final JPanel panel5 = new JPanel();
        panel5.setLayout( new GridBagLayout() );
        c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
        panel4.add( panel5,c);
        
        final JScrollPane scrollPane4 = new JScrollPane();
    	gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add( scrollPane4, gbc );
        daList = new JList();
        daList.setLayoutOrientation( 0 );
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        defaultListModel2.addElement( "Certif 1" );
        defaultListModel2.addElement( "Certif 2" );
        daList.setModel( defaultListModel2 );
        daList.setSelectionMode( 0 );
        scrollPane4.setViewportView( daList );

        final JScrollPane scrollPane5 = new JScrollPane();
        scrollPane5.setEnabled( true );
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add( scrollPane5, gbc );
        daDisp = new JTextArea();
        daDisp.setEditable( false );
        daDisp.setLineWrap( true );
        daDisp.setText( "Informations sur le certificat" );
        daDisp.setWrapStyleWord( false );
        scrollPane5.setViewportView( daDisp );

 	   	// Le quatrieme "onglet" Action.           
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout() );
        tabbedPane1.addTab( "Actions", panel6 );
        
        final JPanel panel7 = new JPanel();
        panel7.setLayout( new GridBagLayout() );
        panel6.add( panel7) ;
        
        final JPanel panel8 = new JPanel();
        panel8.setLayout( new GridBagLayout() );
        c.gridx = 0;
		c.gridy = 0;
        panel7.add( panel8,c);
        panel8.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Client" ) );

        clientConnexionButton = new JButton();
        clientConnexionButton.setText( "Connexion" );
        c.gridx = 0;
		c.gridy = 0;
        c.weightx = 1;
        panel8.add( clientConnexionButton,c);
        
        final JPanel panel9 = new JPanel();
        panel9.setLayout( new GridBagLayout() );
        c.gridx = 0;
		c.gridy = 1;
        panel8.add( panel9,c);
        
        final JLabel label3 = new JLabel();
        label3.setText( "Adresse" );
        Dimension dim = label3.getPreferredSize();
    	label3.setPreferredSize(new Dimension(dim.width+150,dim.height));
        c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel9.add( label3, c);

        final JLabel label4 = new JLabel();
        label4.setText( "Port" );
    	label4.setPreferredSize(new Dimension(dim.width+150,dim.height));
        c.gridx = 1;
		c.gridy = 0;
        panel9.add( label4, c);

		adresseField = new JTextField();
        adresseField.setText( "localhost" );
        c.gridx = 0;
		c.gridy = 1;
        panel9.add( adresseField, c);

		portField = new JTextField();
        portField.setText( "1234" );
        c.gridx = 1;
		c.gridy = 1;
        panel9.add( portField, c);
        
        final JPanel panel10 = new JPanel();
        panel10.setLayout( new GridBagLayout() );
        panel10.setPreferredSize(panel8.getPreferredSize());
        c.gridx = 0;
		c.gridy = 1;
        panel7.add( panel10,c);
        panel10.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Serveur" ) );

        serverConnexionButton = new JButton();
        serverConnexionButton.setText( "Connexion" );
        c.gridx = 0;
		c.gridy = 0;
        c.weightx = 1;
        panel10.add( serverConnexionButton,c);
    }
}
