package testing;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Control.ControllerGestioneAzienda;
import Database.TeamDAO;

public class CreaTeamTest {

    private ControllerGestioneAzienda controller;

    @Before
    public void setUp() {
      controller = ControllerGestioneAzienda.getIstance();
        TeamDAO myTeam = new TeamDAO();
        //PULIZIA DATABASE
        
        myTeam.deleteAll();
    }

    @After
    public void tearDown() {
      TeamDAO myTeam = new TeamDAO();
      myTeam.deleteAll();
    }

    @Test
    //TEST CASE 1 PIANO FUNZIONALE CREA TEAM
    public void testCreaTeamSuccess() {
        int expected=0;
        
    	String nomeTeam = "Sviluppo Frontend";
        int result = controller.creaTeam(nomeTeam);
        
        assertEquals(expected,result);
        
    }

    @Test
    //TEST CASE 2 PIANO FUNZIONALE CREA TEAM
    public void testCreaTeamDuplicate() {
    	int expectedfirst=0;
    	int expectedsecond=-4;
        String nomeTeam = "Sviluppo Frontend2";
        
        int firstResult = controller.creaTeam(nomeTeam);
        assertEquals(expectedfirst,firstResult);

        int secondResult = controller.creaTeam(nomeTeam);
        assertEquals(expectedsecond,secondResult);
    }

    
    @Test
    //TEST CASE 3 PIANO FUNZIONALE CREA TEAM
    public void testCreaTeamEmptyName() {
    	int expected=-2;
        String nomeTeam = "";
        
        int result = controller.creaTeam(nomeTeam);
        assertEquals(expected,result);
    }
    
    
    @Test
    //TEST CASE 4 PIANO FUNZIONALE CREA TEAM
    public void testCreaTeamTroppiCaratteri() {
    	int expected=-3;
        String nomeTeam = "Ingegneria del software Ingegneria del software IS2";
        
        int result = controller.creaTeam(nomeTeam);
        assertEquals(expected,result);
    }
    
}
	