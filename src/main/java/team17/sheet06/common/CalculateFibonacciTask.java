package team17.sheet06.common;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class CalculateFibonacciTask implements Callable<Integer>, Serializable {

    private int number;

    public CalculateFibonacciTask(int n) {
        number = n;
    }

    private Integer getFibonacciNumber(int x) {

        Integer result;
        if (x == 1 || x == 2) {
            result = 1;
        } else {
            result= getFibonacciNumber(x - 1) + getFibonacciNumber(x - 2);
        }

        return result;
    }

    @Override
    public Integer call() throws Exception {
        return getFibonacciNumber(number);
    }

    public void setNumber(int n) {
        this.number = n;
    }

    public int getNumber() {
        return number;
    }
}
