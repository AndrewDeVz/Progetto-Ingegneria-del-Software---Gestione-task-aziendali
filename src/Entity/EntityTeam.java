package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import Database.TeamDAO;

/**
 * <p>Classe deputata a memorizzare nel sistema un Entità Team</p>
 */
public class EntityTeam {
	
	private String nominativo;

	private static Logger log=LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

	public EntityTeam(String nominativo) {
		this.setNominativo(nominativo);
	}
	
	public EntityTeam() {
		this("");
	};

	
	public String getNominativo() {return nominativo;}
	public void setNominativo(String nominativo) {this.nominativo = nominativo;}
	
	public EntityTeam copyFrom(EntityTeam team) {
		this.setNominativo(team.getNominativo());
		
		return this;
	}
	
	/**
	 * <p>Salva l'istanza chiamante nel database dopo aver verificato che il nominativo non sia vuoto (valore non accettato per quell'attributo).</p>
	 * Verifica che non esista già un team con quel nome mediantr {@link Database.TeamDAO.selectByNominativo}(se non ritorna nulla allora non è presente)<br>
	 * Effettua un nuovo inserimento mediante {@link Database.TeamDAO.insertIntoDB}. <br>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 *-1 se il database ha riscontrato un errore(salvataggio non effettuato);<br> 
	 *-2 se il nominativo del team da salvare è vuoto;<br>
	 *-3 se il nominativo del team da salvare supera i 50 caratteri;<br>
	 *-4 se esiste già un team con quel nome;
	 */
	
	public int salvaInDB() {
		
		int resu=0;
		
		TeamDAO teamDAO=new TeamDAO();
		HashMap<String,Object> mapTeam=convertIntoHashMapForDB(this);
		
		log.info("Tentativo di salvataggio sul DB del Responsabile Team id=" + this.getNominativo() + " in corso" );
		
		if(this.getNominativo().compareTo("")==0) {
			resu= -2; //Se il nome del team da salvare è vuoto
			log.warning("Il nome del team da salvare è vuoto");
		}else if(this.getNominativo().length()>50) {
			resu= -3;//Se il nome del team da salvare supera i 50 caratteri
			log.warning("Il nome del team da salvare supera i 50 caratteri");
		}else if(teamDAO.selectByNominativo(this.getNominativo())==null) {
			resu=teamDAO.insertIntoDB(mapTeam);
		}else { 
			//Se si dovessero aggiungere altri attributi al Team allora in questo blocco ci andrà la funzione update, finoa quando non succederà non ha senso aggiornare la chaive primaria 
			resu= -4; //se già esiste un team con quel nome
			log.warning("Esiste già un nome con quel team");
		}
		
		return resu;	
	}
	
	/**
	 *  <p>Effettua la rimozione del team team con il nmominativo dell'istanza chiamante dal database mediantre {@link Database.TeamDAO.deleteByNominativo}</p>
	 *  
	 * @return 0 se l'operazione ha avuto successo;<br> 
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public int rimuoviDaDB() {
		TeamDAO teamDAO=new TeamDAO();
		return teamDAO.deleteByNominativo("nominativo");
	}
	
	/**
	 *<p>Effettua la rimozione di tutti i team dal DB mediante {@link Database.TeamDAO.deleteAll}</p>
	 * 
	 * @return 0 se l'operazione ha avuto successo;<br>
	 * -1 se il database ha riscontrato un errore(salvataggio non effettuato);
	 */
	public static int rimuoviTuttiDaDB() {
		TeamDAO teamDAO=new TeamDAO();
		return teamDAO.deleteAll();
	}
	
	/**
	 * <p>Preleva il team richiesto dal database se presente {@link Database.TeamDAO.selectByNominativo}</p>
	 * <p>L'istanza deve avere già un nominativo valido per poter avere successo l'interrogazione la database</p>
	 * 
	 * @return Un riferimetno all'istanza chiamante EntityTeam in caso di successo<br>
	 * Un riferimento a null in caso di fallimento
	 */
	public EntityTeam prelevaTeamDaDB() {
		
		EntityTeam team=new EntityTeam();
		TeamDAO teamDAO=new TeamDAO();
		
		log.info("Tentativo di prelievio dal DB del team nominativo=" + this.getNominativo());
		
		HashMap<String,Object> mapTeam=teamDAO.selectByNominativo(this.getNominativo());
		
		
		if(mapTeam==null) {
			team = null;
		}else{
			team=convertMapTeamIntoEntityTeam(mapTeam,team);
			if(team!=null) {
				this.copyFrom(team);
				team=this;
			}else {
				log.warning("Tentativo fallito,errore nel nella conversione dei dati passati dal database");
			}
		}
		
		return team;
	}
	
	/**
	 * <p>Preleva dal DB una lista di tutti i team presenti</p>
	 * 
	 * @return In caso di successo un riferimento ad una ArrayList di istanze EntityTeam con gli attributi prelevati dal db;<br>
	 * In caso di fallimento un puntatore a null;
	 */
	public static ArrayList<EntityTeam> prelevaTuttiTeam(){
		TeamDAO teamDAO=new TeamDAO();
		ArrayList<HashMap<String,Object>>listMapTeam=teamDAO.selectAll();
		
		return convertMapListIntoEntityTeam(listMapTeam);
	}
	
	/**
	 * <p>Preleva i dati da una HashMap con i dati di un team(tipo restituito da alcuni metodi delle classi del package Database) e li copia nell'istanza EntityTeam data in input</p>
	 * 
	 * @param mapTeam HashMap con i dati del team
	 * @param team EntityTeam in cui copiare i dati prelevati dalla mapTeam
	 * @return Un riferimento al team dato in input
	 */	
	private static EntityTeam convertMapTeamIntoEntityTeam(HashMap<String,Object> mapTeam,EntityTeam team){
		
		if(mapTeam!=null&&!mapTeam.isEmpty()) {
			team.setNominativo((String) mapTeam.get("nominativo"));
		}
			
		return team;
	}
	
	/**
	 * <p>Preleva i dati da un ArrayList di HashMap con i dati di un team(tipo restituito da alcuni metodi delle classi del package Database) e crea un ArrayList di istanze di EntityTeam</p>
	 * 
	 * @param listaMapRespTeam ArrayList di HashMap con i dati dei manager
	 * @return In caso di successo un riferimento all'ArrayList di manager creata;<br>
	 * In caso di fallimento un riferimento a null;
	 */
	private static ArrayList<EntityTeam> convertMapListIntoEntityTeam(ArrayList<HashMap<String,Object>> listaMapTeam){
		ArrayList<EntityTeam> listaTeam =null;
		
		if(listaMapTeam!=null&&!listaMapTeam.isEmpty()) {
			listaTeam = new ArrayList<EntityTeam>();
			
			for(HashMap<String,Object> mapTeam : listaMapTeam) {
				EntityTeam team= new EntityTeam();
				team=convertMapTeamIntoEntityTeam(mapTeam,team);
				listaTeam.add(team);
			}
		}
		return listaTeam;
	}
	
	/**
	 * <p>Trasforma gli attributi di un istanza EntityTeam in una HashMap (tipo da immettere all'invocazione di alcuni metodi del package Database)</p>
	 * 
	 * @param team EntityResponsabileTeam da cui prelevare i dati
	 * @return Riferimento alla HashMap creata
	 */
	private static HashMap<String,Object> convertIntoHashMapForDB (EntityTeam team){
		
		HashMap<String,Object> mapTeam=new HashMap<String,Object>();
		
		mapTeam.put("nominativo", team.getNominativo());
		
		return mapTeam;
		
	}

	
}
