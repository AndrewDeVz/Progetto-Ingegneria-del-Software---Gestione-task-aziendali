package Control;

import Entity.EntityDipendente;
import Entity.EntityTask;
import Entity.EntityTeam;
import Entity.EntityResponsabileTeam;

import communicationEnum.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * <p>È la classe deputata a tutte quelle operazioni che sono di utilità per lo svolgimento dei task</p>
 */
public class ControllerGestioneAzienda {
	
	//per fare in modo che memorizzi il livello del dipendente che fa l'accesso
	private EntityDipendente dipendente;
	private EntityResponsabileTeam responsabileTeam;
	
	private Logger log;
	//Per far si che il Controller sia un Singleton
	private static ControllerGestioneAzienda controllerGestioneAzinda=null;
	
	private ControllerGestioneAzienda() {
		this.dipendente=null;
		this.responsabileTeam=null;
		
		log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	};
	
	/**
	 * <p>Permette creare ControllerGestioneAzienda come singleton, se è la prima volta che viene invocata crea l'istanza</p>
	 * 
	 * @return Un riferimento all'istanza ControllerGestioneAzienda
	 */
	public static ControllerGestioneAzienda getIstance() {
		if(controllerGestioneAzinda==null) {
			controllerGestioneAzinda = new ControllerGestioneAzienda();
		}
		
		return controllerGestioneAzinda;
	}
	
	/**
	 * <p>Permette l'autenticazione del dipendente se non c'è nessuno loggato nel sistema. Se l'autenticazione va a buon dine memorizza il riferimetno all'istanza dipendente che ha fatto l'accesso<br>
	 * Se non va a buon fine lo riporta a null per permettere una nuova autenticazione</p>
	 * 
	 * @param id intero che identifica il dipendente
	 * @param password da verificare se è associata all'Id
	 * @return 0 se id e pasword sono corrette;<br>
	 * -1 se id o password sono errate;<br>
	 * -2 se c'è stato un errore di qualcha altro tipo;
	 */
	public int autenticazioneDipendente(int id,String password) {
		int risu=0;
		
		try {
			if(this.dipendente==null&&this.responsabileTeam==null) {
				this.dipendente=new EntityDipendente();
				if(dipendente.autenticazione(id, password)==0) {
					risu= 0;
				}else {
					this.dipendente=null;
					this.responsabileTeam=null;
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
	 * <p>Permette di assegnare un dipendente ad un team dati i riespettivi identificativi id e nominativo</p>
	 * 
	 * @param id intero che identifica il dipendente
	 * @param nominativo che identifica il team
	 * @return 0 se l'associazione va a buon fine;<br>
	 * -1 se il dipendnete non è stato salvato correttmanete<br>
	 * -2 se l'id è errato;<br>
	 * -3 se il nominativo è errato;<br>
	 * -4 se c'è stato un errore di sistema;
	 */
	public int assegnaDipendenteTeam(int id,String nominativo) {
		
		int risu=0;
		
		try {
			if(id==0) {
				risu=-2;
				log.warning("Id dipendente vuoto o non valido");
			}else if(nominativo.compareTo("")==0) {
				risu=-3;
				log.warning("Nominativo team vuoto o non valido");
			}else {
			
				EntityDipendente dipendente=new EntityDipendente(id);
				EntityTeam team=new EntityTeam(nominativo);
				
				dipendente=dipendente.prelevaDipendenteDaDB();
				team=team.prelevaTeamDaDB();
				
				if(dipendente==null) {
					risu=-2;
					log.warning("Errore nel prelievo del dipendente dal DB");
				}else if(team==null) {
					risu=-3;
					log.warning("Errore nel prelievo del team dal DB");
				}else {
					dipendente.setTeam(team);
					int salvataggio=dipendente.salvaInDB();
					if(salvataggio==0) {
						risu=0;
					}else {
						risu=-1;
						log.warning("Errore durante il salvataggio del dipendente sul database");
					}
				}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			risu=-4;
		}
		
		return risu;
		
	}
	
	/**
	 * Richiede il prelievo di tutti i diepndenti che non sono in un team facendosi fornire la lista di tutti i diepndenti e rimuovendo quelli che già sno assegnati ad un team
	 * 
	 * @return Se ha succeso un ArrayList di HashMap<String,String> che contiene tutte le informazioni dei dipendenti senza team;<br>
	 * Se si solleva un eccezione ritorna una referenza a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTuttiDipendentiSenzaTeam() {
		ArrayList<HashMap<String,String>> mapDipendenti=null;
		
		try {
			ArrayList<EntityDipendente> listaDipendenti=EntityDipendente.listaTuttiDipendenti();
		
			for(int i=0;i<listaDipendenti.size();i++) {
				if(listaDipendenti.get(i).getTeam()!=null) {
					listaDipendenti.remove(i);
					i--;
				}
			}
			
			mapDipendenti=convertArrayDipendentiIntoHashMap(listaDipendenti);
		}catch(Exception e) {
			mapDipendenti=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		return mapDipendenti;
		
	}
	
	/**
	 * Richiede il prelievo di tutti i diepndenti presenti nel sistema
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei dipendenti senza team; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTuttiDipendenti(){
		ArrayList<HashMap<String,String>> mapListaDiepndenti=null;
		
		try {
			ArrayList<EntityDipendente> listaDipendenti=EntityDipendente.listaTuttiDipendenti();		
			mapListaDiepndenti= convertArrayDipendentiIntoHashMap(listaDipendenti);
		}catch(Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			mapListaDiepndenti=null;
		}
		
		return mapListaDiepndenti;
	}
	
	/**
	 * <p>Permette l'autenticazione del responsabile team se non c'è nessuno loggato nel sistema. Se l'autenticazione va a buon dine memorizza il riferimetno all'istanza responsabile team che ha fatto l'accesso<br>
	 * Se non va a buon fine lo riporta a null per permettere una nuova autenticazione</p>
	 * 
	 * @param id intero che identifica il responsabile team
	 * @param password da verificare se è associata all'Id
	 * @return 0 se id e pasword sono corrette;<br>
	 * -1 se id o password sono errate;<br>
	 * -2 se c'è stato un errore di qualcha altro tipo;
	 */
	public int autenticazioneResponsabileTeam(int id,String password) {
		int risu=0;
		
		try {
			if(this.dipendente==null&&this.responsabileTeam==null) {
				this.responsabileTeam=new EntityResponsabileTeam();
				if(responsabileTeam.autenticazione(id, password)==0) {
					risu=0;
				}else {
						this.dipendente=null;
						this.responsabileTeam=null;
						risu=-1;
					}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			risu=-2;
		}
		
		return risu;
	}
	
	/**
	 * Permette la creazione di un taask dati determinati valori in input, gli assegna un codice incrementale e lo memorizza nel sistema
	 * 
	 * @param nome stringa del task che si vuole creare
	 * @param priorita  del task che si vuole creare
	 * @param scadenza del task che si vuole creare
	 * @param descrizione del task che si vuole creare
	 * @return 0 Se il task è stato creato correttamente
	 * -1 Se il task non è stato creato a causa di un problema con il controller o il database;<br>
	 * -3 se il nome è vuoto(salvataggio non effettuato);<br>
	 * -4 se il nome supera i 50 caratteri(salvataggio non effettuato);<br>
	 * -5 se la descrizione supera i 200 caratteri(salvataggio non effettuato);<br>
	 * -6 se si è sollevato un altro tipo di errore nel sistema;
	 */
	public int creaTask(String nome,enumPrioritaTask priorita,LocalDate scadenza,String descrizione) {
		
		try {
			ArrayList<Integer> codiciTaskEsistenti = EntityTask.prelevaTuttiCodiciTask();
			
			int codice=0;
			
			if(codiciTaskEsistenti.isEmpty()) {
				codice=10001;
			}else {
				codice=codiciTaskEsistenti.getLast()+1;
			}
			
			log.info("Al task nome=" + nome + " è stato assegnato codice=" + codice);
			
			if(priorita==null) {
				priorita=enumPrioritaTask.BASSA;
			}
			
			enumStatiTask stato=enumStatiTask.DA_ASSEGNARE;
			
			log.info("Tentativo di creare il task: codice=" + codice +
					", nome=" + nome + ", proiorita="+ priorita+ 
					", stato=" + stato + ", scadenza=" + scadenza +
					", descrizione="+ descrizione);
			
			EntityTask task=new EntityTask(codice,nome,priorita,stato,scadenza,descrizione);	
			
			int salvataggio=task.salvaTaskInDB();
			
			if(salvataggio==-2||salvataggio==-1) {
				return -1; //Poichè -2 è un errore dovuto al codice generato dal sistema e -1 dovuto al database alla boundary si comunica che c'è stato un generico errore di sistema
			}else {
				return salvataggio; //gli altri codici di errore ci interessa che arrivino al bopundary cosi che possa mostrare il corretto messaggio di errore
			}
		}catch(Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			return -6;
		}
		
	}
	
	/**
	 * Richiede il prelievo di tutti i task associati ad un certo dipendente
	 * 
	 * @param id del diepndnete di cui si vogliono conoscere i task assegnati
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni task associati al dipendente; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskPerDipendente(int id){
		ArrayList<HashMap<String,String>> listaMapTask=null;
		
		try {
			ArrayList<EntityTask> listaTask=EntityTask.prelevaTaskPerDipendente(id);
			listaMapTask=convertArrayTaskIntoHashMap(listaTask);
		}catch(Exception e) {
			listaMapTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		
		return listaMapTask;
		
	}
	
	/**
	 * Richiede il prelievo di tutti i task Assegnati con un certo stato
	 * 
	 * @param stato dei task che si vogliono prelevare
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei task con quel determinato stato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskPerStato(enumStatiTask stato){
		ArrayList<HashMap<String,String>> listaMapTask=null;
		
		try {
			ArrayList<EntityTask> listaTask=EntityTask.prelevaTaskPerStato(stato);
			listaMapTask= convertArrayTaskIntoHashMap(listaTask);
		}catch(Exception e) {
			listaMapTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		return listaMapTask;
	}
	
	/**
	 * Richiede il prelievo di tutti i task non assegnati ad nessun dipendente
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei task non associati a nessun dipendente; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTuttiTaskSenzaDipendente(){
		ArrayList<HashMap<String,String>> listMapTask=null;
		
		try {
			ArrayList<EntityTask> listaTask=EntityTask.prelevaTuttiTask();
			
			for(int i=0;i<listaTask.size();i++) {
				if(listaTask.get(i).getDipendente()!=null) {
					listaTask.remove(i);
					i--;
				}
			}
			
			listMapTask= convertArrayTaskIntoHashMap(listaTask);	
		}catch(Exception e) {
			listMapTask= null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		
		return listMapTask;
	}
	
	/**
	 * Richiede il prelievo di tutti i task assegnati ad un determinato dipendente ma non terminati
	 * 
	 * @param id del dipendnete di cui si vopgliono consocere i task associati non terminati
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei task associati al dipendnete e con stato diverso da terminato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskAssegnatiNonTerminati(int id){
		ArrayList<HashMap<String,String>> listaTask=null;
		
		try {//mi prendo tutti i task del dipendente e poi tolgo quelli con stato terminato
			listaTask=mostraTaskPerDipendente(id);
			for(int i=0;i<listaTask.size();i++) {
				if(((String)listaTask.get(i).get("stato")).compareTo(enumStatiTask.TERMINATO.toString())==0) {
					listaTask.remove(i);
					i--;
				}
			}
		}catch(Exception e) {
			listaTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		
		return listaTask;
	}
	
	/**
	 * Richiede il prelievo di tutti i task nel sistema
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei task presenti nel sistema; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTuttiTask(){
		ArrayList<HashMap<String,String>> listaMapTask=null;
		
		try {
			ArrayList<EntityTask> listaTask=EntityTask.prelevaTuttiTask();
			listaMapTask= convertArrayTaskIntoHashMap(listaTask);	
		}catch(Exception e) {
			listaMapTask=null;
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
		}
		
		return listaMapTask;
	}
	
	
	/**
	 * Permette l'assegnazione di un task ad un dipenente date diverse condizioni
	 * 
	 * @param codice del task che si vuole assegnare
	 * @param id del dipendnete a cui si vuole assegnare il task
	 * @return 0 Se il task è asseganto correttamente al dipendente;<br>
	 * -1 Se il task non è stato assgenato a causa di un problema con il controller o il database;<br>
	 * -2 se il livello del dipendente assegnante è minore o uguale di quello assegantario;<br>
	 * -3 se il dipendente che vuole assegnare il task non è in un team;<br>
	 * -4 se il dipendente a cui si vuole assegnare il task non è in un team;<br>
	 * -5 se i due dipendneti non sono nello stesso team;<br>
	 * -6 se il dipendente assegnatario ha già tre task assegnati e non terminati;<br>
	 * -7 se si riscontra un errore genrico nel sistema;<br>
	 * -8 se il task non è stato trovato nel sistema;<br>
	 * -9 se il dipendenete non è stato trovato nel sistema;
	 */
	public int assegnaTaskDipendente(int codice,int id) {
		int risu=0;
		
		try {
			log.info("Tentativo di assegnare il task con codice="+codice+" al dipendente con id="+id+" in corso");
			if(codice==0||id==0) {
				risu=-6;
				log.warning("Tentativo fallito, id o codice non specificati");
			}else {
				EntityTask task=new EntityTask(codice);
				task.prelevaTaskDaDB();
				
				EntityDipendente dipendenteAssegnatario=new EntityDipendente(id);
				dipendenteAssegnatario.prelevaDipendenteDaDB();
				if(task==null) {
					risu=-8;
					log.warning("Tentativo fallito, prelievo dal DB del task non riuscito");
				}else if(dipendenteAssegnatario==null) {
					risu=-9;
					log.warning("Tentativo fallito, prelievo dal DB del dipendente non riuscito");
				}else if(dipendenteAssegnatario.getLivello().compareTo(dipendente.getLivello())>=0) {
					risu= -2;
					log.warning("Tentativo fallito, il livello di id="+dipendenteAssegnatario.getId()+" non è adeguato per assegnare un task a id="+dipendente.getId());
				}else if(dipendenteAssegnatario.getTeam()==null) {
					risu= -3;
					log.warning("Tentativo fallito,id="+dipendenteAssegnatario.getId()+" non è in nessun team");
				}else if(dipendente.getTeam()==null) {
					risu=-4;
					log.warning("Tentativo fallito,id="+dipendente.getId()+" non è in nessun team");
				}else if(dipendenteAssegnatario.getTeam().getNominativo().compareTo(dipendente.getTeam().getNominativo())!=0) {
					risu=-5;
					log.warning("Tentativo fallito, id="+dipendenteAssegnatario.getId()+" non è nello stesso team id="+dipendente.getId()+" non può assegnargli un task");
				}else {
					ArrayList<HashMap<String,String>> listaDipendenti=mostraTaskAssegnatiNonTerminati(id);
					if(listaDipendenti!=null){
						ArrayList<EntityTask> listTask=EntityTask.prelevaTaskPerDipendente(id);
						if(listTask!=null) {
							if(EntityTask.prelevaTaskPerDipendente(id).size()>=3) {
								risu=-6;
								log.warning("Tentativo fallito, id="+dipendente.getId()+" ha già tre task Assegnati");
							}
						}
						if(risu!=-6){
							task.setDipendente(new EntityDipendente(id).prelevaDipendenteDaDB());
							task.setStato(enumStatiTask.ASSEGNATO);
							int salvataggio=task.salvaTaskInDB();
							if(salvataggio==0) {
								risu=0;
							}else {
								risu=-1;
								log.warning("Tentativo fallito, errore nel salvataggio sul DB del task");
							}
						}
					}
				}
			}
		}catch (Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			risu=-7;
		}
		
		return risu;
	}
	
	/**
	 * Mostra i task assegnati al dipendente loggato nel sistema
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutti i task assegnati al dipendente loggato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskAssegnatiLoggato(){
			return mostraTaskPerDipendente(this.dipendente.getId());
	}
	
	/**
	 * Mostra i task assegnati al dipendente loggato nel sistema in stato non terminato
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutti i task assegnati al dipendente loggato  in stato non terminato; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTaskAssegnatiNonTerminatiLoggato(){
		return mostraTaskAssegnatiNonTerminati(this.dipendente.getId());
	}
	
	/**
	 * Permette l'assegnazione di un nuovo stato al task
	 * 
	 * @param codice del task a cui si vuole assegnare lo stato
	 * @param stato che si vuole asseganre
	 * @return 0 Se lo stato è asseganto correttamente al task;<br>
	 * -1 Se il task non è stato trovato nel sistema ;<br>
	 * -2 se il salvataggio del task non è andato a buon fine;<br>
	 * -3 se si è sollevata un eccezione nel sistema;
	 */
	public int assegnaStatoTask(int codice,enumStatiTask stato) {
		int risu=0;
		
		try {
			log.info("Tentativo di modificare lo stato del task codice=" + codice + " in stato=" + stato);
			EntityTask task=new EntityTask(codice);
			task.prelevaTaskDaDB();
			
			if(task==null) {
				risu=-1;
				log.warning("Il task codice=" + codice + " non è stato trovato nel sistema");
			}else {
				task.setStato(stato);
				
				int salvataggio=task.salvaTaskInDB();
				if(salvataggio==0) {
					risu=0;
				}else {
					risu=-2;
					log.warning("Errore nel salvataggio del task sul database");
				}
			}
		}catch(Exception e) {
			log.log(Level.SEVERE,"Si è sollevata un eccezione:",e);
			risu=-3;
		}
		
		return risu;
	}
	
	/**
	 * Permette di creare un  nuovo team
	 * 
	 * 
	 * @param nome del team che si vuole creare
	 * @return 0 Se il team è stato creato correttamente;<br>
	 * -1 Se il team non è stato salvato a causa di un errore nel DB;<br>
	 * -2 se il nominativo del team è vuoto;<br>
	 * -3 se il nominativo del team supera i 50 caratteri;<br>
	 * -4 se esiste già un task con quel nome;<br>
	 * -5 se si è sollevata un eccezione nel sistema;<br>
	 */
	public int creaTeam(String nome) {
		int risu=0;
		
		try {
			EntityTeam team=new EntityTeam(nome);
			risu= team.salvaInDB();
		}catch(Exception e) {
			risu=-5;
		}
		return risu;
	}
	
	/**
	 * Richiede il prelievo di tutti i team nel sistema
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei team presenti nel sistema; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	public ArrayList<HashMap<String,String>> mostraTuttiTeam() {
		ArrayList<EntityTeam> listaTeam=EntityTeam.prelevaTuttiTeam();
		return convertArrayTeamIntoHashMap(listaTeam);
	}
	
	/**
	 * <p>Converte un ArrayList di EntityDipendente in un ArrayList di HashMap che contiene tutte le informazioni dei dipendnenti presenti nell'arrayList in ingresso in formato stringa per poterli restituire al boundary
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei dipendenti presenti presenti nell'arrayList in ingresso; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	ArrayList<HashMap<String,String>> convertArrayDipendentiIntoHashMap(ArrayList<EntityDipendente> listDipendenti){
		ArrayList<HashMap<String,String>> listMapDipendenti= new ArrayList<HashMap<String,String>>();
		
		if(listDipendenti!=null) {
			for(EntityDipendente dipendente:listDipendenti) {
				
				HashMap<String,String> mapDipendenti=new HashMap<String,String>();
				mapDipendenti.put("id", Integer.toString(dipendente.getId()));
				mapDipendenti.put("nome", dipendente.getNome());
				mapDipendenti.put("cognome", dipendente.getCognome());
				mapDipendenti.put("livello", dipendente.getLivello().toString());
				if(dipendente.getTeam()==null) {
					mapDipendenti.put("team", "");
				}else {
					mapDipendenti.put("team", dipendente.getTeam().getNominativo());
				}
				listMapDipendenti.add(mapDipendenti);
			}
		}
		return listMapDipendenti;
	}
	
	/**
	 * <p>Converte un ArrayList di EntityTeam in un ArrayList di HashMap che contiene tutte le informazioni dei Team presenti nell'arrayList in ingresso in formato stringa per poterli restituire al boundary
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei Team presenti nell'ArrayList in ingresso; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	ArrayList<HashMap<String,String>> convertArrayTeamIntoHashMap(ArrayList<EntityTeam> listTeam){
		ArrayList<HashMap<String,String>> listMapTeam= new ArrayList<HashMap<String,String>>();
		
		if(listTeam!=null) {
			for(EntityTeam team:listTeam) {
				
				HashMap<String,String> mapTeam=new HashMap<String,String>();
				mapTeam.put("nominativo", team.getNominativo());
				listMapTeam.add(mapTeam);
			}
		}
		return listMapTeam;
	}
	
	/**
	 * <p>Converte un ArrayList di EntityTask in un ArrayList di HashMap che contiene tutte le informazioni dei Task presenti nell'arrayList in ingresso in formato stringa per poterli restituire al boundary
	 * 
	 * @return Se ha successo restituisce ArrayList di HashMap<String,String> che contiene tutte le informazioni dei Task nell'ArrayList in ingresso; <br>
	 *Se si soleva un errore ritorna un riferimento a null;
	 */
	ArrayList<HashMap<String,String>> convertArrayTaskIntoHashMap(ArrayList<EntityTask> listTask){
		ArrayList<HashMap<String,String>> listMapTask= new ArrayList<HashMap<String,String>>();
		
		if(listTask!=null) {
			for(EntityTask task:listTask) {
				
				HashMap<String,String> mapTask=new HashMap<String,String>();
				mapTask.put("codice", Integer.toString(task.getCodice()));
				mapTask.put("nome", task.getNome());
				mapTask.put("priorita", task.getPriorita().toString() );
				mapTask.put("stato", task.getStato().toString());
				mapTask.put("scadenza", task.getScadenza().toString());
				mapTask.put("descrizione", task.getDescrizione());
				if(task.getDipendente()==null) {
					mapTask.put("dipendente_assegnato", "");
				}else {
					mapTask.put("dipendente_assegnato", task.getDipendente().getNome());
				}
				listMapTask.add(mapTask);
			}
		}
		return listMapTask;
	}
	
	
}
