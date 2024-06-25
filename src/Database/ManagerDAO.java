package Database;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
//import java.util.ArrayList;SE CI SERVE LA LISTA DEI MANAGER

public class ManagerDAO {
	
	private Logger log;
	
	public ManagerDAO() {
		LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	private HashMap<String,Object> convertintoHashMap(ResultSet rs) throws SQLException{
		HashMap<String,Object> mapManager = new HashMap<String,Object>();
		mapManager.put("id",rs.getInt("id"));
        mapManager.put("password", rs.getString("password")) ;
        mapManager.put("nome", rs.getString("nome"));
        mapManager.put("cognome", rs.getString("cognome"));
    	
    	return mapManager;
	}
	
    // Metodo di inserimento nel database
    public int insertIntoDB(HashMap<String,Object> mapManager) {
        int res = 0;
        // QUERY INSERIMENTO DATI
        String query = "INSERT INTO manager(id, password, nome, cognome) VALUES (" 
                        + (Integer) mapManager.get("id") + ", '" 
                        + (String) mapManager.get("password") + "', '" 
                        + (String) mapManager.get("nome") + "', '" 
                        + (String) mapManager.get("cognome") + "');";
        
        log.info("Tentativo di eseguire la query: " + query);
        // INVIO LA QUERY DI UPDATE
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

    // Metodo di select in base all'ID
    public HashMap<String,Object> selectById(int id) {
        // VAR BOOL PER CAPIRE SE TROVO IL MANAGER
    	HashMap<String,Object> mapManager = null;
        // QUERY SELETTIVA IN BASE ALL'ID
        String query = "SELECT * FROM manager WHERE id = " + id + ";";
        // SPOSTO IL CURSORE TRA I RECORD ALLA RICERCA DELL'ID CHE MATCHA CON QUELLO DESIDERATO
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            // SPOSTO IL CURSORE AL PROSSIMO RECORD NEL RESULT SET
            // SE PUO' ESSERE SPOSTATO --> TRUE, ALTRIMENTI --> FALSE
            if (rs.next()) {
            	mapManager=convertintoHashMap(rs);
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
            mapManager=null;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return mapManager;
    }
    
	public ArrayList<HashMap<String,Object>> selectAll() {
        //ARRAY LIST PER I MIEI RISULTATI
		ArrayList<HashMap<String,Object>> listManagers = null;
        //QUERY SELECT ALL
        String query = "SELECT * FROM manager;";
        //SPOSTO IL CURSORE SU TUTTI I RECORD E LI AGGIUNGO ALL'ARRAY LIST
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listManagers=new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                listManagers.add(convertintoHashMap(rs));
            }
            rs.close();
            
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listManagers=null;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        //RESTITUISCO LA LISTA DI MANAGERS
        return listManagers;
    }
	
	 public int updateIntoDB(HashMap<String,Object> mapManager) {
	        int res = 0;
	        // QUERY INSERIMENTO DATI
	        String query = "UPDATE manager SET " + 
	                        "password='" + (String) mapManager.get("password") + "', " +  
	                        "nome='"+ (String) mapManager.get("nome") + "', " + 
	                        "cognome='"+ (String) mapManager.get("cognome") + "' " + 
	                        "WHERE id="+(Integer) mapManager.get("id") + ";";
	        // STAMPO LA QUERY
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
	 
    
    //Metodo per rimuovere un ManagerDAO in base all'ID
    public int deleteByID(int id) {
        int res = 0;
        String query = "DELETE FROM manager WHERE ID = " + id + ";";
        log.info("Tentativo di eseguire la query: " + query);
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

    //Metodo per rimuovere tutti i ManagerDAO dal database
    public int deleteAll() {
        int res = 0;
        String query = "DELETE FROM manager;";
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

