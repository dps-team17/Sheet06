package Server;

import team17.sheet06.common.CalculateFibonacciTask;
import team17.sheet06.common.IComputationService;
import team17.sheet06.common.IJob;
import team17.sheet06.common.IJobDoneCallback;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Client implements IJobDoneCallback<Integer> {

    private static int CLIENTS_CREATED = 0;

    private IComputationService computationService;
    private IJobDoneCallback<Integer> callback;
    private IJob<Integer> pendingJob;
    private int id;

    public Client() {
        this.id = CLIENTS_CREATED++;
    }

    private void init() {

        try {
            Registry registry = LocateRegistry.getRegistry();
            computationService = (IComputationService) registry.lookup(IComputationService.SERVICE_NAME);

            callback = (IJobDoneCallback<Integer>) UnicastRemoteObject.exportObject(this, 0);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        init();

        try {
            int n = 46;
            CalculateFibonacciTask fibonacciTask = new CalculateFibonacciTask(n);
            pendingJob = computationService.submit(fibonacciTask, callback);

            if (pendingJob == null) {
                log("Service not available");

                UnicastRemoteObject.unexportObject(this, false);
                return;
            }

            log("Waiting for love....");

            while (!pendingJob.isDone()) {
                Thread.sleep(1000);
                //System.out.printf("Waiting since %d seconds\n", x++);
            }

            UnicastRemoteObject.unexportObject(this, false);
            log(String.format("The fibonacci number %d is %d", n, pendingJob.getResult()));

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void log(String message) {
        System.out.printf("%d: %s\n",id, message);
    }

    @Override
    public void setResult(Integer result) {
        pendingJob.setResult(result);
    }


    public static void main(String[] main) {

        for(int i = 0; i < 10; i++) {

            new Thread (){
                public void run(){
                    Client client = new Client();
                    client.run();
                }
            }.start();
        }
    }
}
