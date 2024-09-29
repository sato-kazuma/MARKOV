package queue.ClientServer;

public class ClientData {
    public int clientNumber;
    public long waitTime;
    public long serviceTime;

    public ClientData(int number, long waitTime, long serviceTime) {
        this.clientNumber = number;
        this.waitTime = waitTime;
        this.serviceTime = serviceTime;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    // Getter per il numero del cliente
    public int getClientNumber() {
        return clientNumber;
    }

    // Getter per il tempo di attesa
    public long getWaitTime() {
        return waitTime;
    }

    // Getter per il tempo di servizio
    public long getServiceTime() {
        return serviceTime;
    }
}
