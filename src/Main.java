/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Model.Process;
import Model.Process_Queue;
import Model.GanttEntry;
import Scheduler.SRTFScheduler;
import java.util.List;
/**
 *
 * @author TUF
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(5);
           Process_Queue queue = new Process_Queue();
        queue.addProcess(new Process(1, 0, 8));  // P1: arrival=0, burst=8
        queue.addProcess(new Process(2, 1, 4));  // P2: arrival=1, burst=4
        queue.addProcess(new Process(3, 2, 9));  // P3: arrival=2, burst=9
        queue.addProcess(new Process(4, 3, 5));  // P4: arrival=3, burst=5

        
        SRTFScheduler scheduler = new SRTFScheduler(queue);
        scheduler.run();

        // طباعة النتائج
        scheduler.printResults();

        // التحقق من الـ Gantt Data
        System.out.println("\n--- Verifying Gantt Data ---");
        List<GanttEntry> gantt = scheduler.getGanttData();
        for (GanttEntry entry : gantt) {
            System.out.println(entry);
        }
        
    }
}
