package Metrics;

import Model.Process;
import java.util.List;

public class MetricsCalculator {

    public static void calculate(List<Process> processes) {
        for (Process p : processes) {
            int tat = p.getFinishTime() - p.getArrivalTime(); // TAT = Finish - Arrival
            int wt  = tat - p.getBurstTime();                 // WT  = TAT - Burst
            p.setTurnaroundTime(tat);
            p.setWaitingTime(wt);
            if (p.getResponseTime() == -1) p.setResponseTime(wt);
        }
    }

    public static double averageWT(List<Process> processes) {
        double total = 0;
        for (Process p : processes) total += p.getWaitingTime();
        return total / processes.size();
    }

    public static double averageTAT(List<Process> processes) {
        double total = 0;
        for (Process p : processes) total += p.getTurnaroundTime();
        return total / processes.size();
    }

    public static double averageRT(List<Process> processes) {
        double total = 0;
        for (Process p : processes) total += p.getResponseTime();
        return total / processes.size();
    }

    public static void printResults(String algorithmName, List<Process> processes) {
        System.out.println("\n========================================");
        System.out.println("  Results — " + algorithmName);
        System.out.println("========================================");
        System.out.printf("%-6s %-10s %-10s %-6s %-6s %-6s%n",
                "PID", "Arrival", "Burst", "WT", "TAT", "RT");
        System.out.println("----------------------------------------");
        for (Process p : processes) {
            System.out.printf("%-6s %-10d %-10d %-6d %-6d %-6d%n",
                    "P" + p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime(),
                    p.getResponseTime());
        }
        System.out.println("----------------------------------------");
        System.out.printf("%-28s %-6.2f %-6.2f %-6.2f%n",
                "Average",
                averageWT(processes),
                averageTAT(processes),
                averageRT(processes));
        System.out.println("========================================\n");
    }

    public static void printComparison(List<Process> rrProcesses, List<Process> priorityProcesses) {
        double rrAvgWT  = averageWT(rrProcesses);
        double rrAvgTAT = averageTAT(rrProcesses);
        double rrAvgRT  = averageRT(rrProcesses);
        double prAvgWT  = averageWT(priorityProcesses);
        double prAvgTAT = averageTAT(priorityProcesses);
        double prAvgRT  = averageRT(priorityProcesses);

        System.out.println("========================================");
        System.out.println("       COMPARISON SUMMARY");
        System.out.println("========================================");
        System.out.printf("%-20s %-15s %-15s%n", "Metric", "Round Robin", "Priority");
        System.out.println("----------------------------------------");
        System.out.printf("%-20s %-15.2f %-15.2f%n", "Avg Waiting Time",  rrAvgWT,  prAvgWT);
        System.out.printf("%-20s %-15.2f %-15.2f%n", "Avg Turnaround",    rrAvgTAT, prAvgTAT);
        System.out.printf("%-20s %-15.2f %-15.2f%n", "Avg Response Time", rrAvgRT,  prAvgRT);
        System.out.println("----------------------------------------");
        System.out.println("Better Avg WT        → " + (rrAvgWT  <= prAvgWT  ? "Round Robin" : "Priority Scheduling"));
        System.out.println("Better Avg RT        → " + (rrAvgRT  <= prAvgRT  ? "Round Robin" : "Priority Scheduling"));

        double rrStd = stdDevWT(rrProcesses);
        double prStd = stdDevWT(priorityProcesses);
        System.out.println("More Fair (StdDev)   → " + (rrStd <= prStd ? "Round Robin" : "Priority Scheduling"));
        System.out.printf("  RR=%.2f | Priority=%.2f%n", rrStd, prStd);
        System.out.println("Starvation Risk      → " + checkStarvation(priorityProcesses));
        System.out.println("========================================\n");
    }

    private static double stdDevWT(List<Process> processes) {
        double avg = averageWT(processes);
        double sumSq = 0;
        for (Process p : processes) {
            double diff = p.getWaitingTime() - avg;
            sumSq += diff * diff;
        }
        return Math.sqrt(sumSq / processes.size());
    }

    private static String checkStarvation(List<Process> processes) {
        double avg = averageWT(processes);
        for (Process p : processes) {
            if (avg > 0 && p.getWaitingTime() > 3 * avg) {
                return "YES — P" + p.getId() + " waited " + p.getWaitingTime()
                        + " (avg=" + String.format("%.1f", avg) + ")";
            }
        }
        return "No starvation detected";
    }
}
