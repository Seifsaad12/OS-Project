
package Model;

import java.util.ArrayList;
import java.util.List;

public class Process_Queue {
    
    private List<java.lang.Process> processes;

    public Process_Queue() {
        this.processes = new ArrayList<>();
    }

    public void addProcess(java.lang.Process p) {
        processes.add(p);
    }

    public void removeProcess(java.lang.Process p) {
        processes.remove(p);
    }

    public List<java.lang.Process> getAll() {
        return new ArrayList<>(processes);
    }

    public int size() {
        return processes.size();
    }

    public boolean isEmpty() {
        return processes.isEmpty();
    }

    public void clear() {
        processes.clear();
    }
}
