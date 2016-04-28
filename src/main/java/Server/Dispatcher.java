package Server;


import team17.sheet06.common.IComputationService;
import team17.sheet06.common.IDispatcherService;
import team17.sheet06.common.IJob;
import team17.sheet06.common.IJobDoneCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class Dispatcher implements IComputationService, IDispatcherService {

    private final int MAX_TRIES = 3;

    private List<IComputationService> servers;
    private Random rnd;

    public Dispatcher() {

        servers = new ArrayList<>();
        rnd = new Random(System.currentTimeMillis());
    }

    @Override
    public <T> IJob<T> submit(Callable<T> job, IJobDoneCallback<T> callback) throws RemoteException {

        if (servers.size() == 0){
            System.err.println("No servers registered.");
            return null;
        }

        IComputationService server;
        IJob<T> result = null;
        int tries = 0;

        do{
            server = getRandomServer();
            result = server.submit(job, callback);
            tries++;
        }while(result == null && tries < MAX_TRIES);

        System.out.println("Request dispatched to " + server.toString());
        return result;
    }

    private IComputationService getRandomServer() {

        int ndx = rnd.nextInt(servers.size());
        return servers.get(ndx);
    }

    @Override
    public void Register(IComputationService server) throws RemoteException {

        System.out.println("Server registered: " + server.toString());
        this.servers.add(server);
    }

    public static void main(String[] args) {

        Dispatcher dispatcher = new Dispatcher();

        //Create Security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            // Create stubs
            final Remote remoteStub = UnicastRemoteObject.exportObject(dispatcher, 0);
            IComputationService computationServiceStub = (IComputationService) remoteStub;
            IDispatcherService dispatcherStub = (IDispatcherService) remoteStub;

            // Bind stubs in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(IComputationService.SERVICE_NAME, computationServiceStub);
            registry.rebind(IDispatcherService.SERVICE_NAME, dispatcherStub);

            System.out.println("Dispatcher running");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
