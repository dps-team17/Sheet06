package team17.sheet06.common;


public interface Job<T> {

    public boolean isDone();

    public T getResult();
}