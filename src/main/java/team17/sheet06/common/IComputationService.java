package team17.sheet06.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

public interface IComputationService extends Remote{

    public final String SERVICE_NAME = "IComputationService";

    public <T> IJob<T> submit(Callable<T> job, IJobDoneCallback<T> callback) throws RemoteException;

}
