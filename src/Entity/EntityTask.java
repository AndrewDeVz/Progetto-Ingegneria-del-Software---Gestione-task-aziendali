package Entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;

import communicationEnum.*;
import Database.TaskDAO;

/**
 * <p>Classe deputata a memorizzare nel sistema un Entità T</p>
 */
public class EntityTask {
	private int codice;
	private String nome;
	private enumPrioritaTask priorità;
	private enumStatiTask stato;
	private LocalDate scadenza;
	private String descrizione;
	private EntityDipendente dipendente_assegnato;
	
	private static Logger log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public EntityTask(int codice, String nome, enumPrioritaTask priorita, enumStatiTask stato, LocalDate scadenza, String descrizione, EntityDipendente dipendente) {
		this.setCodice(codice);
		this.setNome(nome);
		this.setPriorita(priorita);
		this.setStato(stato);
		this.setScadenza(scadenza);
		this.setDescrizione(descrizione);
		this.setDipendente(dipendente);
	}
	
	public EntityTask(int codice, String nome, enumPrioritaTask priorita, enumStatiTask stato, LocalDate scadenza, String descrizione) {
		this(codice,nome,priorita,stato,scadenza,descrizione,null);
	}
	
	public EntityTask(int codice) {
		this(codice,"",null,null,null,"");
	}	
	
	public EntityTask() {
		this(0);
	}	
	
	
	public int getCodice() {return codice;}
	public void setCodice(int codice) {
		if(codice<0) {
			this.codice=0;
		}else {
			this.codice = codice;
		}
	}
		
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public enumPrioritaTask getPriorita() {return priorità;}
	public void setPriorita(enumPrioritaTask priorità) {this.priorità = priorità;}
	
	public enumStatiTask getStato() {return stato;}	
	public void setStato(enumStatiTask stato) {this.stato = stato;}
	
	public LocalDate getScadenza() {return scadenza;}
	public void setScadenza(LocalDate scadenza) {this.scadenza = scadenza;}
	
	public String getDescrizione() {return descrizione;}
	public void setDescrizione(String descrizione) {this.descrizione = descrizione;}
	
	public EntityDipendente getDipendente() {return dipendente_assegnato;}
	public void setDipendente(EntityDipendente dipendente) {this.dipendente_assegnato = dipendente;}
	
	public EntityTask copyFrom(EntityTask task) {
		this.setCodice(task.getCodice());
		this.setNome(task.getNome());
		this.setPriorita(task.getPriorita());
		this.setStato(task.getStato());
		this.setScadenza(task.getScadenza());
		this.setDescrizione(task.getDescrizione());
		this.setDipendente(task.getDipendente());
		
		return this;
	}
	
	/**
	 * <p>Salva l'istanza chiamante nel database dopo aver verificato che il codice non sia negativo o 0 (valore non accettato per quell'attributo).</p>
	 * Effettua una verifica dell'esistenza di questo task nel db mediante la funzione {@link Database.TaskDAO.selectByID}. <br>
	 * Se la verifica ha esito positivo effettua un nuovo inserimento mediante {@link Database.taskDAO.insertIntoDB}. <br>
	 * Altrimenti effettua un update mediante {@link Database.taskDAO.insertIntoDB}
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);<br>
	 * -2 se il codice non è valido(salvataggio non effettuato);<br>
	 * -3 se il nome è vuoto(salvataggio non effettuato);<br>
	 * -4 se il nome supera i 50 caratteri(salvataggio non effettuato);<br>
	 * -5 se la descrizione supera i 200 caratteri(salvataggio non effettuato); 
	 */
	public int salvaTaskInDB() {
		int resu=0;
		
		TaskDAO taskDAO=new TaskDAO();
		HashMap<String,Object> mapTask=convertIntoHashMapForDB(this);
		
		log.info("Tentativo di salvataggio sul DB del task codice=" + Integer.toString(this.getCodice()) + " in corso" );
		
		if(this.getCodice()==0) {
			resu= -2; //Se non ha codice (0 equivale ad un codice non valido)
			log.warning("Il task ha codice non valido");
		}else if(this.getNome().compareTo("")==0){
			resu= -3; //Se il nome è vuoto
			log.warning("Il task ha nome vuoto");
		}else if(this.getNome().length()>50) {
			resu= -4; //Se il nome ha più di 50 caratteri
			log.warning("Il task ha nome con più di 50 caratteri");
		}else if(this.getDescrizione().length()>200) {
			resu= -5;
			log.warning("Il task ha descrizione con più di 200 caratteri");
		}else if(taskDAO.selectByCodice(this.getCodice())==null) {
			resu=taskDAO.insertIntoDB(mapTask);
			log.info("Il task è sato salvato");
		}else {
			resu=taskDAO.updateIntoDB(mapTask);
		}
		
		return resu;

	}
	
	/**
	 *  <p>Effettua la rimozione del Task con l'id dell'istanza chiamante dal database mediante {@link Database.TaskDAO.deleteByCodice}</p>
	 *  
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public int rimuoviDaDB() {
		TaskDAO taskDAO=new TaskDAO();
		
		log.info("Tentativo di rimozione dal DB del task codice=" + Integer.toString(this.getCodice()) + " in corso" );
		
		return taskDAO.deleteByCodice(this.getCodice());
	}
	
	/**
	 *<p>Effettua la rimozione di tutti i Task dal DB mediante {@link Database.TaskDAO.deleteAll}</p>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br>
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public static int rimuoviTuttiDaDB() {
		TaskDAO taskDAO=new TaskDAO();
		
		log.info("Tentativo di rimozione dal DB di tutti i task in corso" );
		
		return taskDAO.deleteAll();
	}
	
	/**
	 * <p>Preleva il dipendente richiesto dal database se presente mediante {@link Database.TaskDAO.selectByCodice}</p>
	 * 
	 * @param id del dipendente da prelevare
	 * @return Un riferimento all'istanza di EntityDipendnete chiamante in caso di successo;<br>
	 * Un puntatore a null in caso di fallimento;
	 */
	public EntityTask prelevaTaskDaDB() {
		
		EntityTask task=new EntityTask();
		TaskDAO taskDAO=new TaskDAO();
		
		log.info("Tentativo di prelievo dal DB del task codice=" + this.getCodice());
		
		HashMap<String,Object> mapTask=taskDAO.selectByCodice(codice);
		
		if(mapTask==null) {
			task = null;
		}else{
			task=convertMapTaskIntoEntityTask(mapTask,task);
			if(task!=null) {
				this.copyFrom(task);
				task=this;
			}else {
				log.warning("Tentativo fallito,errore nella conversione dei dati passati dal database");
			}
		}
		
		return task;
	}

	/**
	 * <p>Preleva dal DB una lista di tutti i task presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di EntityTask con gli attributi persenti nel DB;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityTask> prelevaTuttiTask(){
		
		log.info("Tentativo di prelievo di tutti i task in corso");
		
		TaskDAO taskDAO=new TaskDAO();
		ArrayList<HashMap<String,Object>>listMapTask=taskDAO.selectAll();
		ArrayList<EntityTask> listaTask=convertMapListIntoEntityTaskList(listMapTask);
		
		return listaTask;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i codici dei task presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di interi con i codici dei task;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<Integer> prelevaTuttiCodiciTask(){
		
		log.info("Tentativo di prelievo di tutti i codici dei task in corso");
		
		TaskDAO taskDAO=new TaskDAO();
		ArrayList<Integer> listaCodiciTask=taskDAO.selectAllCodici();
	
		return listaCodiciTask;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i task presenti con un determinato stato</p>
	 * 
	 * @param stato enumStatiTask che specifica lo stato dei task da prelevare
	 * @return In caso di successo un riferimento ad una ArrayList di EntityTask con i task aventi stato specificato;<br>
	 * In caso di falliemnto un puntatore a null
	 */
	public static ArrayList<EntityTask> prelevaTaskPerStato(enumStatiTask stato){
		
		log.info("Tentativo di prelievo di tutti i task con stato=" + stato + " in corso");
		
		TaskDAO taskDAO=new TaskDAO();
		ArrayList<HashMap<String,Object>>listMapTask=taskDAO.selectByStato(stato);
		ArrayList<EntityTask> listaTask=convertMapListIntoEntityTaskList(listMapTask);
		
		return listaTask;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i task presenti asseganto ad un determinato dipendente</p>
	 * 
	 * @param id_dipendnte int che specifica l'ID del dipendente di cui si vogliono prelevare i task a cui è associato
	 * @return In caso di successo un riferimento ad una ArrayList di EntityTask con i task associati al dipendente specificato;<br>
	 * In caso di falliemento un puntatore a null
	 */
	public static ArrayList<EntityTask> prelevaTaskPerDipendente(int id_dipendente){
		
		log.info("Tentativo di prelievo di tutti i task associati al dipendente con id=" + id_dipendente + " in corso");
		
		TaskDAO taskDAO=new TaskDAO();
		ArrayList<HashMap<String,Object>>listMapTask=taskDAO.selectByDipendenteAssegnato(id_dipendente);
		ArrayList<EntityTask> listaTask=convertMapListIntoEntityTaskList(listMapTask);
		
		return listaTask;
	}
	
	/**
	 * <p>Restituisce il numero di Task con un determinato stato </p>
	 * 
	 * @param stato enumStatiTask che specifica lo stato dei task da prelevare
	 * @return In caso di successo il numero di task con quello stato presenti;<br>
	 * -1 In caso di fallimento;
	 */
	public static int contaTaskPerStato(enumStatiTask stato) {
		
		log.info("Conteggio di tutti i task con stato=" + stato + " in corso");
		
		return (new TaskDAO()).countTasksByStato(stato);
	}	
	
	/**
	 * <p>Preleva i dati da una HashMap con i dati di un task(tipo restituito da alcuni metodi delle classi del package Database) e li copia nell'istanza task data in input</p>
	 * 
	 * @param mapTask HashMap con i dati del task
	 * @param task EntityTask in cui copiare i dati prelevati dalla mapTask
	 * @return Un riferimento al task dato in input
	 */
	private static EntityTask convertMapTaskIntoEntityTask(HashMap<String,Object> mapTask,EntityTask task){
		
		if(mapTask!=null&&!mapTask.isEmpty()) {
			task.setCodice((Integer) mapTask.get("codice"));
			task.setNome((String) mapTask.get("nome"));
			task.setPriorita((enumPrioritaTask) mapTask.get("priorita"));
			task.setStato((enumStatiTask) mapTask.get("stato"));
			task.setScadenza((LocalDate) mapTask.get("scadenza"));
			task.setDescrizione((String) mapTask.get("descrizione"));
			if((Integer) mapTask.get("dipendente_assegnato")!=0) {
				EntityDipendente dipendente_asseganatario=new EntityDipendente((Integer) mapTask.get("dipendente_assegnato"));
				dipendente_asseganatario.prelevaDipendenteDaDB();
				task.setDipendente(dipendente_asseganatario);
			}else {
				task.setDipendente(null);
			}
		} 
		
		return task;
	}
	
	/**
	 * <p>Preleva i dati da un ArrayList di HashMap con i dati di task(tipo restituito da alcuni metodi delle classi del package Database) e crea un ArrayList di istanze EntityTask</p>
	 * 
	 * @param listMapTask ArrayList di HashMap con i dati dei dipendenti
	 * @return In caso di successo un riferimento all'ArrayList di EntityTask creata;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	private static ArrayList<EntityTask> convertMapListIntoEntityTaskList(ArrayList<HashMap<String,Object>> listMapTask){
		ArrayList<EntityTask> listTask=null;
		
		if(listMapTask!=null&&!listMapTask.isEmpty()) {
			listTask=new ArrayList<EntityTask>();
			
			for(HashMap<String,Object> mapTask: listMapTask){
				EntityTask task=new EntityTask();
				convertMapTaskIntoEntityTask(mapTask,task);
				listTask.add(task);
			} 
			
		}
		
		return listTask;
	}

	/**
	 * <p>Trasforma gli attributi di un istanza dipendente passata in input in una HashMap (tipo da immettere all'invocazione di alcuni metodi del package Database)</p>
	 * 
	 * @param dipendente EntityDipendente da cui prelevare i dati
	 * @return Riferimento alla HashMap creata
	 */
	private static HashMap<String,Object> convertIntoHashMapForDB (EntityTask task){
		
		HashMap<String,Object> mapTask=new HashMap<String,Object>();
		
		mapTask.put("codice", task.getCodice());
		mapTask.put("nome", task.getNome());
		mapTask.put("priorita", task.getPriorita());
		mapTask.put("stato", task.getStato());
		mapTask.put("scadenza", task.getScadenza());
		mapTask.put("descrizione", task.getDescrizione());
		if(task.getDipendente()!=null) {
			mapTask.put("dipendente_assegnato", task.getDipendente().getId());
		}else {
			mapTask.put("dipendente_assegnato", 0);
		}
		
		return mapTask;
	}
}

