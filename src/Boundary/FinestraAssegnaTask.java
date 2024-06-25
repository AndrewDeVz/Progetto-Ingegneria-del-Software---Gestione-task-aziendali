package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Control.ControllerGestioneAzienda;

import javax.swing.JList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinestraAssegnaTask extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_task;
	private JTable table_dipendente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraAssegnaTask frame = new FinestraAssegnaTask();
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
	
	
	public FinestraAssegnaTask() {
		setBounds(100, 100, 965, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_SelezionaTask = new JLabel("Seleziona Task");
		titolo_SelezionaTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_SelezionaTask.setBounds(222, 26, 116, 27);
		contentPane.add(titolo_SelezionaTask);
		
		JLabel titolo_SelezionaDipendente = new JLabel("Seleziona Dipendente");
		titolo_SelezionaDipendente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_SelezionaDipendente.setForeground(new Color(0, 0, 0));
		titolo_SelezionaDipendente.setBounds(679, 26, 151, 27);
		contentPane.add(titolo_SelezionaDipendente);
		
		JScrollPane scrollPane_task = new JScrollPane();
		scrollPane_task.setBounds(21, 62, 543, 194);
		contentPane.add(scrollPane_task);
		
		String[] header_task=new String[] {"codice","nome","priorita","stato","scadenza","descrizione"};
		
		Vector<String> header_task_vector=new Vector<String> ();
		for(String header:header_task) {
			header_task_vector.addLast(header);
		}
		int num_header_task=header_task_vector.size();
		
		ArrayList<HashMap<String,String>> listMapTask=ControllerGestioneAzienda.getIstance().mostraTuttiTaskSenzaDipendente();
	
		String[][] dati_task=null;
		
		if(listMapTask==null) {
			dati_task=new String[][] {{}};
		}else {
			dati_task=new String[listMapTask.size()][num_header_task];
	        for(int i=0;i<listMapTask.size();i++) {
	        	for(int j=0;j<num_header_task;j++) {
	        		dati_task[i][j]=(String) listMapTask.get(i).get(header_task[j]);
	        	}
	        } 
		}
        
        //Creiamo la struttura della tabella da isnerire nella nostra JTable (così da renderla anche facilemnte modificabile)
        DefaultTableModel model_task=new DefaultTableModel(dati_task,header_task);
		
        //Creiamo la Jtable con i dati della tabella creata sopra
		table_task = new JTable(model_task){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_task.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_task.setViewportView(table_task);
		
		JScrollPane scrollPane_Dipendente = new JScrollPane();
		scrollPane_Dipendente.setBounds(574, 62, 347, 194);
		contentPane.add(scrollPane_Dipendente);
		
		String[] header_dipendenti=new String[] {"id","nome","cognome","team","livello"};
		
		Vector<String> header_dipendenti_vector=new Vector<String> ();
		for(String header:header_dipendenti) {
			header_dipendenti_vector.addLast(header);
		}
		int num_dipendenti_header=header_dipendenti_vector.size();
		
		ArrayList<HashMap<String,String>> listMapDipendenti=ControllerGestioneAzienda.getIstance().mostraTuttiDipendenti();
        
		String[][] dati_dipendenti=null;
		
		if(listMapDipendenti==null) {
			dati_dipendenti=new String[][] {{}};
		}else {		
			dati_dipendenti=new String[listMapDipendenti.size()][num_dipendenti_header];
	        for(int i=0;i<listMapDipendenti.size();i++) {
	        	for(int j=0;j<num_dipendenti_header;j++) {
	        		dati_dipendenti[i][j]=(String) listMapDipendenti.get(i).get(header_dipendenti[j]);
	        	}
	        }  
		}
        DefaultTableModel model_dipendenti=new DefaultTableModel(dati_dipendenti,header_dipendenti);
        
		table_dipendente = new JTable(model_dipendenti){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_dipendente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_Dipendente.setViewportView(table_dipendente);
		
		JLabel titolo_errore = new JLabel("");
		titolo_errore.setBounds(20, 270, 593, 50);
		contentPane.add(titolo_errore);
		
		JButton btnAssegnaTask = new JButton("Assegna Task");
		btnAssegnaTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Verifichiamo che sia stato selezionato un task e un dipendente
				if(table_task.getSelectedRow()<0) {
					titolo_errore.setText("Seleziona un Task");
				}else if(table_dipendente.getSelectedRow()<0) {
					titolo_errore.setText("Seleziona un dipendente");
				}else {
					int ID_task=Integer.parseInt(table_task.getValueAt(table_task.getSelectedRow(),header_task_vector.indexOf("codice")).toString());
					String nome_task = table_task.getValueAt(table_task.getSelectedRow(),header_task_vector.indexOf("nome")).toString();
					int ID_dipendente=Integer.parseInt(table_dipendente.getValueAt(table_dipendente.getSelectedRow(),header_dipendenti_vector.indexOf("id")).toString());
					String nome_dipendente = table_dipendente.getValueAt(table_dipendente.getSelectedRow(),header_dipendenti_vector.indexOf("nome")).toString();
					String livello_dipendente = table_dipendente.getValueAt(table_dipendente.getSelectedRow(),header_dipendenti_vector.indexOf("livello")).toString();
					
					//Se si chiamiamo la funzione del controller che mi fa fare l'assegnazione
					int assegnaTask=ControllerGestioneAzienda.getIstance().assegnaTaskDipendente(ID_task, ID_dipendente);
					//(0=successo,1=livello non adeguato,2=Team diverso,3=dipendente con più di 3 task)
					if(assegnaTask==-2) {
						titolo_errore.setText("Il tuo livello non è adeguato ad assegnare un task a: " + nome_dipendente + " livello: " + livello_dipendente);
					}else if(assegnaTask==-5) {
						titolo_errore.setText("Tu e " + nome_dipendente + " non siete nello stesso team, non puoi assegnargli un task");
					}else if(assegnaTask==-6) {
						titolo_errore.setText(nome_dipendente + " già ha 3 task assegnati");
					}else if(assegnaTask==-1||assegnaTask<-7){//i valori di ritorno -1, -7,-8,-9 corrispondono ad errori che non sono previsit nei casi di test, epr tanto si solleva un errore generico
						titolo_errore.setText("Problema:" + nome_task + " non assegnato, consulta il log");
					}else if(assegnaTask==-3) {
						titolo_errore.setText("Problema:" + nome_dipendente + " non è in nessun team, fallo assegnare ad un team dal responsabile manager");
					}else if(assegnaTask==-4) {
						titolo_errore.setText("Problema: Non sei in nessun team, fatti assegnare ad un team dal responsabile manager");
					}else{
						titolo_errore.setText("Task \"" + nome_task + "\" assegnato con successo a \"" + nome_dipendente + "\"");
						model_task.removeRow(table_task.getSelectedRow());
					}
				}
				
			}
		});
		btnAssegnaTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAssegnaTask.setBounds(367, 312, 246, 38);
		contentPane.add(btnAssegnaTask);
		
		
	}
}
