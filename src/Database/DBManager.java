package Database;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.LogManager;

/**
 * <p> Classe che permette la comunicazione con il databse, permettendo di aprire e chiudere una connessione con esso</p>
 */
public class DBManager {
	
	//CLASSE GESTIONE CONNESSIONE CON IL DB
    public static String url = "jdbc:mysql://localhost:3306/";
    public static String dbName = "azienda";
    public static String driver = "com.mysql.cj.jdbc.Driver";
    public static String userName = "root";
    public static String password = "admin";
    
    private static Logger log;

    
    /**
     * <p>Permette di creare una connessione con il database all'indirizzo specificato negli attributi</p>
     * 
     * @return Ritorna un riferimento al tipo Connection, in caso di successo si è correttamente connessi al database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
    	
    	LogManager logManager= LogManager.getLogManager();
		log=logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
    	
        Connection connessione = null;
        Class.forName(driver); //INIZIALIZZAZIONE DRIVER JDBC
        connessione = DriverManager.getConnection(url+dbName,userName,password);
        log.info("Connessione al database "+ url + dbName + " avvenuta con successo");
        return connessione;
    }

    /**
     * <p>Permette di chiudere la connessione presa in input</p>
     * 
     * @param c è il tipo Connection che si vuole chiudere
     * @throws SQLException
     */
    public static void closeConnection(Connection c) throws SQLException {
        c.close();
        log.info("Connessione al database "+ url + dbName + " chiusa");
    }

    /**
     * <p>Permettere di eseguire una query di tipo SELECT</p>
     * 
     * @param query, in formato stringa, che si intende eseguire
     * @return Ritorna un oggetto contnente i risultati della query
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ResultSet selectQuery(String query) throws ClassNotFoundException, SQLException {
        Connection connessione = getConnection(); 
        Statement statement = connessione.createStatement();
        ResultSet result = statement.executeQuery(query); //SINGOLO RESULT SET
        return result;
    }

    /**
     * <p>Pemrette di eseguire le query del tipo UPDATE,INSERT,DELETE</p>
     * 
     * @param query, in formato stringa, che si intende eseguire
     * @return ritorna ciò che è ritornato da {@link Connection.executeUpdate()}
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static int updateQuery(String query) throws ClassNotFoundException, SQLException {
        Connection connessione = getConnection();
        Statement statement = connessione.createStatement();
        int ret = statement.executeUpdate(query);
        connessione.close();
        return ret;
    }

    
}

