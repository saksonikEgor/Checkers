package checkers.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result1 = JUnitCore.runClasses(BoardTest.class);
        Result result2 = JUnitCore.runClasses(IOTest.class);

        for (Failure failure : result1.getFailures())
            System.out.println(failure.toString());

        for (Failure failure : result2.getFailures())
            System.out.println(failure.toString());

        System.out.println(result1.wasSuccessful());
        System.out.println(result2.wasSuccessful());
    }
}
