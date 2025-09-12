MARKOV
Bachelor’s Thesis Proposal — Computer Engineering

This thesis project focuses on the study and implementation of Markov chains in various practical scenarios, exploring their potential applications through simulation and algorithmic modeling. 
The work is structured into three main case studies:
*************************************************************************************************************************************************************************************************
1-Text Generation from an Input Corpus

The system takes an input text and:
-Builds a dictionary from the corpus;
-Assigns transition probabilities to each word in the dictionary;
-Generates a new text by selecting each word according to these probabilities.

The output text length is determined by the user and depends on a configurable KEY, which defines the size of the word chains used in the generation process.
*************************************************************************************************************************************************************************************************
2-Epidemic Progression Simulation (COVID-19 Case Study)

This simulation models the spread of COVID-19 within a fictional population:
-Transition probabilities are defined for the states: healthy, infected, recovered, and deceased;
-The user specifies the population size and simulation duration;

The simulation outputs daily population statistics.
The underlying computation ensures that, at every step, the sum of individuals across all states matches the total population, maintaining mathematical consistency.
*************************************************************************************************************************************************************************************************
3- Server-Request Simulation Using Markov Chains

This scenario simulates client requests to a server, where the number of clients evolves according to a Markov chain:

-The number of STEPS and the MAXIMUM NUMBER of clients are defined;
-At each step, the number of clients changes based on the current state and a transition matrix;
-The transition matrix for each step determines the next client count by selecting the state with the highest transition probability;
-The system logs simulated GET, POST, PUT, and DELETE requests, along with server responses, service times, and response times for each client;
-The collected data is analyzed, encoded in a QR code, compressed into a ZIP file, and uploaded to Google Drive.
*************************************************************************************************************************************************************************************************
Executable Files: The provided .exe programs allow execution of the code, requiring Java and WinRAR to run.
