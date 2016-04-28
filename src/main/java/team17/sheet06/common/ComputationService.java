package team17.sheet06.common;

import team17.sheet06.common.IComputationService;
import team17.sheet06.common.IJob;
import team17.sheet06.common.IJobDoneCallback;
import team17.sheet06.common.Job;

import java.rmi.RemoteException;
import java.util.concurrent.*;


public class ComputationService implements IComputationService {

    private ExecutorService executor;
    private volatile int currentJobs;
    private final int MAX_JOBS = 1;

    public ComputationService(){
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public <T> IJob<T> submit(Callable<T> task, IJobDoneCallback<T> callback) throws RemoteException {

        if (currentJobs >= MAX_JOBS) return null;

        incrementJobCount();

        Job<T> job = new Job<>();

        Runnable taskRunner = new Runnable() {

            @Override
            public void run() {

                try {
                    final T x = task.call();

                    callback.setResult(x);
                    decrementJobCount();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        executor.execute(taskRunner);

        return job;
    }


    private synchronized void incrementJobCount(){
        this.currentJobs++;
    }

    private synchronized void decrementJobCount(){
        this.currentJobs--;
    }
}
