package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Control.ControllerGestioneAzienda;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinestraDipendente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraDipendente frame = new FinestraDipendente();
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
	public FinestraDipendente() {
		//Chiude tutte le finestre se si chiude questa(inserire un opzione che on close chiude tutto il programma)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 449, 241);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_MenuUtente = new JLabel("Menu Utente");
		titolo_MenuUtente.setFont(new Font("Tahoma", Font.PLAIN, 18));
		titolo_MenuUtente.setBounds(162, 10, 110, 34);
		contentPane.add(titolo_MenuUtente);
		
		//Per ogni funzione che l'utente può svolgere il menu presenta un bottone separato
		JButton btnCreaTask = new JButton("Crea Task");
		btnCreaTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraCreaTask creatask=new FinestraCreaTask();
				creatask.setVisible(true);
			}
		});
		btnCreaTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCreaTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCreaTask.setBounds(10, 60, 132, 34);
		contentPane.add(btnCreaTask);
		
		JButton btnAssegnaTask = new JButton("Assegna Task");
		btnAssegnaTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraAssegnaTask assegnaTask=new FinestraAssegnaTask();
				assegnaTask.setVisible(true);
			}
		});
		btnAssegnaTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAssegnaTask.setBounds(10, 132, 132, 34);
		contentPane.add(btnAssegnaTask);
		
		JButton btnMostraTaskAssegnati = new JButton("Mostra Task Assegnati");
		btnMostraTaskAssegnati.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraMostraTaskAssegnati frame=new FinestraMostraTaskAssegnati();
				frame.setVisible(true);
			}
		});
		btnMostraTaskAssegnati.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnMostraTaskAssegnati.setBounds(200, 60, 211, 34);
		contentPane.add(btnMostraTaskAssegnati);
		
		JButton btnModificaStatoTask = new JButton("Modifica Stato Task");
		btnModificaStatoTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraAssegnaStatoTask frame=new FinestraAssegnaStatoTask();
				frame.setVisible(true);
			}
		});
		btnModificaStatoTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnModificaStatoTask.setBounds(200, 134, 211, 34);
		contentPane.add(btnModificaStatoTask);
	}
}
