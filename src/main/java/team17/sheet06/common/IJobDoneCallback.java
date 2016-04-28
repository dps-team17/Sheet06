package team17.sheet06.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IJobDoneCallback<T> extends Remote {

    public void setResult(T result) throws RemoteException;
}
