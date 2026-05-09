package Model;

import util.Validation;

public class Process {
    private static int nextId = 1;
    
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
    private Validation val = new Validation();

    public Process( int arrivalTime, int burstTime) {
        if (!Validation.isValidProcess(arrivalTime, burstTime)) {
            throw new IllegalArgumentException("Invalid Process Info: arrival must be >= 0 and burst must be > 0");
        }

        this.id = nextId++;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.responseTime = -1;
        this.finishTime = 0;
        this.started = false;
    }


    public int getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }

    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int t) { 
        if(t<0){
            throw new IllegalArgumentException("Remaining time cannot be negative");
        }else{
            this.remainingTime = t;
        } }

    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int t) {
        if(t<0){
            throw new IllegalArgumentException("Waiting time cannot be negative");
        }else{
            this.waitingTime = t;
        } }

    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int t) {
        if(t<0){
            throw new IllegalArgumentException("Turnaround time cannot be negative");
        }else{
            this.turnaroundTime = t;
        } }

    public int getResponseTime() { return responseTime; }
    public void setResponseTime(int t) {
        if(t<0){
            throw new IllegalArgumentException("Respone time cannot be negative");
        }else{
            this.responseTime = t;
        }  }

    public int getFinishTime() { return finishTime; }
    public void setFinishTime(int t) { 
        if(t<0){
            throw new IllegalArgumentException("Finish time cannot be negative");
        }else{
            this.finishTime = t;
        }
         }

    public boolean isStarted() { return started; }
    public void setStarted(boolean s) { this.started = s; }
    
    public static void resetIdCounter(){
    nextId = 1;
    }

    @Override
    public String toString() {
        return "P" + id + 
               " [arrival=" + arrivalTime + 
               ", burst=" + burstTime + "]";
    }
}