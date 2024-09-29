package covid;

import java.util.Scanner;

import static More.HelperMethod.attendiInput;

public class MarkovOnDataCovid {
    /* Variabile per la popolazione sana iniziale */
    private static int sani;

    /* Variabile per i giorni totali di simulazione, inseriti dall'utente */
    private static int giorniTotaliDaInput;

    public static void main(String[] args) throws InterruptedException {
        // Probabilità di transizione tra i diversi stati
        double probTransizioneInfetto = 0.2; // 20% di probabilità di diventare infetto
        double probTransizioneGuarito = 0.8; // 80% di probabilità di guarire
        double probTransizioneDeceduto = 0.03; // 3% di probabilità di decedere
        double probTransizioneReInfezione = 0.15; // 15% di probabilità di reinfezione

        Thread.sleep(2000);
        System.out.println("Premere invio per iniziare la Simulazione!");
        attendiInput();
        System.out.println();

        // Stampa delle informazioni introduttive
        stampePresentazione(probTransizioneInfetto, probTransizioneDeceduto, probTransizioneGuarito, probTransizioneReInfezione);

        System.out.println("Premi invio per continuare...");
        attendiInput();

        // Chiede all'utente la dimensione della popolazione iniziale di sani
        askSani();
        Thread.sleep(3000);

        // Chiede all'utente quanti giorni durerà la simulazione
        askGiorniTot();
        Thread.sleep(3000);

        // Stampa le informazioni sull'avvio della simulazione
        stampeEsecuzione();
        Thread.sleep(2000);

        // Avvia la simulazione di Markov
        simulazioneMarkov(sani, giorniTotaliDaInput, probTransizioneInfetto, probTransizioneGuarito, probTransizioneDeceduto, probTransizioneReInfezione);

        System.out.println("La simulazione è terminata, procedi con premere invio per terminare");
        attendiInput();
    }

    // Funzione principale che esegue la simulazione della catena di Markov
    private static void simulazioneMarkov(int sani, int giorniTotali, double probTransizioneInfetto, double probTransizioneGuarito, double probTransizioneDeceduto, double probTransizioneReInfezione) throws InterruptedException {
        int infetti = 0; // Popolazione infetta all'inizio
        int guariti = 0; // Popolazione guarita all'inizio
        int deceduti = 0; // Popolazione deceduta all'inizio
        int popolazioneTotale = sani; // La popolazione totale è inizialmente uguale alla popolazione sana

        // Giorno 0: popolazione interamente sana
        System.out.println("Il Primo giorno, la popolazione è totalmente sana.");
        System.out.println("Giorno 0: Sani = " + sani + ", Infetti = " + infetti + ", Guariti = " + guariti + ", Deceduti = " + deceduti);
        Thread.sleep(3000);
        cornice(); // Stampa un separatore per migliorare la leggibilità

        // Loop che simula il progresso della malattia per il numero di giorni inserito dall'utente
        for (int i = 1; i <= giorniTotali; i++) {
            // Calcolo del numero di nuove persone che transizionano da uno stato all'altro
            double nuoviInfettiSani = sani * probTransizioneInfetto;
            double nuoviInfettiGuariti = guariti * probTransizioneReInfezione;
            double nuoviGuariti = infetti * probTransizioneGuarito;
            double nuoviDeceduti = infetti * probTransizioneDeceduto;

            // Aggiornamento degli stati: diminuzione dei sani, aumento degli infetti, guariti e deceduti
            sani -= (int) nuoviInfettiSani;
            infetti += (int) nuoviInfettiSani + (int) nuoviInfettiGuariti;
            infetti -= (int) nuoviGuariti;
            guariti += (int) nuoviGuariti;
            deceduti += (int) nuoviDeceduti;

            // Verifica per evitare che i valori diventino negativi
            sani = Math.max(sani, 0);
            infetti = Math.max(infetti, 0);

            // Correzione per assicurarsi che la somma delle variabili non superi la popolazione totale
            if (sani + infetti + guariti + deceduti > popolazioneTotale) {
                int eccedenza = (sani + infetti + guariti + deceduti) - popolazioneTotale;
                infetti -= eccedenza; // Sottrae l'eccedenza dagli infetti
            }

            // Stampa lo stato aggiornato al giorno corrente
            cornice();
            System.out.println("Giorno " + i + ": Sani = " + sani + ", Infetti = " + infetti + ", Guariti = " + guariti + ", Deceduti = " + deceduti);
            System.out.println("La somma di tutte le variabili è: " + (sani + infetti + guariti + deceduti) + "\n");
            cornice();

            Thread.sleep(2000); // Pausa tra un giorno e l'altro per simulare il passare del tempo
        }
    }

    // Funzione per stampare la presentazione delle probabilità
    private static void stampePresentazione(double probTransizioneInfetto, double probTransizioneDeceduto, double probTransizioneGuarito, double probTransizioneReInfezione) throws InterruptedException {
        cornice();
        System.out.println(
                "In questa simulazione, tenterò di fare una previsione, basata sui meri dati numerici, dell'andamento ipotetico del 'Covid-19'\n" +
                        "Si prende di riferimento un periodo di analisi dal 24/02/2020 al 01/12/2023 \n" +
                        "In particolare, quello che servirà sono le probabilità di transizione da uno stato ad un altro.\n" +
                        "Quelle da noi prese in considerazione saranno:\n");
        Thread.sleep(2000);
        System.out.println("Probabilità di transizione Guarito: Useremo un valore fissato a: " + (probTransizioneGuarito * 100) + "%\n");

        Thread.sleep(2000);
        System.out.println("Probabilità di transizione Deceduto: Useremo un valore fissato a: " + (probTransizioneDeceduto * 100) + "%\n");

        Thread.sleep(2000);
        System.out.println("Probabilità di transizione Infetto: Useremo un valore fissato a: " + (probTransizioneInfetto * 100) + "%\n");

        Thread.sleep(2000);
        System.out.println("Probabilità di re-infezione: Useremo un valore fissato a: " + (probTransizioneReInfezione * 100) + "%\n");

    }

    // Stampa le informazioni sull'esecuzione della simulazione
    private static void stampeEsecuzione() {
        System.out.println(
                "Una volta ottenute queste probabilità, avviamo la nostra simulazione.\n" +
                        "Prendiamo di riferimento una popolazione di " + getPopolazioneScelta() + " abitanti sani e scegliamo in maniera randomica\n" +
                        "che una parte della popolazione si ammali.\n" +
                        "Scelto un lasso di tempo di " + getGiorniTotaliScelti() + " giorni, vediamo come procede la nostra generazione!\n");
        cornice();
    }

    // Funzione per stampare una cornice di separazione
    private static void cornice() {
        System.out.println("+----------------------------------------------------------------------------------------------------------------+\n");
    }

    // Funzione che chiede all'utente il numero di giorni di simulazione
    private static void askGiorniTot() {
        Scanner sc = new Scanner(System.in);
        cornice();
        System.out.print("Vorrei che decidessi quanti giorni durerà la simulazione, fino ad un massimo di un mese [31 giorni]!\n");

        int giorniTot;
        do {
            System.out.print("Inserisci il numero di giorni: ");
            giorniTot = sc.nextInt();

            // Verifica che il numero di giorni sia valido
            if (giorniTot <= 0) {
                System.out.println("Il numero di giorni deve essere maggiore di zero.");
            } else if (giorniTot > 31) {
                System.out.println("Il numero di giorni deve essere minore di trenta.");
            }
        } while (giorniTot <= 0 || giorniTot > 31);

        setGiorniTotaliDaInput(giorniTot);
        cornice();
        System.out.println("\n");
    }

    // Funzione che chiede all'utente la popolazione iniziale
    private static void askSani() {
        Scanner sc = new Scanner(System.in);
        cornice();
        System.out.print("Ora mi serve sapere la dimensione della popolazione totale.\n");

        int sani;
        do {
            System.out.print("Inserisci la dimensione della popolazione totale: ");
            sani = sc.nextInt();

            if (sani <= 0) {
                System.out.println("La dimensione della popolazione totale deve essere maggiore di zero.");
            }
        } while (sani <= 0);

        setSani(sani);
        cornice();
        System.out.println("\n");
    }

    // Setter per la variabile sani
    public static void setSani(int sani) {
        MarkovOnDataCovid.sani = sani;
    }

    // Getter per la popolazione inserita
    public static int getPopolazioneScelta() {
        return sani;
    }

    // Getter per i giorni di simulazione inseriti
    public static int getGiorniTotaliScelti() {
        return giorniTotaliDaInput;
    }

    // Setter per i giorni totali di simulazione
    public static void setGiorniTotaliDaInput(int giorniTotaliDaInput) {
        MarkovOnDataCovid.giorniTotaliDaInput = giorniTotaliDaInput;
    }
}

