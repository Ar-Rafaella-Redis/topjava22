package ru.javawebinar.topjava;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

public class MyJUnitStopWatch extends Stopwatch{

    private static List<String> summary = new ArrayList<>();

    public static void printSummary(){
        for (String res: summary){
            System.out.println(res);
        }

    }

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        String result = String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos));
        System.out.println(result);
        summary.add(result);
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
}