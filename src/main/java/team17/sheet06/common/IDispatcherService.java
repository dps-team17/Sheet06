package team17.sheet06.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDispatcherService extends Remote {

    public final String SERVICE_NAME = "IDispatcherService";

    public void Register(IComputationService server) throws RemoteException;
}
