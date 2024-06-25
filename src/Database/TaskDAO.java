package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.time.LocalDate;

import communicationEnum.*;

public class TaskDAO {
	
	private Logger log;
	
	public TaskDAO() {
		LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}
	
	private HashMap<String,Object> convertintoHashMap(ResultSet rs) throws SQLException{
			HashMap<String,Object> mapTask= new HashMap<String,Object>();
			
			mapTask.put("codice", rs.getInt("codice"));
	        mapTask.put("nome", rs.getString("nome"));
	        mapTask.put("priorita", enumPrioritaTask.valueOf(rs.getString("priorita")));
	        mapTask.put("stato", enumStatiTask.valueOf(rs.getString("stato")));
	        mapTask.put("scadenza", LocalDate.parse(rs.getString("scadenza")));
	        mapTask.put("descrizione", rs.getString("descrizione"));
	        Integer dipendente_assegnato=rs.getInt("dipendente_assegnato");
	        if(dipendente_assegnato!=null){
	        	mapTask.put("dipendente_assegnato",dipendente_assegnato);
	        }else {
	        	mapTask.put("dipendente_assegnato", 0);
	        }
	        
	        return mapTask;
	}
  
    //Ritorna -1 in caso di errore del DB, -2 nel caso di task con codice già essitente
    public int insertIntoDB(HashMap<String,Object> mapTask) {
        int res = 0;
        //Verifica che il codice non sia già esistente     	
        String query = "INSERT INTO task (codice, nome, priorita, stato, scadenza, descrizione) VALUES (" + 
        		((Integer) mapTask.get("codice")).toString() + ", '" +
                (String) mapTask.get("nome") + "', '" +
                ((enumPrioritaTask) mapTask.get("priorita")).toString() + "', '" +
                ((enumStatiTask) mapTask.get("stato")).toString() + "', '" +
                ((LocalDate) mapTask.get("scadenza")).toString() + "', '" +
                (String) mapTask.get("descrizione") +  "');";

	        //LANCIO LA QUERY
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
    
    
    //Permette al manager di avere la lista dei codici in modo da poterlo assegnare incrementale
    public ArrayList<Integer> selectAllCodici(){
    	ArrayList<Integer> listaCodici = null;
    	
    	String query = "SELECT codice FROM task;";
    	
    	log.info("Tentativo di eseguire la query: " + query);
    	try {
    		ResultSet res = DBManager.selectQuery(query);
    		
    		listaCodici = new ArrayList<Integer>();
    		while(res.next()) {
    			listaCodici.add(res.getInt("codice"));
    		}     
    		log.info("Query eseguita con successo");
    	} catch (ClassNotFoundException | SQLException e) {
    		listaCodici=null;
    		log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
    	return listaCodici;
    }

    //SELEZIONE PER CODICE
    public HashMap<String,Object> selectByCodice(int codice) {
        HashMap<String,Object> mapTask = null;
        //QUERY SELETTIVA
        String query = "SELECT * FROM task WHERE codice = " + codice + ";";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            //SCORRO TRA I RECORD DELLA TABELLA
            //QUANDO TROVO IL TASK CON IL CODICE DESIDERATO MI PRENDO I DATI
            if (rs.next()) {
            	mapTask=convertintoHashMap(rs);
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	mapTask=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return mapTask;
    }

    //SELEZIONE PER STATO --> LISTA DI TASK CON QUELLO STATO
    public ArrayList<HashMap<String,Object>> selectByStato(enumStatiTask stato) {
        ArrayList<HashMap<String,Object>> listaTasks = null;
        String query = "SELECT * FROM task WHERE stato = '" + stato.toString() + "'";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listaTasks = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                listaTasks.add(convertintoHashMap(rs));
            }
            rs.close();
            
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listaTasks=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return listaTasks;
    }

    //SELEZIONE DEL TASK IN BASE AL DIPENDENTE
    public ArrayList<HashMap<String,Object>> selectByDipendenteAssegnato(int dipendenteAssegnato) {
    	ArrayList<HashMap<String,Object>> listaTasks = null;
        String query = "SELECT * FROM task WHERE dipendente_assegnato = " + dipendenteAssegnato + ";";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listaTasks = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                listaTasks.add(convertintoHashMap(rs));
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listaTasks=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return listaTasks;
    }

    //SELEZIONE DI TUTTI I TASK
    public ArrayList<HashMap<String,Object>> selectAll() {
    	ArrayList<HashMap<String,Object>> listaTasks = null;
        String query = "SELECT * FROM task";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            
            listaTasks = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
            	listaTasks.add(convertintoHashMap(rs));
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	listaTasks=null;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return listaTasks;
    }

    //CONTEGGIO TASK IN BASE ALLO STATO (monitoraProgressi analizza il numero di task per stato)
    public int countTasksByStato(enumStatiTask stato) {
        int count = -1;
        String query = "SELECT COUNT(*) AS task_count FROM task WHERE Stato = '" + stato.toString() + "'";
        log.info("Tentativo di eseguire la query: " + query);
        try {
            ResultSet rs = DBManager.selectQuery(query);
            if (rs.next()) {
                count = rs.getInt("task_count");
            }
            rs.close();
            log.info("Query eseguita con successo");
        } catch (ClassNotFoundException | SQLException e) {
        	count = -1;
        	log.log(Level.WARNING,"Eccezione all'esecuzione della query",e);
        }
        return count;
    }
    
    public int updateIntoDB(HashMap<String,Object> mapTask) {
        int res = 0;
        
        String query = "UPDATE task SET " +
        "nome = '" + (String) mapTask.get("nome") + "', "+
        "priorita = '" + ((enumPrioritaTask) mapTask.get("priorita")).toString() + "', " +
        "stato = '" + ((enumStatiTask) mapTask.get("stato")).toString() + "', " +
        "scadenza = '" + ((LocalDate) mapTask.get("scadenza")).toString() + "', " +
        "descrizione = '" + (String) mapTask.get("descrizione") + "', ";
        
        if((Integer) mapTask.get("dipendente_assegnato")!=0) {
        	query = query + " dipendente_assegnato = " + (Integer) mapTask.get("dipendente_assegnato");
        }else {
        	query = query + " dipendente_assegnato = NULL";
        }
        query = query + " WHERE codice = " + ((Integer) mapTask.get("codice")).toString()  + ";";
        
        log.info("Tentativo di eseguire la query: " + query);
        //LANCIO LA QUERY
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
    
    //Metodo per rimuovere un TaskDAO in base al codice
    public int deleteByCodice(int codice) {
        int res = 0;
        String query = "DELETE FROM task WHERE codice = " + codice + ";";
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

    //Metodo per rimuovere tutti i TaskDAO dal database
    public int deleteAll() {
        int res = 0;
        String query = "DELETE FROM task;";
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

