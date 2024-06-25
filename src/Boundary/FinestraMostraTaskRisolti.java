package Boundary;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle.Control;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Control.ControllerDirezioneAzienda;

import javax.swing.SwingConstants;

public class FinestraMostraTaskRisolti extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_TaskRisolti;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraMostraTaskRisolti frame = new FinestraMostraTaskRisolti();
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
	public FinestraMostraTaskRisolti() {
		setBounds(100, 100, 944, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_TaskAssegnati = new JLabel("Questi sono i task terminati");
		titolo_TaskAssegnati.setHorizontalAlignment(SwingConstants.CENTER);
		titolo_TaskAssegnati.setBounds(324, 26, 414, 13);
		contentPane.add(titolo_TaskAssegnati);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(69, 50, 849, 176);
		contentPane.add(scrollPane);
		
		//Ci facciamo ritornare l'array di tutti i task esistenti
		String[] header_task=new String[] {"codice","nome","priorita","scadenza","descrizione"};
		
		Vector<String> header_task_vector=new Vector<String> ();
		for(String header:header_task) {
			header_task_vector.addLast(header);
		}
		int num_task_header=header_task_vector.size();
		
		ArrayList<HashMap<String,String>> listMapTask=ControllerDirezioneAzienda.getIstance().mostraTaskTerminati();
		String[][] dati_task=null;
		
		if(listMapTask==null) {
			dati_task=new String[][] {{}};
		}else {
			dati_task=new String[listMapTask.size()][num_task_header];
	        for(int i=0;i<listMapTask.size();i++) {
	        	for(int j=0;j<num_task_header;j++) {
	        		dati_task[i][j]=(String) listMapTask.get(i).get(header_task[j]);
	        	}
	        } 
		}
        DefaultTableModel model_task=new DefaultTableModel(dati_task,header_task);
		
        table_TaskRisolti = new JTable(model_task){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_TaskRisolti.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(table_TaskRisolti);
	}

}

