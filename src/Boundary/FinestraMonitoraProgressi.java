package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.Level;	

import Control.ControllerDirezioneAzienda;

import communicationEnum.*;

public class FinestraMonitoraProgressi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private static Logger log = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraMonitoraProgressi frame = new FinestraMonitoraProgressi();
					frame.setVisible(true);
				} catch (Exception e) {
					log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FinestraMonitoraProgressi() {
		setBounds(100, 100, 515, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_titolo = new JLabel("Progressi");
		lbl_titolo.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_titolo.setBounds(115, 11, 229, 30);
		contentPane.add(lbl_titolo);
		
		JLabel lbl_daAssegnare = new JLabel("Da Assegnare");
		lbl_daAssegnare.setBounds(10, 64, 105, 14);
		contentPane.add(lbl_daAssegnare);
		
		JLabel lbl_assegnati = new JLabel("Assegnati");
		lbl_assegnati.setBounds(10, 107, 105, 14);
		contentPane.add(lbl_assegnati);
		
		JLabel lbl_InLavorazione = new JLabel("In Lavorazione");
		lbl_InLavorazione.setBounds(10, 155, 105, 14);
		contentPane.add(lbl_InLavorazione);
		
		JLabel lbl_Terminati = new JLabel("Terminati");
		lbl_Terminati.setBounds(10, 196, 105, 14);
		contentPane.add(lbl_Terminati);
		
		HashMap<enumStatiTask,Integer> conteggioTask=ControllerDirezioneAzienda.getIstance().monitoraProgressi();
		int taskDaAssegnare=conteggioTask.get(enumStatiTask.DA_ASSEGNARE);
		int taskAssegnati=conteggioTask.get(enumStatiTask.ASSEGNATO);
		int taskInLavorazione=conteggioTask.get(enumStatiTask.IN_LAVORAZIONE);
		int taskTerminati=conteggioTask.get(enumStatiTask.TERMINATO);
		int somma=taskDaAssegnare+taskAssegnati+taskInLavorazione+taskTerminati;
		
		JLabel lbl_NumDaAssegnare = new JLabel(Integer.toString(taskDaAssegnare));
		lbl_NumDaAssegnare.setBounds(364, 64, 46, 14);
		contentPane.add(lbl_NumDaAssegnare);
		
		JLabel lbl_NumAssegnati = new JLabel(Integer.toString(taskAssegnati));
		lbl_NumAssegnati.setBounds(364, 107, 46, 14);
		contentPane.add(lbl_NumAssegnati);
		
		JLabel lbl_NumInLavorazione = new JLabel(Integer.toString(taskInLavorazione));
		lbl_NumInLavorazione.setBounds(364, 155, 46, 14);
		contentPane.add(lbl_NumInLavorazione);
		
		JLabel lbl_NumTerminati = new JLabel(Integer.toString(taskTerminati));
		lbl_NumTerminati.setBounds(364, 196, 46, 14);
		contentPane.add(lbl_NumTerminati);
		
		JProgressBar PB_daAssegnare = new JProgressBar();
		PB_daAssegnare.setStringPainted(true);
		PB_daAssegnare.setValue((taskDaAssegnare*100)/somma);
		PB_daAssegnare.setBounds(125, 64, 229, 14);
		contentPane.add(PB_daAssegnare);
		
		JProgressBar PB_Assegnati = new JProgressBar();
		PB_Assegnati.setStringPainted(true);
		PB_Assegnati.setValue((taskAssegnati*100)/somma);
		PB_Assegnati.setBounds(125, 107, 229, 14);
		contentPane.add(PB_Assegnati);
		
		JProgressBar PB_inLAvorazione = new JProgressBar();
		PB_inLAvorazione.setStringPainted(true);
		PB_inLAvorazione.setValue((taskInLavorazione*100)/somma);
		PB_inLAvorazione.setBounds(125, 155, 229, 14);
		contentPane.add(PB_inLAvorazione);
		
		JProgressBar PB_Terminati = new JProgressBar();
		PB_Terminati.setStringPainted(true);
		PB_Terminati.setValue((taskTerminati*100)/somma);
		PB_Terminati.setBounds(125, 196, 229, 14);
		contentPane.add(PB_Terminati);	
	}
}
