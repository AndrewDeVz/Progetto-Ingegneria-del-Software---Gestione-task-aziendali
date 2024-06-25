package testing;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import Control.ControllerGestioneAzienda;
import communicationEnum.enumPrioritaTask;
import Entity.EntityTask;
import Database.TaskDAO;

public class CreaTaskTest {
	
  private ControllerGestioneAzienda controller;

    @Before
    public void setUp() {
        controller = ControllerGestioneAzienda.getIstance();
        TaskDAO myTask = new TaskDAO();
        myTask.deleteAll();
    }
    
    @After
    public void tearDown() {
      TaskDAO myTask = new TaskDAO();
      myTask.deleteAll();
    }
    
    @Test
    //TEST CASE 1 PIANO FUNZIONALE CREA TASK
    public void testCreaTaskSuccess() {
    	int expected=0;
    	
        String nomeTask = "Crea Homepage";
        enumPrioritaTask prioritaTask = enumPrioritaTask.ALTA;
        LocalDate scadenzaTask = LocalDate.of(2024, 9, 17);
        String descrizioneTask = "muoviti";
        
        int result = controller.creaTask(nomeTask, prioritaTask, scadenzaTask, descrizioneTask);
        
        assertEquals(expected,result);        
    }
    
    @Test
    //TEST CASE 2 PIANO FUNZIONALE CREA TASK
    public void testCreaTaskSenzaPriorita() {
    	int expected=0;
    	
        String nomeTask = "Crea Homepage";
        enumPrioritaTask prioritaTask = null;
        LocalDate scadenzaTask = LocalDate.of(2024, 9, 17);
        String descrizioneTask = "muoviti";
        
        int result = controller.creaTask(nomeTask, prioritaTask, scadenzaTask, descrizioneTask);
               
        assertEquals(expected,result);
        
        int expectedStateCoparation=0;
        
        EntityTask task=new EntityTask(10001);
        task.prelevaTaskDaDB();
        
        int effectiveStateComparation = enumPrioritaTask.BASSA.compareTo(task.getPriorita());
        
        assertEquals(expectedStateCoparation,effectiveStateComparation);
    }
    
    
    @Test
    //TEST CASE 3 PIANO FUNZIONALE CREA TASK
    public void testCreaTaskSenzaNome() {
    	int expected=-3;
    	
        String nomeTask = "";
        enumPrioritaTask prioritaTask = enumPrioritaTask.BASSA;;
        LocalDate scadenzaTask = LocalDate.of(2024, 9, 17);
        String descrizioneTask = "muoviti";
        
        int result = controller.creaTask(nomeTask, prioritaTask, scadenzaTask, descrizioneTask);
       
        assertEquals(expected,result);        
    }
    
    
    @Test
    //TEST CASE 4 PIANO FUNZIONALE CREA TASK
    public void testCreaTaskNomeTroppoLungo() {
    	int expected=-4;
    	
        String nomeTask = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";  
        enumPrioritaTask prioritaTask = enumPrioritaTask.BASSA;;
        LocalDate scadenzaTask = LocalDate.of(2024, 9, 17);
        String descrizioneTask = "muoviti";
        
        int result = controller.creaTask(nomeTask, prioritaTask, scadenzaTask, descrizioneTask);
        
        assertEquals(expected,result);        
    }
    
    @Test
    //TEST CASE 5 PIANO FUNZIONALE CREA TASK
    public void testCreaTaskDescrizioneTroppoLunga() {
    	int expected=-5;
        
        String nomeTask = "Crea Homepage";
        enumPrioritaTask prioritaTask = enumPrioritaTask.BASSA;;
        LocalDate scadenzaTask = LocalDate.of(2024, 9, 17);
        String descrizioneTask = "ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao ciao cia";
        
        int result = controller.creaTask(nomeTask, prioritaTask, scadenzaTask, descrizioneTask);
        
        assertEquals(expected,result);
    }

}
