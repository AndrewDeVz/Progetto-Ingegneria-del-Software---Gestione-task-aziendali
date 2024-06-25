package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TeamDAO {
	
	private Logger log;
	
	public TeamDAO() {
		LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	private HashMap<String,Object> convertintoHashMap(ResultSet rs) throws SQLException{
		HashMap<String,Object> mapTeam = new HashMap<String,Object>();
		
		mapTeam.put("nominativo", rs.getString("nominativo"));
    	
    	return mapTeam;
	}	
    // INSERIMENTO
    public int insertIntoDB(HashMap<String,Object> mapTeam) {
        int res = 0;
        // QUERY INSERIMENTO DATI
        String query = "INSERT INTO team (nominativo) VALUES ('" +
                (String) mapTeam.get("nominativo") + "');";

        log.info("Tentativo di eseguire la query: " + query);
        // INVIO LA QUERY DI UPDATE
        try {
        	DBManager.updateQuery(query);
            res = 0;
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
            res = -1;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        
        return res;
    }
    
    public HashMap<String,Object> selectByNominativo(String nominativo) {
    	HashMap<String,Object> mapTeam = null;
        // QUERY SELETTIVA IN BASE AL NOMINATIVO DEL TEAM
        String query = "SELECT * FROM team WHERE nominativo = '" + nominativo + "'";
        
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            if (rs.next()) {
            	mapTeam=convertintoHashMap(rs);
            }
            rs.close();
            
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	mapTeam=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return mapTeam;
    }

    // SELEZIONE DI TUTTI I TEAM
    public ArrayList<HashMap<String,Object>> selectAll() {
    	ArrayList<HashMap<String,Object>> listaNominativi = null;
        // QUERY SELETTIVA IN BASE AL NOMINATIVO DEL TEAM
        String query = "SELECT * FROM team";
        
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listaNominativi = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                listaNominativi.add(convertintoHashMap(rs));
            }
            rs.close();
            
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listaNominativi=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return listaNominativi;
    }    
    
 // Metodo per rimuovere un TeamDAO in base al nominativo
    public int deleteByNominativo(String nominativo) {
        int res = 0;
        String query = "DELETE FROM team WHERE nominativo = '" + nominativo + "';";
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

    // Metodo per rimuovere tutti i TeamDAO dal database
    public int deleteAll() {
        int res = 0;
        String query = "DELETE FROM team;";
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

