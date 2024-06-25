package Control;

import Entity.EntityTask;
import Entity.EntityManager;

import communicationEnum.enumStatiTask;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * <P>È la classe deputata a svolgere tutte le analisi sullo stato dei task e dei team nell'azienda</p>
 */
public class ControllerDirezioneAzienda {
	//per fare in modo che memorizzi il livello del dipendente che fa l'accesso
	private EntityManager manager;
	
	//Per far si che il Controller sia un Singleton
	private static ControllerDirezioneAzienda controllerDirezioneAzienda=null;
	
	private Logger log;

	private ControllerDirezioneAzienda() {
		
		this.manager=null;
		
		log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	};
	
	public static ControllerDirezioneAzienda getIstance() {
		if(controllerDirezioneAzienda==null) {
			controllerDirezioneAzienda = new ControllerDirezioneAzienda();
		}
		
		return controllerDirezioneAzienda;
	}
	
	/**
	 * <p>Permette l'autenticazione del manager se non c'è nessuno loggato nel sistema. Se l'autenticazione va a buon dine memorizza il riferimetno all'istanza dipendente che ha fatto l'accesso<br>
	 * Se non va a buon fine lo riporta a null per permettere una nuova autenticazione</p>
	 * 
	 * @param id intero che identifica il manager
	 * @param password da verificare se è associata all'Id
	 * @return 0 se id e pasword sono corrette;<br>
	 * -1 se id o password sono errate;<br>
	 * -2 se c'è stato un errore di qualcha altro tipo;
	 */
	public int autenticazioneManager(int id,String password) {
		int risu=0;
		
		try {
			if(this.manager==null) {
				this.manager=new EntityManager();
				if(manager.autenticazione(id, password)==0) {
					risu= 0;
				}else {
					this.manager=null;
					risu=-1;
				}
			}
		}catch(Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			risu=-2;
		}
		return risu;
	}
	
	/**
	 * Richiede il prelievo di tutti i task in stato terminato presenti nel sistema
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni task con stato terminato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskTerminati(){
		ArrayList<HashMap<String,String>> listMapTask=null;
		
		try {
			ArrayList<EntityTask> listaTask=EntityTask.prelevaTuttiTask();
			
			for(int i=0;i<listaTask.size();i++) {
				if(listaTask.get(i).getStato().compareTo(enumStatiTask.TERMINATO)!=0) {
					listaTask.remove(i);
					i--;
				}
			}
		
			listMapTask= ControllerGestioneAzienda.getIstance().convertArrayTaskIntoHashMap(listaTask);	
		}catch(Exception e) {
			listMapTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		return listMapTask;
	}
	
	
	/**
	 * Restituisce il numero di task per ogni stato possibile
	 * 
	 * @return Se ha successo restituisce una HashMap<enumStatiTask,Integer> che contiene il numero di task realtivi ad ogni stato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public HashMap<enumStatiTask,Integer> monitoraProgressi(){
		HashMap<enumStatiTask,Integer> conteggioTask=null;
		
		try {
			conteggioTask=new HashMap<enumStatiTask,Integer>();
			conteggioTask.put(enumStatiTask.DA_ASSEGNARE,EntityTask.contaTaskPerStato(enumStatiTask.DA_ASSEGNARE));
			conteggioTask.put(enumStatiTask.ASSEGNATO,EntityTask.contaTaskPerStato(enumStatiTask.ASSEGNATO));
			conteggioTask.put(enumStatiTask.IN_LAVORAZIONE,EntityTask.contaTaskPerStato(enumStatiTask.IN_LAVORAZIONE));
			conteggioTask.put(enumStatiTask.TERMINATO,EntityTask.contaTaskPerStato(enumStatiTask.TERMINATO));
			
			return conteggioTask;	
		}catch(Exception e) {
			conteggioTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		return conteggioTask;
	}
}

