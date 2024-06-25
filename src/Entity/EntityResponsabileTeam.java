package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import Database.ResponsabileTeamDAO;

/**
 * <p>Classe deputata a memorizzare nel sistema un Entità Responsabile Team</p>
 */
public class EntityResponsabileTeam {
	
	private int id;
	private String password;
	private String nome;
	private String cognome;
	
	private static Logger log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public EntityResponsabileTeam(int id, String password, String nome, String cognome) {
		this.setId(id);
		this.setPassword(password);
		this.setNome(nome);
		this.setCognome(cognome);
	}
	
	public EntityResponsabileTeam(int id) {
		this(id,"","","");
	}
	
	public EntityResponsabileTeam() {
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
	
	public EntityResponsabileTeam copyFrom(EntityResponsabileTeam responsabileTeam) {
		this.setId(responsabileTeam.getId());
		this.setPassword(responsabileTeam.getPassword());
		this.setNome(responsabileTeam.getNome());
		this.setCognome(responsabileTeam.getCognome());
		
		return this;
	}
	
	
	/**
	 * <p>Salva l'istanza chiamante nel database dopo aver verificato che l'ID non sia negativo o 0 (valori non accettati per quell'attributo).</p>
	 * Effettua una verifica dell'esistenza di questo Responsabile Team nel db mediante la funzione {@link Database.ResponsabileTeamDAO.selectByID}. <br>
	 * Se la verifica ha esito positivo effettua un nuovo inserimento mediante {@link Database.ResponsabileTeamDAO.insertIntoDB}. <br>
	 * Altrimenti effettua un update mediante {@link Database.ResponsabileTeamDAO.insertIntoDB}
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 *-1 se il database ha riscontrato un errore(salvataggio non effettuato);<br> 
	 *-2 se l'id sell'istanza da salvare non è valido(salvataggio non effettuato);<br>
	 *-3 sel la password non è valida(salvataggio non effettuato); 
	 */
	public int salvaInDB(){
		int res=0;
		
		ResponsabileTeamDAO respTeamDAO= new ResponsabileTeamDAO();
		HashMap<String,Object> mapRespTeam = convertIntoHashMapForDB(this);
		
		log.info("Tentativo di salvataggio sul DB del Responsabile Team id=" + Integer.toString(this.getId()) + " in corso" );
		
		if(this.getId()==0) {
			res=-2;
			log.warning("Il responabile team che si vuole salvare ha id non valido");
		}else if(this.getPassword().compareTo("")==0) {
			res=-3;
			log.warning("Il responasbile team che si vuole salvare non ha una password");
		}else if(respTeamDAO.selectById(this.getId())==null) {
			res=respTeamDAO.insertIntoDB(mapRespTeam);
		}else {
			res=respTeamDAO.updateIntoDB(mapRespTeam);
		}
		return res;
				
	}
	
	/**
	 *  <p>Effettua la rimozione del responsabile team con l'id dell'istanza chiamante dal database mediante {@link Database.ResponsabileTeamDAO.deleteByID}</p>
	 *  
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public int rimuoviDaDB() {
		ResponsabileTeamDAO respTeamDAO= new ResponsabileTeamDAO();
		
		log.info("Tentativo di rimozione dal DB del responsabile team id=" + Integer.toString(this.getId()) + " in corso" );
		
		return respTeamDAO.deleteByID(this.getId());
	}
	
	/**
	 *<p>Effettua la rimozione di tutti i responsabili team dal DB mediante {@link Database.ResponabileManagerDAO.deleteAll}</p>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br>
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public static int rimuoviTuttiDaDB() {
		ResponsabileTeamDAO respTeamDAO = new ResponsabileTeamDAO();
		
		log.info("Tentativo di rimozione dal DB di tutti i dipendenti in corso" );
		
		return respTeamDAO.deleteAll();
	}
	
	/**
	 * <p>Preleva il responsabile team richiesto dal database se presente {@link Database.ResponsabileTeamDAO.selectById}</p>
	 * <p>L'istanza deve avere già un id valido per poter avere successo l'interrogazione la databaase</p>
	 * 
	 * @return Un riferimento all'istanza chiamante EntityResponsabileTeam in caso di successo<br>
	 * Un riferimento a null in caso di fallimento
	 */
	public EntityResponsabileTeam prelevaRespTeamDaDB() {
			
		EntityResponsabileTeam responsabileTeam=new EntityResponsabileTeam();
		ResponsabileTeamDAO respTeamDAO=new ResponsabileTeamDAO();
		
		log.info("Tentativo di prelievo dal DB del responsabile team id=" + this.getId());
		
		HashMap<String,Object> mapRespTeam=respTeamDAO.selectById(id);
		
		
		if(mapRespTeam==null) {
			responsabileTeam = null;
			log.warning("Tentativo fallito,utente non trovato o errore nel database");
		}else{
			responsabileTeam=convertMapRespTeamIntoEntityRespTeam(mapRespTeam,responsabileTeam);
			if(responsabileTeam!=null) {
				this.copyFrom(responsabileTeam);
				responsabileTeam=this;
			}else {
				log.warning("Tentativo fallito,errore nella conversione dei dati passati dal database");
			}
		}
		
		return responsabileTeam;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i responsabili team presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di istanze EntityResponsabileTeam con gli attributi prelevati dal db;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityResponsabileTeam> prelevaTuttiRespTeam(){
		
		log.info("Tentativo di prelievo di tutti i responsabili team in corso");
		
		ResponsabileTeamDAO respTeamDAO= new ResponsabileTeamDAO();
		ArrayList<HashMap<String,Object>> listaMapRespTeam = respTeamDAO.selectAll();
		ArrayList<EntityResponsabileTeam> listaRespTeam = convertMapListIntoEntityRespTeam(listaMapRespTeam);
		
		return listaRespTeam;
	}
	
	/**
	 * <p>Verifica la presenza del responsabile team associato all'id nel database e successivamente verifica la corrisdpondenza tra la password in input e quella memorizzata</p>
	 * 
	 * @param id del responsabile team da verificare
	 * @param password di cui si vuole verificare se associata all'id
	 * @return 0 se il responsabile team è autenticato<br>
	 * -1 se la password è errata<br>
	 * -2 se il responsabile team non è stato trovato o c'è stato un errore nel db
	 */
	public int autenticazione(int id,String password) {
		
		log.info("Tentativo di autenticazione in corso");
		
		int ret=0;
		this.setId(id);
		
		EntityResponsabileTeam respTeam= prelevaRespTeamDaDB();
		if(respTeam==null) {
			ret=-2;
		}else if(this.getPassword().compareTo(password)!=0) {
			ret=-1;
			log.warning("Tentativo di autenticazione fallito poichè la password non coincide con quella associata all'id="+id);
		}
		
		return ret;	
		
	}
	
	/**
	 * <p>Preleva i dati da una HashMap con i dati di un responsabile team(tipo restituito da alcuni metodi delle classi del package Database) e li copia nell'istanza EntityTeam data in input</p>
	 * 
	 * @param mapRespTeam HashMap con i dati del responsabile team
	 * @param respTeam EntityResponsabileTeam in cui copiare i dati prelevati dalla mapRespTeam
	 * @return Un riferimento al respTeam dato in input
	 */
	private static EntityResponsabileTeam convertMapRespTeamIntoEntityRespTeam(HashMap<String,Object> mapRespTeam,EntityResponsabileTeam respTeam){
			
			if(mapRespTeam!=null&&!mapRespTeam.isEmpty()) {
				respTeam.setId((Integer) mapRespTeam.get("id"));
				respTeam.setPassword((String) mapRespTeam.get("password"));
				respTeam.setNome((String) mapRespTeam.get("nome"));
				respTeam.setCognome((String) mapRespTeam.get("cognome"));
			}
				
			return respTeam;
		};
		
	/**
	 * <p>Preleva i dati da un ArrayList di HashMap con i dati di un responsabile team(tipo restituito da alcuni metodi delle classi del package Database) e crea un ArrayList di istanze di EntityTeam</p>
	 * 
	 * @param listaMapRespTeam ArrayList di HashMap con i dati dei manager
	 * @return In caso di successo un riferimento all'ArrayList di manager creata;<br>
	 * In caso di fallimento un riferimento a null;
	 */
	private static ArrayList<EntityResponsabileTeam> convertMapListIntoEntityRespTeam(ArrayList<HashMap<String,Object>> listaMapRespTeam){
		ArrayList<EntityResponsabileTeam> listaRespTeam =null;
		
		if(listaMapRespTeam!=null&&!listaMapRespTeam.isEmpty()) {
			listaRespTeam = new ArrayList<EntityResponsabileTeam>();
			
			for(HashMap<String,Object> mapRespTeam : listaMapRespTeam) {
				EntityResponsabileTeam respTeam= new EntityResponsabileTeam();
				respTeam=convertMapRespTeamIntoEntityRespTeam(mapRespTeam,respTeam);
				listaRespTeam.add(respTeam);
			}
		}
		return listaRespTeam;
	};
	
		
	/**
	 * <p>Trasforma gli attributi di un istanza EntityResponsabileTeam in una HashMap (tipo da immettere all'invocazione di alcuni metodi del package Database)</p>
	 * 
	 * @param manager EntityResponsabileTeam da cui prelevare i dati
	 * @return Riferimento alla HashMap creata
	 */
	private static HashMap<String,Object> convertIntoHashMapForDB (EntityResponsabileTeam respTeam){
		
		HashMap<String,Object> mapRespTeam=new HashMap<String,Object>();
		
		mapRespTeam.put("id", respTeam.getId());
		mapRespTeam.put("password", respTeam.getPassword());
		mapRespTeam.put("nome", respTeam.getNome());
		mapRespTeam.put("cognome", respTeam.getCognome());
		
		return mapRespTeam;
		
	}

}

