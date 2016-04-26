package team17.sheet06.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface ComputationService extends Remote{

    public <T> Job<T> submit(Callable<T> job) throws RemoteException;
}
