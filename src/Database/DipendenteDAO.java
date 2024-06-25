package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import communicationEnum.enumLivello;

/**
 * <p> Permette la comunicazione tra l'entità Dipendente e il Database</p>
 */
public class DipendenteDAO {
	
	private Logger log;
	
	public DipendenteDAO() {
		LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	/**
	 * <p>Permette la creazione di un oggetto per il trasferimento di dati tra il DTO e il corrispondnete Entity del tipo Dipendente</p>
	 * 
	 * @param rs Oggeto risultato di una query
	 * @return HashMap<String,Object> in cui si associano i vari elemetno di Dipendente
	 * @throws SQLException
	 */
	private HashMap<String,Object> convertintoHashMap(ResultSet rs) throws SQLException{
		HashMap<String,Object> mapDipendente = new HashMap<String,Object>();
		
		mapDipendente.put("id",rs.getInt("id"));
    	mapDipendente.put("password",rs.getString("password"));
    	mapDipendente.put("nome",rs.getString("nome"));
    	mapDipendente.put("cognome",rs.getString("cognome"));
    	mapDipendente.put("livello",enumLivello.valueOf(rs.getString("livello")));
    	String team=rs.getString("team");
    	if(team==null) {
    		mapDipendente.put("team","");
    	}else {
    		mapDipendente.put("team",team);
    	}
    	
    	return mapDipendente;
	}
	
    //INSERIMENTO le abbiamo tolte perché ci sono passate da un sistema esterno, quindi l'unica responsabilità che possiamo averci è quella di lettura
    public int insertIntoDB(HashMap<String,Object> mapDipendente) {
        int res = 0;
        // QUERY INSERIMENTO DATI
        String query = "INSERT INTO dipendente (id, password, nome, cognome, team, livello) VALUES (" +
                ((Integer) mapDipendente.get("id")).toString() + ", '" +
                (String) mapDipendente.get("password") + "', '" +
                (String) mapDipendente.get("nome") + "', '" +
                (String) mapDipendente.get("cognome") + "', '" +
                (String) mapDipendente.get("team") + "', '" +
                ((enumLivello) mapDipendente.get("livello")).toString() + "');";
        // INVIO LA QUERY DI UPDATE
        log.info("Tentativo di eseguire la query: " + query);
        try {
            DBManager.updateQuery(query);
            res=0;
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
            res = -1;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return res;
    }
    
    public HashMap<String,Object> selectById(int id) {
        HashMap<String,Object> mapDipendente = null ;
        // QUERY SELETTIVA IN BASE ALL'ID
        String query = "SELECT * FROM dipendente WHERE id = " + id + ";";
        // SPOSTO IL CURSORE TRA I RECORD ALLA RICERCA DELL'ID CHE MATCHA CON QUELLO DESIDERATO
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            // SPOSTO IL CURSORE AL PROSSIMO RECORD NEL RESULT SET
            // SE PUO' ESSERE SPOSTATO --> TRUE, ALTRIMENTI --> FALSE
            if (rs.next()) {
            	//PRENDO I DATI DEL DIPENDENTE CHE HA VERIFICATO LA CONDIZIONE
            	mapDipendente=convertintoHashMap(rs);
            }
            rs.close();
            log.info("Query eseguita con successo");
            
        } catch (ClassNotFoundException | SQLException e) {
        	mapDipendente=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return mapDipendente;
    }

    //SELEZIONE PER TEAM(Servirebbe per implementare Mostra Task risolti per team e riassegna risorse)
    public ArrayList<HashMap<String,Object>> selectByTeam(String team) {
    	ArrayList<HashMap<String,Object>> arrayDipendenti = null;
        // QUERY SELETTIVA IN BASE AL TEAM
        String query = "SELECT * FROM dipendente WHERE team = '" + team + "';";
        
        log.info("Tentativo di eseguire la query: " + query);
        try {
        	ResultSet rs = DBManager.selectQuery(query);
            // SPOSTO IL CURSORE AL PROSSIMO RECORD NEL RESULT SET
            // SE PUO' ESSERE SPOSTATO --> TRUE, ALTRIMENTI --> FALSE
        	arrayDipendenti = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {        
            	arrayDipendenti.add(convertintoHashMap(rs));
            }
            rs.close();
            
            log.info("Query eseguita con successo");
              
        } catch (ClassNotFoundException | SQLException e) {
        	arrayDipendenti=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return arrayDipendenti;
    }

    //SELECT ALL
    public ArrayList<HashMap<String,Object>> selectAll() {
    	ArrayList<HashMap<String,Object>> arrayDipendenti = null;
        // QUERY SELETTIVA IN BASE AL TEAM
        String query = "SELECT * FROM dipendente;";
        
        log.info("Tentativo di eseguire la query: " + query);
        try {
        	ResultSet rs = DBManager.selectQuery(query);
            // SPOSTO IL CURSORE AL PROSSIMO RECORD NEL RESULT SET
            // SE PUO' ESSERE SPOSTATO --> TRUE, ALTRIMENTI --> FALSE
        	arrayDipendenti = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
            	arrayDipendenti.add(convertintoHashMap(rs));
            }
            rs.close();
                        
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	arrayDipendenti=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return arrayDipendenti;
    }
    
    public int updateIntoDB(HashMap<String,Object> mapDipendente) {
    	int res = 0;
        // QUERY INSERIMENTO DATI
        String query = "UPDATE dipendente SET " +
                "password = '" + (String) mapDipendente.get("password") + "', "+
                "nome = '" + (String) mapDipendente.get("nome") + "', " +
                "cognome = '" + (String) mapDipendente.get("cognome") + "', " +
                "team = '" + (String) mapDipendente.get("team") + "', " +
                "livello = '" + ((enumLivello) mapDipendente.get("livello")).toString() + "' " +
                "where ID = " + ((Integer) mapDipendente.get("id")).toString() + ";";
        // INVIO LA QUERY DI UPDATE
        log.info("Tentativo di eseguire la query: " + query);
        
        try {
            DBManager.updateQuery(query);
            res=0;
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	res = -1;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return res;
    }
    
    //RIMOZIONE PER ID
   public int deleteByID(int id) {
        int res = 0;
        // QUERY PER RIMUOVERE UN DIPENDENTE IN BASE ALL'ID
        String query = "DELETE FROM dipendente WHERE id = " + id + ";";
        // INVIO LA QUERY DI DELETE
        log.info("Tentativo di eseguire la query: " + query);
        try {
            DBManager.updateQuery(query);
            res=0;
            log.info("Query eseguita con successo");       
        } catch (ClassNotFoundException | SQLException e) {
        	res = -1;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return res;
    }

    //RIMOZIONE TUTTI I DIPENDENTI
    public int deleteAll() {
        int res = 0;
        // QUERY PER RIMUOVERE TUTTI I DIPENDENTI
        String query = "DELETE * FROM dipendente;";
        // INVIO LA QUERY DI DELETE
        log.info("Tentativo di eseguire la query: " + query);
        try {
            DBManager.updateQuery(query);
            res=0;
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
            res = -1;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return res;
    }

}


