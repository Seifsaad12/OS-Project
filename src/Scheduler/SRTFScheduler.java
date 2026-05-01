package Scheduler;
import Model.GanttEntry;
import Model.Process;
import Model.Process_Queue;
import java.util.ArrayList;
import java.util.List;

public class SRTFScheduler {
    private Process_Queue processQueue;
    private List<String> executionTimeline;
    private List<GanttEntry> ganttData;

    public SRTFScheduler(Process_Queue queue) {
        this.processQueue = queue;
        this.executionTimeline = new ArrayList<>();
        this.ganttData = new ArrayList<>();
    }

    public List<String> run() {
        List<Process> processes = processQueue.getAll();
        executionTimeline = new ArrayList<>();
        ganttData = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        String lastLabel = null;
        int segmentStart = 0;

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

            String currentLabel;

            // لو مفيش process وصلت → CPU idle
            if (shortest == null) {
                currentLabel = "Idle";
                executionTimeline.add("Idle");
                currentTime++;
            } else {
                currentLabel = "P" + shortest.getId();

                // تسجيل response time أول مرة بس
                if (!shortest.isStarted()) {
                    shortest.setStarted(true);
                    shortest.setResponseTime(currentTime - shortest.getArrivalTime());
                }

                // تنفيذ وحدة واحدة
                executionTimeline.add(currentLabel);
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

            // بناء Gantt segments
            if (lastLabel == null) {
                lastLabel = currentLabel;
                segmentStart = 0;
            } else if (!currentLabel.equals(lastLabel)) {
                ganttData.add(new GanttEntry(lastLabel, segmentStart, currentTime - 1));
                segmentStart = currentTime - 1;
                lastLabel = currentLabel;
            }
        }

        // إضافة آخر segment
        if (lastLabel != null) {
            ganttData.add(new GanttEntry(lastLabel, segmentStart, currentTime));
        }

        return executionTimeline;
    }

    public List<GanttEntry> getGanttData() {
        return ganttData;
    }

    public List<String> getExecutionTimeline() {
        return executionTimeline;
    }

    public void printResults() {
        List<Process> processes = processQueue.getAll();

        System.out.println("=== SRTF (SJF Preemptive) Scheduling Results ===");
        System.out.printf("%-5s %-10s %-8s %-12s %-13s %-10s %-14s%n",
                "PID", "Arrival", "Burst", "Completion", "Turnaround", "Waiting", "Response");

        // ترتيب الجدول حسب completion time
        processes.sort((a, b) -> a.getFinishTime() - b.getFinishTime());

        for (Process p : processes) {
            System.out.printf("%-5s %-10s %-8s %-12s %-13s %-10s %-14s%n",
        "P" + p.getId(),
        p.getArrivalTime(),
        p.getBurstTime(),
        p.getFinishTime(),
        p.getTurnaroundTime(),
        p.getWaitingTime(),
        p.getResponseTime());
        }

        System.out.println("\n--- Gantt Chart Data ---");
        for (GanttEntry entry : ganttData) {
            System.out.println(entry);
        }

        System.out.println("\nFull Timeline: " + executionTimeline);
    }
}