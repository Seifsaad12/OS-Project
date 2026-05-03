
package util;
import java.util.ArrayList;
import Model.Process;

public class Validation {
    // NO negative Values
     public static boolean isValidProcess(int id, int arrival, int burst) {
        if (id < 0 || arrival < 0 || burst <= 0) {
            return false;
        }
        return true;
    }
     
     public static boolean isDuplicateId(int id, ArrayList<Process> existing) {
        for (Process p : existing) {
            if (p.getId() == id) return true;
        }
        return false;
    }
     
     public static boolean isValidQuantum(int quantum) {
        return quantum > 0;
    }
     
}
