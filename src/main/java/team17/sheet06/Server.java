
package team17.sheet06;

import team17.sheet06.common.ComputationService;
import team17.sheet06.common.IComputationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

class Server {

    public static void main(String args[]) {

        //Create Security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            IComputationService computationService = new ComputationService();
            IComputationService computationServiceStub = (IComputationService) UnicastRemoteObject.exportObject(computationService,0);

            // Bind stubs in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(IComputationService.SERVICE_NAME, computationServiceStub);

            System.out.println("Server ready");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
