package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ResponsabileTeamDAO {
	
	private Logger log;
	
	public ResponsabileTeamDAO() {
		LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	private HashMap<String,Object> convertintoHashMap(ResultSet rs) throws SQLException{
		HashMap<String,Object> mapResponsabileTeam = new HashMap<String,Object>();
		mapResponsabileTeam.put("id",rs.getInt("id"));
		mapResponsabileTeam.put("password", rs.getString("password")) ;
		mapResponsabileTeam.put("nome", rs.getString("nome"));
		mapResponsabileTeam.put("cognome", rs.getString("cognome"));
    	
    	return mapResponsabileTeam;
	}
	
	public int insertIntoDB(HashMap<String,Object> mapRespTeam) {
        int res = 0;
        // QUERY INSERIMENTO DATI
        String query = "INSERT INTO responsabileteam(id, password, nome, cognome) VALUES (" 
                        + (Integer) mapRespTeam.get("id") + ", '" 
                        + (String) mapRespTeam.get("password") + "', '" 
                        + (String) mapRespTeam.get("nome") + "', '" 
                        + (String) mapRespTeam.get("cognome") + "');";
        // STAMPO LA QUERY
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
    	HashMap<String,Object> mapRespTeam = null;
        // QUERY SELETTIVA IN BASE ALL'ID
        String query = "SELECT * FROM responsabileteam WHERE ID = " + id + ";";
        // SPOSTO IL CURSORE TRA I RECORD ALLA RICERCA DELL'ID CHE MATCHA CON QUELLO DESIDERATO
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            // SPOSTO IL CURSORE AL PROSSIMO RECORD NEL RESULT SET
            // SE PUO' ESSERE SPOSTATO --> TRUE, ALTRIMENTI --> FALSE
            if (rs.next()) {
            	mapRespTeam=convertintoHashMap(rs);
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	mapRespTeam=null;
            log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return mapRespTeam;
    }
    
	public ArrayList<HashMap<String,Object>> selectAll() {
        //ARRAY LIST PER I MIEI RISULTATI
		ArrayList<HashMap<String,Object>> listRespTeam = null;
        //QUERY SELECT ALL
        String query = "SELECT * FROM responsabileteam;";
        //SPOSTO IL CURSORE SU TUTTI I RECORD E LI AGGIUNGO ALL'ARRAY LIST
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listRespTeam=new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                listRespTeam.add(convertintoHashMap(rs));
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listRespTeam=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        //RESTITUISCO LA LISTA DI MANAGERS
        return listRespTeam;
    }
	
	 public int updateIntoDB(HashMap<String,Object> mapRespTeam) {
	        int res = 0;
	        // QUERY INSERIMENTO DATI
	        String query = "UPDATE responsabileteam SET " + 
	                        "password='" + (String) mapRespTeam.get("password") + "', " +  
	                        "nome='"+ (String) mapRespTeam.get("nome") + "', " + 
	                        "cognome='"+ (String) mapRespTeam.get("cognome") + "' " + 
	                        "WHERE id="+(Integer) mapRespTeam.get("id") + ";";
	        // STAMPO LA QUERY
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
	 
    
    //Metodo per rimuovere un ManagerDAO in base all'ID
    public int deleteByID(int id) {
        int res = 0;
        String query = "DELETE FROM responsabileteam WHERE ID = " + id + ";";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            DBManager.updateQuery(query);
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
        String query = "DELETE FROM responsabileteam;";
        log.info("Tentativo di eseguire la query: " + query);
        System.out.println(query);
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


