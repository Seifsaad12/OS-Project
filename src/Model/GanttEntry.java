package Model;

public class GanttEntry {
    private String processLabel; // "P1" or "Idle"
    private int startTime;
    private int endTime;

    public GanttEntry(String processLabel, int startTime, int endTime) {
        this.processLabel = processLabel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProcessLabel() { return processLabel; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }

    @Override
    public String toString() {
        return processLabel + " [" + startTime + " → " + endTime + "]";
    }
}