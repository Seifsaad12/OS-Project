/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Model.Process;
import Model.Process_Queue;
import Model.GanttEntry;
import static Scheduler.RoundRobin.run;
import Scheduler.SRTFScheduler;
import java.util.ArrayList;
import java.util.List;
import util.Validation;
/**
 *
 * @author TUF
 */
public class Main {
    public static void main(String[] args) {
//        System.out.println(5);
//           Process_Queue queue = new Process_Queue();
//        queue.addProcess(new Process(1, 0, 8));  // P1: arrival=0, burst=8
//        queue.addProcess(new Process(2, 1, 4));  // P2: arrival=1, burst=4
//        queue.addProcess(new Process(3, 2, 9));  // P3: arrival=2, burst=9
//        queue.addProcess(new Process(4, 3, 5));  // P4: arrival=3, burst=5
//
//        
//        SRTFScheduler scheduler = new SRTFScheduler(queue);
//        scheduler.run();
//
//        // طباعة النتائج
//        scheduler.printResults();
//        // GantData
//        System.out.println("\n--- Verifying Gantt Data ---");
//        List<GanttEntry> gantt = scheduler.getGanttData();
//        for (GanttEntry entry : gantt) {
//            System.out.println(entry);
//        }
//        
//        
//         List<Process> processes = new ArrayList<>();
//        processes.add(new Process(1, 0, 5));  // P1: arrival = 0, burst = 5
//        processes.add(new Process(2, 1, 6));  // P2: arrival = 1, burst = 6
//        processes.add(new Process(3, 2, 3));  // P3: arrival = 2, burst = 3
//
//        // تعيين Quantum = 4
//        run(processes, 4);
        
        
         try {
            // إنشاء قائمة العمليات
            List<Process> processes = new ArrayList<>();
            Process_Queue queue = new Process_Queue();

            // قائمة لحفظ العمليات وإجراء التحقق من التكرار
            ArrayList<Process> existingProcesses = new ArrayList<>();

            // إضافة العمليات مع التحقق من صلاحية المعرفات
            Process p1 = new Process(0, 8);  // P1: arrival=0, burst=8
            if (p1 != null && !Validation.isDuplicateId(p1.getId(), existingProcesses)) {
                queue.addProcess(p1);
                existingProcesses.add(p1);
            }

            Process p2 = new Process(1, 4);  // P2: arrival=1, burst=4
            if (p2 != null && !Validation.isDuplicateId(p2.getId(), existingProcesses)) {
                queue.addProcess(p2);
                existingProcesses.add(p2);
            }

            Process p3 = new Process(2, 9);  // P3: arrival=2, burst=9
            if (p3 != null && !Validation.isDuplicateId(p3.getId(), existingProcesses)) {
                queue.addProcess(p3);
                existingProcesses.add(p3);
            }

            Process p4 = new Process(3, 5);  // P4: arrival=3, burst=5
            if (p4 != null && !Validation.isDuplicateId(p4.getId(), existingProcesses)) {
                queue.addProcess(p4);
                existingProcesses.add(p4);
            }

            // تشغيل الخوارزمية SRTF
            SRTFScheduler scheduler = new SRTFScheduler(queue);
            scheduler.run();
            scheduler.printResults();

            // Reset الـ ID counter بعد SRTF قبل تشغيل Round Robin
            Process.resetIdCounter();

            // إضافة العمليات لخوارزمية Round Robin
            processes.clear();
            processes.add(new Process(0, 5));  // P1: arrival=0, burst=5
            processes.add(new Process(1, 6));  // P2: arrival=1, burst=6
            processes.add(new Process(2, 3));  // P3: arrival=2, burst=3

            // إضافة العمليات لـ existingProcesses بعد Round Robin
            existingProcesses.clear();
            existingProcesses.addAll(processes);

            // تعيين Quantum = 4
            run(processes, 4);  // تشغيل Round Robin مع Quantum = 4

        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }
}
