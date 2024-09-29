package Matrix;


import queue.ClientServer.ClientData;

import java.util.List;

public class ServiceAndAverageTime {
    public void analyzeData(List<ClientData> clientDataList) {

        System.out.println("Analiziamo ora il tempo di attesa medio e il tempo di servizio medio!\n");

        WaitAverageTime(clientDataList);
        double[] arrayTemp = getWaitTimesArray(clientDataList);
        MakeHistogram.makeHistogram(arrayTemp, "Wait Time Graph", 10, 0);

        WaitServiceTime(clientDataList);
        arrayTemp = getServiceTimesArray(clientDataList);
        MakeHistogram.makeHistogram(arrayTemp, "Service Time Graph", 800, 0);
    }

    private void WaitAverageTime(List<ClientData> clientDataList) {
        double totalWaitTime = 0;

        for (ClientData clientData : clientDataList) {
            totalWaitTime += clientData.waitTime;
        }

        double averageWaitTime = (clientDataList.size() == 0) ? 0 : totalWaitTime / clientDataList.size();
        System.out.println("Tempo di attesa medio: " + averageWaitTime + " ms");
    }

    private double[] getWaitTimesArray(List<ClientData> clientDataList) {
        double[] waitTimesArray = new double[clientDataList.size()];

        for (int i = 0; i < clientDataList.size(); i++) {
            waitTimesArray[i] = clientDataList.get(i).waitTime;
        }

        return waitTimesArray;
    }

    private void WaitServiceTime(List<ClientData> clientDataList) {
        double totalServiceTime = 0;

        for (ClientData clientData : clientDataList) {
            totalServiceTime += clientData.serviceTime;
        }

        double averageServiceTime = (clientDataList.size() == 0) ? 0 : totalServiceTime / clientDataList.size();
        System.out.println("Tempo di servizio medio: " + averageServiceTime + " ms");
    }

    private double[] getServiceTimesArray(List<ClientData> clientDataList) {
        double[] serviceTimesArray = new double[clientDataList.size()];

        for (int i = 0; i < clientDataList.size(); i++) {
            serviceTimesArray[i] = clientDataList.get(i).serviceTime;
        }

        return serviceTimesArray;
    }
}
