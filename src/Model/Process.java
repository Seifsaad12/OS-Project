
package Model;

public class Process {
      private int id;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;
    private int startTime;
    private int finishTime;
    private boolean started;

    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.responseTime = -1;
        this.started = false;
    }

   
    public int getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int t) { this.remainingTime = t; }
    
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int t) { this.waitingTime = t; }
    
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int t) { this.turnaroundTime = t; }
    
    public int getResponseTime() { return responseTime; }
    public void setResponseTime(int t) { this.responseTime = t; }
    
    public int getFinishTime() { return finishTime; }
    public void setFinishTime(int t) { this.finishTime = t; }
    
    public boolean isStarted() { return started; }
    public void setStarted(boolean s) { this.started = s; }

    @Override
    public String toString() {
        return "P" + id + 
               " [arrival=" + arrivalTime + 
               ", burst=" + burstTime + "]";
    }
}
