# Progetto-Ingegneria-del-Software-Gestione-task-aziendali
This is the coding part of the Software Engineering Exam Project (10CFU).

"Customer" request:

Si intende sviluppare un sistema software per la gestione dei task lavorativi all'interno di un'azienda.  
Questo sistema mira a facilitare la pianificazione delle attività, il monitoraggio del progresso dei compiti assegnati e la collaborazione tra i dipendenti. 
Ogni dipendente è caratterizzato da nome, cognome, team di appartenenza e livello (director, principal, senior, junior). Ogni team è identificato da un proprio nominativo (es. Vendite, Reclami, etc..). 
Il sistema permette al responsabile dei team di creare un nuovo team e di assegnare ad esso i dipendenti, specificando tutte le rispettive informazioni.  
Il sistema include la possibilità per un dipendente di creare e assegnare task ad altri dipendenti specificando scadenze, priorità, nome e descrizione. Un dipendente può assegnare un task ad un altro dipendente solo se questi appartiene al suo team e possiede un livello superiore.  
Se uno di questi requisiti non è rispettato, allora il sistema deve mostrare a video un opportuno messaggio di errore. Inoltre, un dipendente non può avere più di tre task assegnati in contemporanea. 
I dipendenti potranno aggiornare lo stato di terminazione dei loro task attraverso una opportuna interfaccia grafica che mostra i task ad essi assegnati. 
Per i manager, sarà invece necessario realizzare una dashboard che fornisca una visione generale: dello stato di tutti i task, task risolti per dipendente, task risolti per team. Questa interfaccia consentirà ai manager di monitorare i progressi, identificare i colli di bottiglia e redistribuire le risorse se necessario, per garantire il rispetto delle scadenze.
