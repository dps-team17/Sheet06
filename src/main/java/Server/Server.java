
package Server;

import com.sun.xml.internal.bind.v2.model.core.ID;
import team17.sheet06.common.IComputationService;
import team17.sheet06.common.IDispatcherService;

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

            Registry registry = LocateRegistry.getRegistry();
            IDispatcherService dispatcher = (IDispatcherService) registry.lookup(IDispatcherService.SERVICE_NAME);

            dispatcher.Register(computationServiceStub);
            System.out.println("Server ready");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
