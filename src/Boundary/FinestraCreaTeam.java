package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import Control.*;

public class FinestraCreaTeam extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField field_nome;
	
	private static Logger log = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraCreaTeam frame = new FinestraCreaTeam();
					frame.setVisible(true);
				} catch (Exception e) {
					log.log(Level.SEVERE,"C'è stato un errore",e);
				}
			}
		});
	}

	
	
	/**
	 * Create the frame.
	 */
	public FinestraCreaTeam() {
		setBounds(100, 100, 453, 191);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_CreaTeam = new JLabel("Crea Team");
		lbl_CreaTeam.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_CreaTeam.setBounds(148, 11, 138, 14);
		contentPane.add(lbl_CreaTeam);
		
		field_nome = new JTextField();
		field_nome.setBounds(148, 48, 251, 20);
		contentPane.add(field_nome);
		field_nome.setColumns(10);
		
		JLabel lbl_nometeam = new JLabel("Nome Team");
		lbl_nometeam.setBounds(44, 48, 94, 20);
		contentPane.add(lbl_nometeam);
		
		JLabel lbl_erroreCreaTeam = new JLabel("");
		lbl_erroreCreaTeam.setBounds(54, 79, 345, 29);
		contentPane.add(lbl_erroreCreaTeam);
		
		JButton btn_CreaTeam = new JButton("Crea Team");
		btn_CreaTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String nome_Team=field_nome.getText();

				int creaTeam=ControllerGestioneAzienda.getIstance().creaTeam(nome_Team); //Inserire la funzione che crea il team
				
				if(creaTeam==0) {
					lbl_erroreCreaTeam.setText("Team " + nome_Team + " registrato");
				}else if(creaTeam==-1||creaTeam==-5) {
					lbl_erroreCreaTeam.setText("C'è stato un problema, il team non è stato salvato, contolla il log");
				}else if(creaTeam==-2) {
					lbl_erroreCreaTeam.setText("Il nome del team deve contenere almeno un carattere");
				}else if(creaTeam==-3) {
					lbl_erroreCreaTeam.setText("Il nome del team deve essere al più 50 caratteri");
				}else if(creaTeam==-4) {
					lbl_erroreCreaTeam.setText("Già esiste un Team con questo nominativo");
				}
			}
		});
		btn_CreaTeam.setBounds(148, 118, 126, 23);
		contentPane.add(btn_CreaTeam);
		
		
	}
}

