# MARKOV
La mia tesi :)

Progettino per catene di Markov basato sulle seguenti casistiche:
  1)  Generazione di un testo basato su un testo di input.
       a) creazione del dizionario basato sul testo di input;
       b) assegnazione delle probabilità su ogni parola del dizionario;
       c) scelta di ogni parola generata basata su quelel probabilità.
       OSSERVAZIONE: Dimensione del testo di output scelta dall'utente, basato su una KEY che decide la dimensione delle catene di parole usate per generare il testo         di output.
  3)  Simulazione degli stati di una catena di Markov con l'andamento del covid su una popolazione fittizia.
       a) vengono stabilite le propabilità di transizione di stato -> sano, infetto, guarito e deceduto;
       b) vengono decisi, dall'utente, dimensione della popolazione e durata del periodo di simulazione.
       c) Si procede alla stampa giorno per giorno;
       OSSERVAZIONE: Il calcolo è prettamente matematico, studiato in modo che le somme dei numeri della popolazione, per ogni stato, sia sempre uguale
       al numero della popolazione totale.
  3)  Simulazione degli stati di una catena di Markov su una simulazione di richieste da parte di client ad un server.
       a) Vengono generati numero di STEP e NUMERO MAX di clienti;
       b) Per ogni STEP, il numero di clienti cambia, seguendo la logica di transizione degli stati delle catene di Markov;
       d) Per ogni STEP, viene generata e stampata una matrice di transizione degli stati, che stabilisce come debba avvenire la transizione( si sceglie la
          probabilità piu alta della riga corripsondente al current state. Questa probabilità sancisce la colonna, che contiene l'indice che rispecchierà il nuovo
          numero di clienti;
       e) Si procede alla stampa di relative get, post, put, delete e risposte del server e si genera un tempo di servizio e di risposta.( per ogni cliente );
       f) Si procede all'analisi di tutti i dati, alla generazione di un qr, alla compressione in un file zip e al caricamento dei dati su google drive.

      Spero di essere stato chiaro, un kiss. a tutti coloro che ci guarderanno.
In eseguibili, se li avviate da windows, parte il programma :3 
