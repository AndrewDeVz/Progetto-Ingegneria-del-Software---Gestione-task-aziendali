package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.LogManager;

import communicationEnum.enumLivello;
import Database.DipendenteDAO;

/**
 * <p>Classe deputata a memorizzare nel sistema un Entità Task</p>
 */
public class EntityDipendente {
	
	private int id;
	private String password;
	private String nome;
	private String cognome;
	private enumLivello livello;
	private EntityTeam team;
	
	private static Logger log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public EntityDipendente(int id, String password, String nome, String cognome, enumLivello livello, EntityTeam team) {
		this.setId(id);
		this.setPassword(password);
		this.setNome(nome);
		this.setCognome(cognome);
		this.setLivello(livello);
		
	}
	
	public EntityDipendente(int id) {
		this(id,"","","",(enumLivello)null,(EntityTeam)null);
		
	}
	
	public EntityDipendente() {
		this(0,"","","",(enumLivello)null,(EntityTeam)null);
		
	}

	public int getId() {return id;}
	public void setId(int id) {
		if(id<=0)
			this.id = 0;
		else
			this.id = id;
	}
	
	public String getPassword() {return password;}	
	public void setPassword(String password) {this.password = password;}
	
	public String getNome() {return nome;}	
	public void setNome(String nome) {this.nome = nome;}
	
	public String getCognome() {return cognome;}	
	public void setCognome(String cognome) {this.cognome = cognome;}
	
	public enumLivello getLivello() {return livello;}
    public void setLivello(enumLivello livello) {this.livello = livello;}
	
	public EntityTeam getTeam() {return team;}	
	public void setTeam(EntityTeam team) {this.team = team;}
	
	public EntityDipendente copyFrom(EntityDipendente dipendente) {
		this.setId(dipendente.getId());
		this.setPassword(dipendente.getPassword());
		this.setNome(dipendente.getNome());
		this.setCognome(dipendente.getCognome());
		this.setLivello(dipendente.getLivello());
		this.setTeam(dipendente.getTeam());
		
		return this;
	}
	
	/**
	 * <p>Salva l'istanza chiamante nel database dopo aver verificato che l'ID non sia negativo o 0 (valori non accettati per quell'attributo).</p>
	 * Effettua una verifica dell'isistenza di questo dipendente nel db mediante la funzione {@link Database.DipendenteDAO.selectByID}. <br>
	 * Se la verifica ha esito positivo effettua un nuovo inserimento mediante {@link Database.DipendenteDAO.insertIntoDB}. <br>
	 * Altrimenti effettua un update mediante {@link Database.DipendenteDAO.insertIntoDB}
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 *-1 se il database ha riscontrato un errore(salvataggio non effettuato);<br> 
	 *-2 se l'id sell'istanza da salvare non è valido(salvataggio non effettuato);<br>
	 *-3 sel la password non è valida(salvataggio non effettuato); 
	 */
	public int salvaInDB(){
		int res=0;
		
		DipendenteDAO dipendenteDAO= new DipendenteDAO();
		HashMap<String,Object> mapDipendente = convertIntoHashMapForDB(this);
		
		log.info("Tentativo di salvataggio sul DB del dipendente id=" + Integer.toString(this.getId()) + " in corso" );
		
		if(this.getId()==0) {
			res=-2;
			log.warning("Il dipendente che si vuole salvare ha id non valido");
		}else if(this.getPassword().compareTo("")==0) {
			res=-3;
			log.warning("Il dipendente che si vuole salvare non ha una password");
		}else if(dipendenteDAO.selectById(this.getId())==null) {
			res=dipendenteDAO.insertIntoDB(mapDipendente); //Poichè voglio che sia ritornato 0 se l'operazione ha successo, -1 se è stata catturata un eccezione (esattamente i valori che ritorna la funzione insertiIntoDB)
		}else {
			res=dipendenteDAO.updateIntoDB(mapDipendente);
		}
		
		return res;
				
	}
	
	/**
	 *  <p>Effettua la rimozione del Dipendente con l'id dell'istanza chiamante dal database mediante {@link Database.DipendenteDAO.deleteByID}</p>
	 *  
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public int rimuoviDaDB() {
		DipendenteDAO databaseDAO = new DipendenteDAO();
		
		log.info("Tentativo di rimozione dal DB del dipendnte id=" + Integer.toString(this.getId()) + " in corso" );
		
		return databaseDAO.deleteByID(this.getId());
	}
	
	/**
	 *<p>Effettua la rimozione di tutti i Dipendenti dal DB mediante {@link Database.DipendenteDAO.deleteAll}</p>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br>
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public static int rimuoviTuttiDaDB() {
		DipendenteDAO databaseDAO = new DipendenteDAO();
		
		log.info("Tentativo di rimozione dal DB di tutti i dipendenti in corso" );
		
		return databaseDAO.deleteAll();
	}
	
	
	/**
	 * <p>Preleva il dipendente richiesto dal database se presente {@link Database.DipendenteDAO.selectById}</p>
	 * <p>L'istanza deve avere già un id valido per poter avere successo la query</p>
	 * 
	 * @return Un riferimento all'istanza chiamante EntityDipendente chiamante in caso di successo<br>
	 * Un puntatore a null in caso di fallimento
	 */
	public EntityDipendente prelevaDipendenteDaDB() {
		
		EntityDipendente dipendente = new EntityDipendente();
		DipendenteDAO dipendenteDAO = new DipendenteDAO();
		
		log.info("Tentativo di prelievio dal DB del dipendente id=" + this.getId());
		
		HashMap<String,Object> mapDipendente=dipendenteDAO.selectById(this.getId());
		
		//Se già il database ha fallito mi fermo, non faccio il tentativo di convertire la map
		if(mapDipendente==null) {
			dipendente = null;
			log.warning("Tentativo fallito,utente non trovato o errore nel database");
		}else{
			dipendente=convertMapDipendenteIntoEntityDipendente(mapDipendente,dipendente);
			if(dipendente!=null) {
				this.copyFrom(dipendente);
				dipendente=this;
			}else {
				log.warning("Tentativo fallito,errore nel nella conversione dei dati passati dal database");
			}
		}
		
		return dipendente;
	}
	
	/**
	 * <p>Verifica la presenza del dipendente associato all'id nel database e successivamente verifica la corrispondenza tra la password in input e quella memorizzata</p>
	 * 
	 * @param id del dipendente da verificare
	 * @param password di cui si vuole verificare se associata all'id
	 * @return 0 se il dipendente è autenticato<br>
	 * -1 se la password è errata;<br>
	 * -2 se il dipendente non è stato trovato o c'è stato un errore nel db;
	 */
	public int autenticazione(int id,String password) {
		
		log.info("Tentativo di autenticazione in corso");
		
		int ret=0;
		this.setId(id);
		
		EntityDipendente dipendente= prelevaDipendenteDaDB();
		if(dipendente==null) {
			ret=-2;
		}else if(this.getPassword().compareTo(password)!=0) {
			ret=-1;
			log.warning("Tentativo di autenticazione fallito poichè la password non coincide con quella associata all'id="+id);
		}
		
		return ret;	
		
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i dipendenti presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di istanze EntityDipendenti con gli attributi prelevati dal db;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityDipendente> listaTuttiDipendenti(){
		
		log.info("Tentativo di prelievo di tutti i dipendenti in corso");
		
		DipendenteDAO dipendenteDAO= new DipendenteDAO();
		ArrayList<HashMap<String,Object>> listaMapDipendenti = dipendenteDAO.selectAll();
		ArrayList<EntityDipendente> listaDipendenti = convertMapListIntoEntityDipendente(listaMapDipendenti);

		return listaDipendenti;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i dipendenti presenti in un dato team</p>
	 * 
	 * @param team a cui devono appartenere i dipendenti desiderati
	 * @return In caso di successo un riferimento ad una ArrayList di istanze EntityDipendneti con gli attributi prelevati dal db e appartenenti al team specificato<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityDipendente> listaTuttiDipendentiPerTeam(String team){
		
		log.info("Tentativo di prelievo di tutti gli utenti del team " + team + " in corso");
		
		DipendenteDAO dipendenteDAO= new DipendenteDAO();
		ArrayList<HashMap<String,Object>> listaMapDipendenti = dipendenteDAO.selectByTeam(team);
		ArrayList<EntityDipendente> listaDipendenti = convertMapListIntoEntityDipendente(listaMapDipendenti);
		
		return listaDipendenti;
		
	}
	
	/**
	 * <p>Preleva i dati da una HashMap con i dati di un dipendente(tipo restituito da alcuni metodi delle classi del package Database) e li copia nell'istanza dipendente data in input</p>
	 * 
	 * @param mapDipendente HashMap con i dati del dipendente
	 * @param dipendente EntityDipendente in cui copiare i dati prelevati dalla mapDipendente
	 * @return Un riferimento al dipendente dato in input
	 */
	private static EntityDipendente convertMapDipendenteIntoEntityDipendente(HashMap<String,Object> mapDipendente,EntityDipendente dipendente){
		
		if(mapDipendente!=null&&!mapDipendente.isEmpty()) {
			dipendente.setId((Integer) mapDipendente.get("id"));
			dipendente.setPassword((String) mapDipendente.get("password"));
			dipendente.setNome((String) mapDipendente.get("nome"));
			dipendente.setCognome((String) mapDipendente.get("cognome"));
			dipendente.setLivello((enumLivello) mapDipendente.get("livello"));
			if(((String) mapDipendente.get("team")).compareTo("")!=0) {
				dipendente.setTeam(new EntityTeam((String) mapDipendente.get("team")));
			} else {
				dipendente.setTeam(null);
			}
		}
			
		return dipendente;
	};	
	
	/**
	 * <p>Preleva i dati da un ArrayList di HashMap con i dati di un dipendnete(tipo restituito da alcuni metodi delle classi del package Database) e crea un ArrayList di istanze EntityDipendente</p>
	 * 
	 * @param listaMapDipendenti ArrayList di HashMap con i dati dei dipendenti
	 * @return In caso di successo un riferimento all'ArrayList di dipendenti creata<br>In caso di fallimento un puntatore a null
	 */
	private static ArrayList<EntityDipendente> convertMapListIntoEntityDipendente(ArrayList<HashMap<String,Object>> listaMapDipendenti){
		ArrayList<EntityDipendente> listaDipendenti =null;
		
		if(listaMapDipendenti!=null&&!listaMapDipendenti.isEmpty()) {
			listaDipendenti = new ArrayList<EntityDipendente>();
			
			for(HashMap<String,Object> mapDipendente : listaMapDipendenti) {
				EntityDipendente dipendente= new EntityDipendente();
				convertMapDipendenteIntoEntityDipendente(mapDipendente,dipendente);
				listaDipendenti.add(dipendente);
			}
		}
		return listaDipendenti;
	};
	
	/**
	 * <p>Trasforma gli attributi di un istanza dipendente passata in input in una HashMap (tipo da immettere all'invocazione di alcuni metodi del package Database)</p>
	 * 
	 * @param dipendente EntityDipendente da cui prelevare i dati
	 * @return Riferimento alla HashMap creata
	 */
	private static HashMap<String,Object> convertIntoHashMapForDB (EntityDipendente dipendente){
		
		HashMap<String,Object> mapDipendente=new HashMap<String,Object>();
		
		mapDipendente.put("id", dipendente.getId());
		mapDipendente.put("password", dipendente.getPassword());
		mapDipendente.put("nome", dipendente.getNome());
		mapDipendente.put("cognome", dipendente.getCognome());
		mapDipendente.put("livello", dipendente.getLivello());
		if(dipendente.getTeam().getNominativo().compareTo("")!=0) {
			mapDipendente.put("team", dipendente.getTeam().getNominativo());
		}else {
			mapDipendente.put("team", "");
		}
		
		return mapDipendente;
		
	}
	
}

