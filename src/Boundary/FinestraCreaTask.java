package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.time.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.awt.FlowLayout;

import com.toedter.calendar.JDateChooser;

import Control.ControllerGestioneAzienda;

import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.DropMode;
import communicationEnum.*;

public class FinestraCreaTask extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField field_nome;
	private JTextField field_scadenza;

	private static Logger log = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraCreaTask frame = new FinestraCreaTask();
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
	public FinestraCreaTask() {
		getContentPane().setLayout(null);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().setLayout(null);
		setBounds(100, 100, 614, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_CreaTask = new JLabel("Crea Task");
		titolo_CreaTask.setFont(new Font("Tahoma", Font.PLAIN, 18));
		titolo_CreaTask.setBounds(281, 9, 105, 35);
		contentPane.add(titolo_CreaTask);
		
		JLabel titolo_Nome = new JLabel("Nome");
		titolo_Nome.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_Nome.setBounds(10, 52, 90, 27);
		contentPane.add(titolo_Nome);
		
		field_nome = new JTextField();
		field_nome.setFont(new Font("Tahoma", Font.PLAIN, 16));
		field_nome.setBounds(110, 55, 457, 24);
		contentPane.add(field_nome);
		field_nome.setColumns(10);
		
		JLabel titolo_Priorita = new JLabel("Priorità");
		titolo_Priorita.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_Priorita.setBounds(10, 89, 90, 27);
		contentPane.add(titolo_Priorita);
		
		//Permette di scegliere da un elenco finito di valori
		//(Il tipo lo indichiamo per evitare errori in fase di conversione da oggetto di JComboBox a Stringa)
		JComboBox<String> comboBox_priorita = new JComboBox<String>();
		comboBox_priorita.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox_priorita.setModel(new DefaultComboBoxModel<String>(new String[] {"", enumPrioritaTask.BASSA.toString(), enumPrioritaTask.MEDIA.toString(), enumPrioritaTask.ALTA.toString()}));
		comboBox_priorita.setBounds(110, 94, 457, 21);
		contentPane.add(comboBox_priorita);
		
		JLabel titolo_Scadenza = new JLabel("Scadenza");
		titolo_Scadenza.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_Scadenza.setBounds(10, 124, 90, 27);
		contentPane.add(titolo_Scadenza);
		
		JLabel titolo_descrizione = new JLabel("Descrizione");
		titolo_descrizione.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_descrizione.setBounds(10, 177, 90, 27);
		contentPane.add(titolo_descrizione);
		
		//JDateChooser fa in modo che si posa compilare il campo solo con una data nel formato espresso in setDateFormatString, altrimenti restituisce null
		//(Sia che sia lasciato vuoto sia che sia in formato sbagliato)
		JDateChooser field_scadenza= new JDateChooser();
		field_scadenza.setBounds(110, 126, 457, 21);
		field_scadenza.setDateFormatString("dd/MM/yyyy");
		contentPane.add(field_scadenza);
		
		JTextArea field_descrizione = new JTextArea();
		field_descrizione.setLineWrap(true);
		field_descrizione.setRows(3);
		field_descrizione.setBounds(110, 177, 457, 62);
		contentPane.add(field_descrizione);
		
		JLabel titolo_Errore = new JLabel("");
		titolo_Errore.setBounds(27, 252, 425, 35);
		contentPane.add(titolo_Errore);
		
		JButton btnCreaTask = new JButton("Crea Task");
		btnCreaTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nome_Task=field_nome.getText();
				enumPrioritaTask priorita;
				if((String) comboBox_priorita.getSelectedItem()!="") {
					priorita=enumPrioritaTask.valueOf((String) comboBox_priorita.getSelectedItem());
				}else{
					priorita=null;
				}
				String descrizione=field_descrizione.getText();

				if(field_scadenza.getDate()==null) {
					titolo_Errore.setText("Devi inserire una data valida nel formato gg/mm/aaaa");
				}else {
					LocalDate scadenza=field_scadenza.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					int creaTask=ControllerGestioneAzienda.getIstance().creaTask(nome_Task, priorita, scadenza, descrizione); //Inserire la funzione che crea il team
				
					if(creaTask==0) {
						titolo_Errore.setText("Task \"" + nome_Task + "\" creato con successo");
					}else if(creaTask==-1||creaTask==-6) {
						titolo_Errore.setText("C'è stato un problema durante la creazione del task,consulta il log");
					}else if(creaTask==-3) {
						titolo_Errore.setText("Il nome del task non può essere vuoto");
					}else if(creaTask==-4) {
						titolo_Errore.setText("Il nome del task può avere al più 50 caratteri");
					}else if(creaTask==-5) {
						titolo_Errore.setText("La descrizione del Task può avere al più 200 caratteri");
					}
				}
				
			}
		});
		btnCreaTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCreaTask.setBounds(462, 250, 105, 35);
		contentPane.add(btnCreaTask);
		
	}
}
