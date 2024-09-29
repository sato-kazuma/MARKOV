package textlogic;

public class StampeOnText {

    public static void cornice() {
        /*Per evitare di sporcare il codice*/
        System.out.println("+----------------------------------------------------------------------------------------------------------------+\n");
    }

    public static void waiting() {
        /*Per evitare di sporcare il codice*/
        System.out.println("...  ...  ...  ... ...  ...  ...  ... ...  ...  ...  ... ...  ...  ...  ... ...  ...  ...  ... ...  ...  ...  ...\n");
    }

    public static void cicleSimulation() throws InterruptedException {
        /*Ciclo di simulazione*/
        cornice();
        Thread.sleep(500);
        waiting();
        Thread.sleep(500);
        cornice();
        Thread.sleep(500);
    }
}
