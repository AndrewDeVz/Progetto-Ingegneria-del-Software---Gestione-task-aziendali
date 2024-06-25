package main;

import Control.ControllerDirezioneAzienda;
import Control.ControllerGestioneAzienda;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Boundary.FinestraAutenticazione;

public class Main {

	public static void main(String[] args) {
		
		Logger logger=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		try {
			FileHandler filehandler=new FileHandler("log.txt");
			logger.addHandler(filehandler);
			SimpleFormatter sf = new SimpleFormatter();
			filehandler.setFormatter(sf);
		}catch(SecurityException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
			
		ControllerDirezioneAzienda.getIstance();
		ControllerGestioneAzienda.getIstance();
		
		FinestraAutenticazione.main(null);

	}

}
