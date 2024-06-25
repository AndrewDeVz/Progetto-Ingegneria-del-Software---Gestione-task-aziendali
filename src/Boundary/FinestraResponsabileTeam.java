package Boundary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinestraResponsabileTeam extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FinestraResponsabileTeam frame = new FinestraResponsabileTeam();
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
	public FinestraResponsabileTeam() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btn_CreaTeam = new JButton("Crea team");
		btn_CreaTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraCreaTeam frame= new FinestraCreaTeam();
				frame.setVisible(true);
			}
		});
		btn_CreaTeam.setBounds(50, 30, 217, 40);
		contentPane.add(btn_CreaTeam);
		
		JButton btn_AssegnaDipendenteTeam = new JButton("Assegna Dipendente Team");
		btn_AssegnaDipendenteTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FinestraAssegnaDipendenteTeam frame= new FinestraAssegnaDipendenteTeam();
				frame.setVisible(true);
			}
		});
		btn_AssegnaDipendenteTeam.setBounds(50, 116, 217, 45);
		contentPane.add(btn_AssegnaDipendenteTeam);
	}
}
