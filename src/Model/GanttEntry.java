package Model;

import java.util.List;

public class GanttEntry {
    private String processLabel; // "P1" or "Idle"
    private int startTime;
    private int endTime;
    private List<GanttEntry> ganttData;

    public GanttEntry(String processLabel, int startTime, int endTime) {
        this.processLabel = processLabel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProcessLabel() { return processLabel; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }
    public List<GanttEntry> getGanttData() {
       return ganttData;
   }
    
    @Override
    public String toString() {
        return processLabel + " [" + startTime + " -> " + endTime + "]";
    }
}