package Boundary;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Control.ControllerGestioneAzienda;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class FinestraMostraTaskAssegnati extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_taskAssegnati;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraMostraTaskAssegnati frame = new FinestraMostraTaskAssegnati();
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
	public FinestraMostraTaskAssegnati() {
		setBounds(100, 100, 805, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_TaskAssegnati = new JLabel("Questi sono i task assegnati");
		titolo_TaskAssegnati.setBounds(342, 26, 152, 13);
		contentPane.add(titolo_TaskAssegnati);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(69, 50, 676, 176);
		contentPane.add(scrollPane);
		
		String[] header_task=new String[] {"codice","nome","priorita","stato","scadenza","descrizione"};
		
		Vector<String> header_task_vector=new Vector<String> ();
		for(String header:header_task) {
			header_task_vector.addLast(header);
		}
		int num_task_header=header_task_vector.size();
		
		ArrayList<HashMap<String,String>> listMapTask=ControllerGestioneAzienda.getIstance().mostraTaskAssegnatiLoggato();
        String[][] dati_task=new String[listMapTask.size()][num_task_header];
        for(int i=0;i<listMapTask.size();i++) {
        	for(int j=0;j<num_task_header;j++) {
        		dati_task[i][j]=(String) listMapTask.get(i).get(header_task[j]);
        	}
        } 
        
        DefaultTableModel model_task=new DefaultTableModel(dati_task,header_task);
		
		table_taskAssegnati = new JTable(model_task){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_taskAssegnati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table_taskAssegnati);
	}
}

