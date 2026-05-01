package Scheduler;

import Model.Process;
import Model.Process_Queue;
import java.util.ArrayList;
import java.util.List;

public class SRTFScheduler {

    private Process_Queue processQueue;
    private List<String> executionTimeline;

    public SRTFScheduler(Process_Queue queue) {
        this.processQueue = queue;
        this.executionTimeline = new ArrayList<>();
    }

    public List<String> run() {
List<Process> processes = processQueue.getAll();
        executionTimeline = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        while (completed < n) {

            // اختيار أقصر remaining time من الـ processes اللي وصلت
            Process shortest = null;
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (shortest == null ||
                        p.getRemainingTime() < shortest.getRemainingTime()) {
                        shortest = p;
                    }
                }
            }

            // لو مفيش process وصلت → CPU idle
            if (shortest == null) {
                executionTimeline.add("Idle");
                currentTime++;
                continue;
            }

            // تسجيل start time وresponse time أول مرة بس
            if (!shortest.isStarted()) {
                shortest.setStarted(true);
                shortest.setResponseTime(currentTime - shortest.getArrivalTime());
            }

            // تنفيذ وحدة واحدة
            executionTimeline.add("P" + shortest.getId());
            shortest.setRemainingTime(shortest.getRemainingTime() - 1);
            currentTime++;

            // لو خلص
            if (shortest.getRemainingTime() == 0) {
                completed++;
                shortest.setFinishTime(currentTime);
                shortest.setTurnaroundTime(currentTime - shortest.getArrivalTime());
                shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBurstTime());
            }
        }

        return executionTimeline;
    }

    public List<String> getExecutionTimeline() {
        return executionTimeline;
    }

    public void printResults() {
        List<Process> processes = processQueue.getAll();
        System.out.println("=== SRTF Scheduling Results ===");
        System.out.printf("%-5s %-10s %-8s %-10s %-13s %-10s %-14s%n",
                "PID", "Arrival", "Burst", "Finish", "Turnaround", "Waiting", "Response");
        for (Process p : processes) {
            System.out.printf("%-5s %-10d %-8d %-10d %-13d %-10d %-14d%n",
                    "P" + p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getFinishTime(),
                    p.getTurnaroundTime(),
                    p.getWaitingTime(),
                    p.getResponseTime());
        }
        System.out.println("\nExecution Timeline: " + executionTimeline);
    }
}