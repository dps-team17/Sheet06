package team17.sheet06;

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

    private Registry registry;
    private IComputationService computationService;
    private IJobDoneCallback<Integer> callback;
    private IJob<Integer> pendingJob;

    public Client() {

    }

    private void init() {

        try {
            registry = LocateRegistry.getRegistry();
            computationService = (IComputationService) registry.lookup(IComputationService.SERVICE_NAME);

            callback = (IJobDoneCallback) UnicastRemoteObject.exportObject(this, 0);

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

            if(pendingJob == null){
                System.out.println("Ach wie schade...");
                return;
            }

            try{
                System.out.printf("",pendingJob.getResult());
            }
            catch (IllegalStateException e){
                System.err.println("Invalid read: Value not ready");
            }

            System.out.println("Waiting for love....");

            int x = 1;
            while (!pendingJob.isDone()) {
                Thread.sleep(1000);
                System.out.printf("Waiting since %d seconds\n", x++);
            }

            UnicastRemoteObject.unexportObject(this, false);
            System.out.printf("The fibonacci number %d is %d\n", n, pendingJob.getResult());

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] main) {

        Client client = new Client();
        client.run();
    }

    @Override
    public void setResult(Integer result) {
        pendingJob.setResult(result);
    }
}
