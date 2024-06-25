package Boundary;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Control.ControllerGestioneAzienda;
import communicationEnum.enumStatiTask;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Collection;

import javax.swing.ListSelectionModel;

public class FinestraAssegnaStatoTask extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_taskAsseganti;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraAssegnaStatoTask frame = new FinestraAssegnaStatoTask();
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
	public FinestraAssegnaStatoTask() {
		setBounds(100, 100, 784, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_TaskAssegnati = new JLabel("Seleziona lo stato a cui modificare lo stato");
		titolo_TaskAssegnati.setBounds(289, 26, 259, 13);
		contentPane.add(titolo_TaskAssegnati);
		
		//Creiamo una combobox con valori dinamici (così da mostrare solo gli stati che il task selezionato può assumere)
		JComboBox<String> cb_stato = new JComboBox<String>();
		cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {}));
		cb_stato.setBounds(289, 250, 289, 21);
		contentPane.add(cb_stato);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(69, 50, 665, 176);
		contentPane.add(scrollPane);
		
		String [] header_task=new String[] {"codice","nome","priorita","stato","scadenza","descrizione"};
		
		Vector<String> header_task_vector=new Vector<String> ();
		for(String header:header_task) {
			header_task_vector.addLast(header);
		}
		int num_header_task=header_task_vector.size();
		
		ArrayList<HashMap<String,String>> listMapTask=ControllerGestioneAzienda.getIstance().mostraTaskAssegnatiNonTerminatiLoggato();
        
		String [][] dati_task=null;
		
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
        
        DefaultTableModel model_task=new DefaultTableModel(dati_task,header_task);
		
		table_taskAsseganti = new JTable(model_task){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		//per far si che quando si selezioan un task nella combobox per selezionare lo stato escano solo quelli in cui il task può evolvere
		table_taskAsseganti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(table_taskAsseganti.getValueAt(table_taskAsseganti.getSelectedRow(),header_task_vector.indexOf("stato"))==enumStatiTask.ASSEGNATO.toString()) {
					cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {enumStatiTask.IN_LAVORAZIONE.toString(), enumStatiTask.TERMINATO.toString()}));
				}else {
					cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {enumStatiTask.TERMINATO.toString()}));
				}
				
			}
		});
		table_taskAsseganti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table_taskAsseganti);
		
		
		
		JLabel lbl_errore = new JLabel("");
		lbl_errore.setBounds(10, 298, 416, 13);
		contentPane.add(lbl_errore);
		
		JButton btn_changeStato = new JButton("Cambia Stato");
		btn_changeStato.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(table_taskAsseganti.getSelectedRow()<0) {
					lbl_errore.setText("Seleziona un Task");
				}else {
					int ID_task=Integer.parseInt(table_taskAsseganti.getValueAt(table_taskAsseganti.getSelectedRow(),header_task_vector.indexOf("codice")).toString());
					String nome_task = table_taskAsseganti.getValueAt(table_taskAsseganti.getSelectedRow(),header_task_vector.indexOf("nome")).toString();
					enumStatiTask new_stato = enumStatiTask.valueOf((String.valueOf(cb_stato.getSelectedItem())));
					
					int assegnazione=ControllerGestioneAzienda.getIstance().assegnaStatoTask(ID_task,new_stato);
					//chiamare la funzione che fa l'assegnazione e un if che chiama la funzione di assegnazione solo in caso di esito positivo
					if(assegnazione==0) {
						lbl_errore.setText("Task \"" + nome_task + "\" aggiornato con stato \""  + new_stato + "\"");
					}else {
						lbl_errore.setText("Problema: Task \"" + nome_task + "\" non aggiornato, Consulta il log");
					}
						
				
					//Una volta aggiornato lo stato carichiamo dinuovo i task in modo da dargli l'opportunità di continuare a modificare
					ArrayList<HashMap<String,String>> listMapTask=ControllerGestioneAzienda.getIstance().mostraTaskAssegnatiNonTerminatiLoggato();
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
			        
			        DefaultTableModel model_task=new DefaultTableModel(dati_task,header_task);
					
					table_taskAsseganti = new JTable(model_task){
						private static final long serialVersionUID =1L;
						public boolean isCellEditable(int row, int column) {                
			                return false;               
						}
					};
					table_taskAsseganti.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if(table_taskAsseganti.getValueAt(table_taskAsseganti.getSelectedRow(),header_task_vector.indexOf("stato"))==enumStatiTask.ASSEGNATO.toString()) {
								cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {enumStatiTask.IN_LAVORAZIONE.toString(), enumStatiTask.TERMINATO.toString()}));
							}else {
								cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {enumStatiTask.TERMINATO.toString()}));
							}
							
						}
					});
					scrollPane.setViewportView(table_taskAsseganti);
					
					cb_stato.setModel(new DefaultComboBoxModel<String>(new String[] {enumStatiTask.IN_LAVORAZIONE.toString(), enumStatiTask.TERMINATO.toString()}));
				}
				
			}
		});
		btn_changeStato.setBounds(289, 322, 259, 21);
		contentPane.add(btn_changeStato);
	}
}


