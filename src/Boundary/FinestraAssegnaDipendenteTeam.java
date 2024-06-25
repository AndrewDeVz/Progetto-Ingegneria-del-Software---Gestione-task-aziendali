package Boundary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Control.ControllerGestioneAzienda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class FinestraAssegnaDipendenteTeam extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_team;
	private JTable table_dipendenti;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraAssegnaDipendenteTeam frame = new FinestraAssegnaDipendenteTeam();
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
	public FinestraAssegnaDipendenteTeam() {
		super("Assegna Dipendente Team");
		setBounds(100, 100, 649, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titolo_SelezionaTeam = new JLabel("Seleziona Team");
		titolo_SelezionaTeam.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_SelezionaTeam.setBounds(57, 26, 116, 27);
		contentPane.add(titolo_SelezionaTeam);
		
		JLabel titolo_SelezionaDipendente = new JLabel("Seleziona Dipendente");
		titolo_SelezionaDipendente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		titolo_SelezionaDipendente.setForeground(new Color(0, 0, 0));
		titolo_SelezionaDipendente.setBounds(265, 25, 200, 27);
		contentPane.add(titolo_SelezionaDipendente);
		
		JScrollPane scrollPane_Team = new JScrollPane();
		scrollPane_Team.setBounds(21, 62, 196, 194);
		contentPane.add(scrollPane_Team);
		
		
		String [] header_team=new String[] {"nominativo"};
		
		Vector<String> header_team_vector=new Vector<String> ();
		for(String header:header_team) {		
			header_team_vector.addLast(header);
		}
		int num_header_team=header_team_vector.size();
		
		ArrayList<HashMap<String,String>> listMapTeam=ControllerGestioneAzienda.getIstance().mostraTuttiTeam();
		
		String[][] dati_team=null;
		if(listMapTeam==null) {
			dati_team= new String[][]{{}};
		}else {
	        dati_team=new String[listMapTeam.size()][num_header_team];
	        for(int i=0;i<listMapTeam.size();i++) {
	        	for(int j=0;j<num_header_team;j++) {
	        		dati_team[i][j]=(String) listMapTeam.get(i).get(header_team[j]);
	        	}
	        }  
		}
        
        DefaultTableModel model_task=new DefaultTableModel(dati_team,header_team);
		
		table_team = new JTable(model_task){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_team.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_Team.setViewportView(table_team);
		
		JScrollPane scrollPane_Dipendente = new JScrollPane();
		scrollPane_Dipendente.setBounds(265, 62, 347, 194);
		contentPane.add(scrollPane_Dipendente);
		
		String[] header_dipendenti=new String[] {"id","nome","team","cognome","livello"};
		
		Vector<String> header_dipendenti_vector=new Vector<String> ();
		for(String header:header_dipendenti) {		
			header_dipendenti_vector.addLast(header);
		}
		int num_header_dipendenti=header_dipendenti_vector.size();
		
		ArrayList<HashMap<String,String>> listMapDipendenti=ControllerGestioneAzienda.getIstance().mostraTuttiDipendentiSenzaTeam();
		
		String[][] dati_dipendenti=null;
		
		if(listMapDipendenti==null) {
			dati_dipendenti=new String[][]{{}};
		}else {		
	        dati_dipendenti=new String[listMapDipendenti.size()][num_header_dipendenti];
	        for(int i=0;i<listMapDipendenti.size();i++) {
	        	for(int j=0;j<num_header_dipendenti;j++) {
	        		dati_dipendenti[i][j]=(String) listMapDipendenti.get(i).get(header_dipendenti[j]);
	        	}
	        }
		}
        
        
        DefaultTableModel model_dipendenti=new DefaultTableModel(dati_dipendenti,header_dipendenti);
        
		table_dipendenti = new JTable(model_dipendenti){
			private static final long serialVersionUID =1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		table_dipendenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_Dipendente.setViewportView(table_dipendenti);
		
		JLabel titolo_errore = new JLabel("");
		titolo_errore.setBounds(20, 270, 593, 50);
		contentPane.add(titolo_errore);
		
		JButton btnAssegnaDipendente = new JButton("Assegna Dipendente");
		btnAssegnaDipendente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if(table_team.getSelectedRow()<0) {
					titolo_errore.setText("Seleziona un Team");
				}else if(table_dipendenti.getSelectedRow()<0) {
					titolo_errore.setText("Seleziona un dipendente");
				}else {
					String nome_team = table_team.getValueAt(table_team.getSelectedRow(),header_team_vector.indexOf("nominativo")).toString();
					int ID_dipendente=Integer.parseInt(table_dipendenti.getValueAt(table_dipendenti.getSelectedRow(),header_dipendenti_vector.indexOf("id")).toString());
					String nome_dipendente = table_dipendenti.getValueAt(table_dipendenti.getSelectedRow(),header_dipendenti_vector.indexOf("nome")).toString();
					
					int assegnazione=ControllerGestioneAzienda.getIstance().assegnaDipendenteTeam(ID_dipendente, nome_team);
					if(assegnazione==0) {
						titolo_errore.setText("\"" + nome_dipendente + "\" assegnato con successo al Team \"" + nome_team + "\"");
						model_dipendenti.removeRow(table_dipendenti.getSelectedRow());
					}else{
						titolo_errore.setText("Problema: \"" + nome_dipendente + "\" non assegnato al Team \"" + nome_team + "\". Conuslta il log");
					}
				}
				
			}
		});
		btnAssegnaDipendente.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAssegnaDipendente.setBounds(170, 344, 246, 38);
		contentPane.add(btnAssegnaDipendente);

		setContentPane(contentPane);
	}

}

