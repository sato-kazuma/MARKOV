package Matrix;

public class StateofMatrix {
    private int currentState;
    private double[][] transitionMatrix; // Matrice delle probabilità di transizione

    public StateofMatrix(int initialState, double[][] transitionMatrix) {
        this.currentState = initialState;
        this.transitionMatrix = transitionMatrix; // Inizializza la matrice di transizione
    }

    public int updatingState(int currentState) {
        // Ottieni la riga corrispondente allo stato corrente
        double[] probabilities = transitionMatrix[currentState];

        // Inizializza maxProbability e maxState
        double maxProbability = probabilities[0]; // Considera il primo valore come massimo iniziale
        int maxState = 0; // Inizializza maxState all'indice 0

        // Scorri i valori nella riga per trovare il massimo
        for (int nextState = 0; nextState < probabilities.length; nextState++) {

            if (probabilities[nextState] > maxProbability) {
                maxProbability = probabilities[nextState];
                maxState = nextState; // Aggiorna maxState se trovi una probabilità più alta
            }
        }

        return maxState; // Restituisce l'indice dello stato con la probabilità massima
    }


}
