package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import java.lang.NumberFormatException;

import Control.*;
import communicationEnum.enumRuoli;

public class FinestraAutenticazione extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField campo_username;
	private JPasswordField campo_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraAutenticazione frame = new FinestraAutenticazione();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FinestraAutenticazione() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 927, 303);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_autenticazione = new JLabel("Autenticazione");
		titolo_autenticazione.setFont(new Font("Tahoma", Font.PLAIN, 18));
		titolo_autenticazione.setBounds(424, 11, 124, 35);
		contentPane.add(titolo_autenticazione);
		
		JLabel titolo_id = new JLabel("ID:");
		titolo_id.setHorizontalAlignment(SwingConstants.RIGHT);
		titolo_id.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_id.setBounds(72, 69, 90, 35);
		contentPane.add(titolo_id);
		
		JLabel titolo_password = new JLabel("Password:");
		titolo_password.setHorizontalAlignment(SwingConstants.RIGHT);
		titolo_password.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_password.setBounds(72, 133, 90, 35);
		contentPane.add(titolo_password);
		
		campo_username = new JTextField();
		campo_username.setBounds(172, 69, 682, 35);
		contentPane.add(campo_username);
		campo_username.setColumns(10);
		
		JLabel label_errore_autenticazione = new JLabel("");
		label_errore_autenticazione.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_errore_autenticazione.setBounds(82, 178, 522, 30);
		contentPane.add(label_errore_autenticazione);
		
		campo_password = new JPasswordField();
		campo_password.setBounds(172, 135, 682, 35);
		contentPane.add(campo_password);
		
		//Per permettere un autenticazione separata lasciamo che sia l'utente a scegliere a quale delle tre "modalità" (dipendente, responasbile Team o manager) vuole accedere
		//Poi facciamo in modo che sia stesso il livello boundary a indicare al controlleer il ruolo dell'utente che accede
		JButton btn_AutenticaDipendente = new JButton("Autentica Dipendente");
		btn_AutenticaDipendente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					int id= Integer.parseInt(campo_username.getText());
					String password= String.valueOf(campo_password.getPassword());					
					
					//Richiediamo al controller la verifica delle credenziali
					int autenticato=ControllerGestioneAzienda.getIstance().autenticazioneDipendente(id, password);//Inserire la funzione autenticazione del controller che prende la password e ti dice se è autenticato (e chi) o meno
					
					//Se la verifica ha esito negativo ritorniamo errore, altrimenti mostriamo il menu dipendenti(e nascondiamo questa)
					if(autenticato==-1) {
						label_errore_autenticazione.setText("Non esiste alcun dipendente con queste credenziali");
						campo_username.setText("");
						campo_password.setText("");
					}else if(autenticato==0){
						FinestraDipendente menuUtente=new FinestraDipendente();
						menuUtente.setVisible(true);
						setVisible(false);
					}else {
						label_errore_autenticazione.setText("C'è stato un errore nel sistema, cosnulta il log");
					}
				}catch (NumberFormatException numError) {
					label_errore_autenticazione.setText("L'ID deve essere un numero");
					campo_username.setText("");
				}
			}
		});
		btn_AutenticaDipendente.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_AutenticaDipendente.setBounds(105, 219, 214, 40);
		contentPane.add(btn_AutenticaDipendente);
		
		//Stessa logica di btn_AutenticaDipendente
		JButton btn_AutenticaResponsabileTeam = new JButton("Autentica Responsabile Team");
		btn_AutenticaResponsabileTeam.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_AutenticaResponsabileTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					int id= Integer.parseInt(campo_username.getText());
					String password= String.valueOf(campo_password.getPassword());
					
					int autenticato=ControllerGestioneAzienda.getIstance().autenticazioneResponsabileTeam(id, password);//Inserire la funzione autenticazione del controller che prende la password e ti dice se è autenticato
					
					if(autenticato==-1) {
						label_errore_autenticazione.setText("Non esiste alcun Responsabile Team con queste credenziali");
						campo_username.setText("");
						campo_password.setText("");
					}else if(autenticato==0){
						FinestraResponsabileTeam menuRespTeam=new FinestraResponsabileTeam();
						menuRespTeam.setVisible(true);
						setVisible(false);
					}else {
						label_errore_autenticazione.setText("C'è stato un errore nel sistema, cosnulta il log");
					}
				}catch (NumberFormatException numError) {
					label_errore_autenticazione.setText("L'ID deve essere un numero");
					campo_username.setText("");
				}
			}
		});
		btn_AutenticaResponsabileTeam.setBounds(350, 220, 280, 38);
		contentPane.add(btn_AutenticaResponsabileTeam);
		
		//Stessa logica di btn_AutenticaDipendente
		JButton btn_AutenticaManager = new JButton("Autentica Manager");
		btn_AutenticaManager.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_AutenticaManager.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int id= Integer.parseInt(campo_username.getText());
					String password= String.valueOf(campo_password.getPassword());
					
					int autenticato=ControllerDirezioneAzienda.getIstance().autenticazioneManager(id, password);//Inserire la funzione autenticazione del controller che prende la password e ti dice se è autenticato 
					
					if(autenticato==-1) {
						label_errore_autenticazione.setText("Non esiste alcun Manager con queste credenziali");
						campo_username.setText("");
						campo_password.setText("");
					}else if(autenticato==0){
						FinestraManager menuManager=new FinestraManager();
						menuManager.setVisible(true);
						setVisible(false);
					}else {
						label_errore_autenticazione.setText("C'è stato un errore nel sistema, cosnulta il log");
					}
				}catch (NumberFormatException numError) {
					label_errore_autenticazione.setText("L'ID deve essere un numero");
					campo_username.setText("");
				}
			}
		});
		btn_AutenticaManager.setBounds(640, 220, 214, 38);
		contentPane.add(btn_AutenticaManager);
		
	}
	
}
