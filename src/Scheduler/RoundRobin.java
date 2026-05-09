package Scheduler;

import Model.Process;
import java.util.*;
import util.Validation;

public class RoundRobin {

    public static void run(List<Process> processes, int quantum) {
        if(!Validation.isValidQuantum(quantum)){
            System.out.println("Invalid Quantum");
            return;
        }
        
        Queue<Process> queue = new LinkedList<>();
//        int currentTime = 0;   علشان لو في اي idle
        int currentTime = processes.get(0).getArrivalTime();
        
        List<Integer> timeline = new ArrayList<>();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        int i = 0;
        queue.add(processes.get(0));
        i++;
        while (!queue.isEmpty()) {
            Process current = queue.poll();
            if (!current.isStarted()) {
                current.setStarted(true);
                current.setResponseTime(currentTime - current.getArrivalTime());
            }
            if (current.getRemainingTime() > quantum) {
                currentTime += quantum;
                current.setRemainingTime(current.getRemainingTime() - quantum);
                timeline.add(current.getId());

                while (i < processes.size() && processes.get(i).getArrivalTime() <= currentTime) {
                    queue.add(processes.get(i));
                    i++;
                }
                queue.add(current);
            } 
            else {   
                currentTime += current.getRemainingTime();
                current.setRemainingTime(0);
                current.setFinishTime(currentTime);
                timeline.add(current.getId());
                while (i < processes.size() && processes.get(i).getArrivalTime() <= currentTime) {
                    queue.add(processes.get(i));
                    i++;
                }
            }
            if (queue.isEmpty() && i < processes.size()) {
                currentTime = processes.get(i).getArrivalTime();
                queue.add(processes.get(i));
                i++;
            }
        }
        System.out.println("Timeline: " + timeline);
        for (Process p : processes) {
            System.out.println("P" + p.getId() +
                    " finished at " + p.getFinishTime() +
                    " | Response Time: " + p.getResponseTime());
        }
    }
}