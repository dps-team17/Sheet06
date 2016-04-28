package team17.sheet06.common;


import java.io.Serializable;

public class Job<T> implements IJob<T>, Serializable{

    private T result = null;

    @Override
    public synchronized boolean isDone() {
        return result != null;
    }

    @Override
    public synchronized T getResult() throws IllegalStateException {

        if(result == null) throw new IllegalStateException("Result not ready");
        return result;
    }

    @Override
    public synchronized void setResult(T x) {
        result = x;
    }
}
