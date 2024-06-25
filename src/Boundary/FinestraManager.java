package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class FinestraManager extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraManager frame = new FinestraManager();
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
	public FinestraManager() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 826, 258);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_Errore = new JLabel("");
		lbl_Errore.setVerticalAlignment(SwingConstants.TOP);
		lbl_Errore.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_Errore.setBounds(10, 134, 777, 74);
		contentPane.add(lbl_Errore);
		
		JButton btn_MostraTaskRisolti = new JButton("Mostra Task Risolti");
		btn_MostraTaskRisolti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraMostraTaskRisolti frame=new FinestraMostraTaskRisolti();
				lbl_Errore.setText("");
				frame.setVisible(true);
			}
		});
		btn_MostraTaskRisolti.setBounds(10, 11, 415, 23);
		contentPane.add(btn_MostraTaskRisolti);
		
		JButton btn_MostraTaskRisoltiDipendente = new JButton("[Da Implementare] Mostra Task Risolti Dipendente");
		btn_MostraTaskRisoltiDipendente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lbl_Errore.setText("<html>La funzione \"Mostra Task Risolti Dipendente\" non è ancora implementata<br>Se vuoi vederla implementata finanzia il nostro progetto!<html>");
			}
		});
		btn_MostraTaskRisoltiDipendente.setBounds(10, 55, 415, 23);
		contentPane.add(btn_MostraTaskRisoltiDipendente);
		
		JButton btn_MostraTaskRisoltiTeam = new JButton("[Da Implementare] Mostra Task Risolti Team");
		btn_MostraTaskRisoltiTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lbl_Errore.setText("<html>La funzione \"Mostra Task Risolti Team\" non è ancora implementata<br>Se vuoi vederla implementata finanzia il nostro progetto!<html>");
			}
		});
		btn_MostraTaskRisoltiTeam.setBounds(10, 100, 415, 23);
		contentPane.add(btn_MostraTaskRisoltiTeam);
		
		JButton btn_MonitoraProgressi = new JButton("Monitora Progressi");
		btn_MonitoraProgressi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraMonitoraProgressi frame=new FinestraMonitoraProgressi();
				lbl_Errore.setText("");
				frame.setVisible(true);
			}
		});
		btn_MonitoraProgressi.setBounds(444, 11, 343, 23);
		contentPane.add(btn_MonitoraProgressi);
		
		JButton btn_SegnalaColloDiBottiglia = new JButton("[Da Implementare] Segnala Collo DI Bottiglia");
		btn_SegnalaColloDiBottiglia.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lbl_Errore.setText("<html>La funzione \"Segnala Collo DI Bottiglia\" non è ancora implementata<br>Se vuoi vederla implementata finanzia il nostro progetto!<html>");
			}
		});
		btn_SegnalaColloDiBottiglia.setBounds(444, 55, 343, 23);
		contentPane.add(btn_SegnalaColloDiBottiglia);
		
		JButton btn_RedistribuzioneRisorse = new JButton("[Da Implementare] Redistribuzione Risorse");
		btn_RedistribuzioneRisorse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lbl_Errore.setText("<html>La funzione \"Redistribuzione Risorse\" non è ancora implementata<br>Se vuoi vederla implementata finanzia il nostro progetto!<html>");
			}
		});
		btn_RedistribuzioneRisorse.setBounds(444, 100, 343, 23);
		contentPane.add(btn_RedistribuzioneRisorse);
		
		
	}

}

