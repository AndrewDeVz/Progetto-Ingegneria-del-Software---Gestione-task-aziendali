package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import Database.ManagerDAO;

/**
 * <p>Classe deputata a memorizzare nel sistema un Entità Manager</p>
 */
public class EntityManager {
	
	private int id;
	private String password;
	private String nome;
	private String cognome;
	
	private static Logger log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public EntityManager(int id, String password, String nome, String cognome) {
		this.setId(id);
		this.setPassword(password);
		this.setNome(nome);
		this.setCognome(cognome);
	}
	
	public EntityManager(int id) {
		this(id,"","","");
	}
	
	public EntityManager() {
		this(0);
	}
	
	
	public int getId() {return id;}
	public void setId(int id) {
		if(id<0) {
			this.id=0;
		}else {
			this.id = id;
		}
	}
	
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getCognome() {return cognome;}
	public void setCognome(String cognome) {this.cognome = cognome;}
	
	public EntityManager copyFrom(EntityManager manager) {
		this.setId(manager.getId());
		this.setPassword(manager.getPassword());
		this.setNome(manager.getNome());
		this.setCognome(manager.getCognome());
		
		return this;
	}
	
	
	/**
	 * <p>Salva l'istanza chiamante nel database dopo aver verificato che l'ID non sia negativo o 0 (valori non accettati per quell'attributo).</p>
	 * Effettua una verifica dell'esistenza di questo dipendente nel db mediante la funzione {@link Database.ManagerDAO.selectByID}. <br>
	 * Se la verifica ha esito positivo effettua un nuovo inserimento mediante {@link Database.ManagerDAO.insertIntoDB}. <br>
	 * Altrimenti effettua un update mediante {@link Database.DipendenteDAO.insertIntoDB}
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 *-1 se il database ha riscontrato un errore(salvataggio non effettuato);<br> 
	 *-2 se l'id sell'istanza da salvare non è valido(salvataggio non effettuato);<br>
	 *-3 sel la password non è valida(salvataggio non effettuato); 
	 */
	public int salvaInDB(){
		int res=0;
		
		ManagerDAO managerDAO= new ManagerDAO();
		HashMap<String,Object> mapManager = convertIntoHashMapForDB(this);
		
		log.info("Tentativo di salvataggio sul DB del manager id=" + Integer.toString(this.getId()) + " in corso" );
		
		if(this.getId()==0) {
			res=-2;
			log.warning("Il manager che si vuole salvare ha id non valido");
		}else if(this.getPassword().compareTo("")==0) {
			res=-3;
			log.warning("Il manager che si vuole salvare non ha una password");
		}else if(managerDAO.selectById(this.getId())==null) {
			res=managerDAO.insertIntoDB(mapManager);
		}else {
			res=managerDAO.updateIntoDB(mapManager);
		}
		return res;		
	}
	
	/**
	 *  <p>Effettua la rimozione del manager con l'id dell'istanza chiamante dal database mediante {@link Database.ManagerDAO.deleteByID}</p>
	 *  
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public int rimuoviDaDB() {
		ManagerDAO managerDAO= new ManagerDAO();
		
		log.info("Tentativo di rimozione dal DB del manager id=" + Integer.toString(this.getId()) + " in corso" );
		
		return managerDAO.deleteByID(this.getId());
	}
	
	/**
	 *<p>Effettua la rimozione di tutti i manager dal DB mediante {@link Database.ManagerDAO.deleteAll}</p>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br>
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public static int rimuoviTuttiDaDB() {
		ManagerDAO managerDAO = new ManagerDAO();
		
		log.info("Tentativo di rimozione dal DB di tutti i dipendenti in corso" );
		
		return managerDAO.deleteAll();
	}
	
	/**
	 * <p>Preleva il manager richiesto dal database se presente {@link Database.ManagerDAO.selectById}</p>
	 * <p>L'istanza deve avere già un id valido per poter avere successo la query</p>
	 * 
	 * @return Un riferimetno all'istanza chiamante EntityManager chiamante in caso di successo<br>
	 * Un puntatore a null in caso di fallimento
	 */
	public EntityManager prelevaManagerDaDB() {
		
		EntityManager manager = new EntityManager();
		ManagerDAO managerDAO=new ManagerDAO();
		
		log.info("Tentativo di prelievo dal DB del manager id=" + this.getId());
		
		HashMap<String,Object> mapManager=managerDAO.selectById(this.getId());

		if(mapManager==null) {
			manager = null;
			log.warning("Tentativo fallito,utente non trovato o errore nel database");
		}else{
			manager=convertMapManagerIntoEntityManger(mapManager,manager);
			if(manager!=null) {
				this.copyFrom(manager);
				manager=this;
			}else {
				log.warning("Tentativo fallito,errore nella conversione dei dati passati dal database");
			}
		}
		
		return manager;
	}
	
	/**
	 * <p>Verifica la presenza del manager associato all'id nel database e successivamente verifica la corrisdpondenza tra la password in input e quella memorizzata</p>
	 * 
	 * @param id del manager da verificare
	 * @param password di cui si vuole verificare se associata all'id
	 * @return 0 se il manager è autenticato<br>
	 * -1 se la password è errata<br>
	 * -2 se il manager non è stato trovato o c'è stato un errore nel db
	 */
	public int autenticazione(int id,String password) {
		
		log.info("Tentativo di autenticazione in corso");
		
		int ret=0;
		this.setId(id);
		
		EntityManager manager= prelevaManagerDaDB();
		if(manager==null) {
			ret=-2;
		}else if(this.getPassword().compareTo(password)!=0) {
			ret=-1;
			log.warning("Tentativo di autenticazione fallito poichè la password non coincide con quella associata all'id="+id);
		}
		
		return ret;	
		
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i manager presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di istanze EntityDipendneti con gli attributi prelevati dal db;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityManager> listaTuttiManager(){
		
		log.info("Tentativo di prelievo di tutti i manager in corso");
		
		ManagerDAO managerDAO= new ManagerDAO();
		ArrayList<HashMap<String,Object>> listMapManager = managerDAO.selectAll();
		ArrayList<EntityManager> listManager = convertMapListIntoEntityManager(listMapManager);
		
		return listManager;
	}
	
	
	/**
	 * <p>Preleva i dati da una HashMap con i dati di un manager(tipo restituito da alcuni metodi delle classi del package Database) e li copia nell'istanza manager data in input</p>
	 * 
	 * @param mapManager HashMap con i dati del manager
	 * @param manager EntityManager in cui copiare i dati prelevati dalla mapManager
	 * @return Un riferimento al manager dato in input
	 */
	private static EntityManager convertMapManagerIntoEntityManger(HashMap<String,Object> mapManager,EntityManager manager){
		
		if(mapManager!=null&&!mapManager.isEmpty()) {
			manager.setId((Integer) mapManager.get("id"));
			manager.setPassword((String) mapManager.get("password"));
			manager.setNome((String) mapManager.get("nome"));
			manager.setCognome((String) mapManager.get("cognome"));
		}
			
		return manager;
	};	
	
	/**
	 * <p>Preleva i dati da un ArrayList di HashMap con i dati di un manager(tipo restituito da alcuni metodi delle classi del package Database) e crea un ArrayList di istanze EntityManager</p>
	 * 
	 * @param listMapManager ArrayList di HashMap con i dati dei manager
	 * @return In caso di successo un riferimento all'ArrayList di manager creata;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	private static ArrayList<EntityManager> convertMapListIntoEntityManager(ArrayList<HashMap<String,Object>> listaMapManager){
		ArrayList<EntityManager> listaManager =null;
		
		if(listaMapManager!=null&&!listaMapManager.isEmpty()) {
			listaManager = new ArrayList<EntityManager>();
			
			for(HashMap<String,Object> mapManager : listaMapManager) {
				EntityManager manager= new EntityManager();
				convertMapManagerIntoEntityManger(mapManager,manager);
				listaManager.add(manager);
			}
		}
		return listaManager;
	};
	
	/**
	 * <p>Trasforma gli attributi di un istanza manager in una HashMap (tipo da immettere all'invocazione di alcuni metodi del package Database)</p>
	 * 
	 * @param manager EntityDipendente da cui prelevare i dati
	 * @return Riferimento alla HashMap creata
	 */
	private static HashMap<String,Object> convertIntoHashMapForDB (EntityManager manager){
		
		HashMap<String,Object> mapManager=new HashMap<String,Object>();
		
		mapManager.put("id", manager.getId());
		mapManager.put("password", manager.getPassword());
		mapManager.put("nome", manager.getNome());
		mapManager.put("cognome", manager.getCognome());
		
		return mapManager;
		
	}

}
