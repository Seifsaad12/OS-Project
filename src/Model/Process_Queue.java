package Model;

import java.util.ArrayList;
import java.util.List;

public class Process_Queue {

    private List<Process> processes;  // ← Model.Process مش java.lang.Process

    public Process_Queue() {
        this.processes = new ArrayList<>();
    }

    public void addProcess(Process p) { processes.add(p); }
    public void removeProcess(Process p) { processes.remove(p); }
    public List<Process> getAll() { return processes; }
    public int size() { return processes.size(); }
    public boolean isEmpty() { return processes.isEmpty(); }
    public void clear() { processes.clear(); }
}