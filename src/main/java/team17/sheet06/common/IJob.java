package team17.sheet06.common;


public interface IJob<T> {

    public boolean isDone();

    public T getResult();

    public void setResult(T x);
}