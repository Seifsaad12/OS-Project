package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Person 7 — ComparisonPanel.java
 *
 * Shows a side-by-side comparison table of RR vs SRTF metrics,
 * then writes an automatic conclusion paragraph.
 *
 * ── HOW PERSON 6 USES THIS ──────────────────────────────────────────────────
 *
 *   // After Person 5's MetricsCalculator runs on both process lists:
 *
 *   double rrAvgWT   = MetricsCalculator.averageWT(rrProcesses);
 *   double rrAvgTAT  = MetricsCalculator.averageTAT(rrProcesses);
 *   double rrAvgRT   = MetricsCalculator.averageRT(rrProcesses);
 *
 *   double srtfAvgWT  = MetricsCalculator.averageWT(srtfProcesses);
 *   double srtfAvgTAT = MetricsCalculator.averageTAT(srtfProcesses);
 *   double srtfAvgRT  = MetricsCalculator.averageRT(srtfProcesses);
 *
 *   ComparisonPanel cp = new ComparisonPanel(
 *       rrAvgWT,  rrAvgTAT,  rrAvgRT,
 *       srtfAvgWT, srtfAvgTAT, srtfAvgRT
 *   );
 *   somePanel.add(cp);
 *
 * ────────────────────────────────────────────────────────────────────────────
 */
public class ComparisonPanel extends JPanel {

    // ── Colours ────────────────────────────────────────────────────────────────
    private static final Color RR_COLOR    = new Color(0x3B82F6);   // blue  → RR
    private static final Color SRTF_COLOR  = new Color(0xEF4444);   // red   → SRTF
    private static final Color WIN_BG      = new Color(0xD1FAE5);   // light green highlight for winner
    private static final Color HEADER_BG   = new Color(0x1E293B);   // dark header
    private static final Color ODD_ROW     = new Color(0xF8FAFC);
    private static final Color EVEN_ROW    = Color.WHITE;
    private static final Color BORDER_COL  = new Color(0xE2E8F0);
    private static final Color CONCLUSION_BG = new Color(0xEFF6FF);
    private static final Color CONCLUSION_BORDER = new Color(0xBFDBFE);

    // ── Metrics ────────────────────────────────────────────────────────────────
    private final double rrWT,  rrTAT,  rrRT;
    private final double srtfWT, srtfTAT, srtfRT;

    // ── Constructor ────────────────────────────────────────────────────────────
    public ComparisonPanel(
        double rrAvgWT,   double rrAvgTAT,   double rrAvgRT,
        double srtfAvgWT, double srtfAvgTAT, double srtfAvgRT
    ) {
        this.rrWT   = rrAvgWT;   this.rrTAT   = rrAvgTAT;   this.rrRT   = rrAvgRT;
        this.srtfWT = srtfAvgWT; this.srtfTAT = srtfAvgTAT; this.srtfRT = srtfAvgRT;

        setLayout(new BorderLayout(0, 14));
        setBackground(Color.WHITE);
        setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));

        // Section title
        JLabel title = new JLabel("📊  Comparison Summary");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0x0F172A));
        add(title, BorderLayout.NORTH);

        // Stack: table then conclusion
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.add(buildTable());
        body.add(Box.createVerticalStrut(16));
        body.add(buildConclusion());
        add(body, BorderLayout.CENTER);
    }

    // ── Metric table ───────────────────────────────────────────────────────────
    private JPanel buildTable() {
        // 4 columns: Metric | Round Robin | SRTF | Winner
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(LEFT_ALIGNMENT);

        // Header
        JPanel header = new JPanel(new GridLayout(1, 4, 1, 0));
        header.setBackground(HEADER_BG);
        for (String h : new String[]{"Metric", "Round Robin", "SRTF", "Winner ✓"}) {
            header.add(headerCell(h));
        }
        wrapper.add(header, BorderLayout.NORTH);

        // Rows
        Object[][] rows = {
            { "Avg Waiting Time (WT)",     rrWT,   srtfWT   },
            { "Avg Turnaround Time (TAT)", rrTAT,  srtfTAT  },
            { "Avg Response Time (RT)",    rrRT,   srtfRT   },
        };

        JPanel dataPanel = new JPanel(new GridLayout(rows.length, 4, 1, 1));
        dataPanel.setBackground(new Color(0xE2E8F0)); // gap colour
        for (int i = 0; i < rows.length; i++) {
            Color rowBg    = (i % 2 == 0) ? ODD_ROW : EVEN_ROW;
            String metric  = (String) rows[i][0];
            double rrVal   = (double)  rows[i][1];
            double srtfVal = (double)  rows[i][2];
            boolean rrWins = rrVal <= srtfVal;

            dataPanel.add(metricCell(metric, rowBg));
            dataPanel.add(valueCell(f(rrVal),   rrWins   ? WIN_BG : rowBg, RR_COLOR));
            dataPanel.add(valueCell(f(srtfVal), !rrWins  ? WIN_BG : rowBg, SRTF_COLOR));
            dataPanel.add(winnerCell(rrWins ? "RR" : "SRTF", rrWins ? RR_COLOR : SRTF_COLOR, rowBg));
        }
        wrapper.add(dataPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Auto-written conclusion ────────────────────────────────────────────────
    private JPanel buildConclusion() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(CONCLUSION_BG);
        panel.setBorder(new CompoundBorder(
            new LineBorder(CONCLUSION_BORDER, 1),
            new EmptyBorder(12, 14, 14, 14)
        ));
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel heading = new JLabel("📝  Conclusion");
        heading.setFont(new Font("SansSerif", Font.BOLD, 14));
        heading.setForeground(new Color(0x1E40AF));
        panel.add(heading, BorderLayout.NORTH);

        JTextArea text = new JTextArea(writeConclusion());
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(CONCLUSION_BG);
        text.setFont(new Font("SansSerif", Font.PLAIN, 13));
        text.setForeground(new Color(0x1E293B));
        text.setBorder(new EmptyBorder(6, 0, 0, 0));
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    // ── Generate conclusion text automatically from the numbers ────────────────
    private String writeConclusion() {
        StringBuilder sb = new StringBuilder();

        // Waiting Time
        if (rrWT < srtfWT) {
            sb.append("• Waiting Time: Round Robin produced a lower average waiting time (")
              .append(f(rrWT)).append(" vs ").append(f(srtfWT)).append(").\n");
        } else if (srtfWT < rrWT) {
            sb.append("• Waiting Time: SRTF produced a lower average waiting time (")
              .append(f(srtfWT)).append(" vs ").append(f(rrWT))
              .append("), which is expected — it always picks the shortest remaining job.\n");
        } else {
            sb.append("• Waiting Time: Both algorithms produced equal average waiting time (")
              .append(f(rrWT)).append(").\n");
        }

        // Turnaround Time
        if (rrTAT < srtfTAT) {
            sb.append("• Turnaround Time: Round Robin achieved a lower average turnaround time (")
              .append(f(rrTAT)).append(" vs ").append(f(srtfTAT)).append(").\n");
        } else if (srtfTAT < rrTAT) {
            sb.append("• Turnaround Time: SRTF achieved a lower average turnaround time (")
              .append(f(srtfTAT)).append(" vs ").append(f(rrTAT))
              .append("), meaning it completes processes more quickly on average.\n");
        } else {
            sb.append("• Turnaround Time: Both algorithms produced equal average turnaround time.\n");
        }

        // Response Time
        if (rrRT < srtfRT) {
            sb.append("• Response Time: Round Robin gave a better average response time (")
              .append(f(rrRT)).append(" vs ").append(f(srtfRT))
              .append("). This is typical — time-slicing ensures every process responds quickly.\n");
        } else if (srtfRT < rrRT) {
            sb.append("• Response Time: SRTF gave a better average response time (")
              .append(f(srtfRT)).append(" vs ").append(f(rrRT)).append(").\n");
        } else {
            sb.append("• Response Time: Both algorithms produced equal average response time.\n");
        }

        // Fairness
        sb.append("• Fairness: Round Robin distributes CPU time equally — no process is ever ignored. ")
          .append("SRTF may starve long processes if shorter jobs keep arriving continuously.\n");

        // Overall verdict
        int rrScore = 0, srtfScore = 0;
        if (rrWT  < srtfWT)  rrScore++; else if (srtfWT  < rrWT)  srtfScore++;
        if (rrTAT < srtfTAT) rrScore++; else if (srtfTAT < rrTAT) srtfScore++;
        if (rrRT  < srtfRT)  rrScore++; else if (srtfRT  < rrRT)  srtfScore++;

        sb.append("\n▶ Overall Recommendation: ");
        if (rrScore > srtfScore) {
            sb.append("Round Robin performed better on this workload. ")
              .append("It is recommended for interactive systems where fairness and fast response time matter most.");
        } else if (srtfScore > rrScore) {
            sb.append("SRTF performed better on this workload in terms of average metrics. ")
              .append("It is recommended when minimizing waiting and turnaround time is the priority, ")
              .append("though it carries a risk of starvation for longer processes.");
        } else {
            sb.append("Both algorithms performed equally on this workload. ")
              .append("Choose Round Robin for fairness, or SRTF for throughput efficiency.");
        }

        return sb.toString();
    }

    // ── Cell builders ──────────────────────────────────────────────────────────
    private JLabel headerCell(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(Color.WHITE);
        l.setBackground(HEADER_BG);
        l.setOpaque(true);
        l.setBorder(new EmptyBorder(9, 10, 9, 10));
        return l;
    }

    private JLabel metricCell(String text, Color bg) {
        JLabel l = new JLabel(text, SwingConstants.LEFT);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(new Color(0x374151));
        l.setBackground(bg);
        l.setOpaque(true);
        l.setBorder(new EmptyBorder(8, 12, 8, 8));
        return l;
    }

    private JLabel valueCell(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(fg);
        l.setBackground(bg);
        l.setOpaque(true);
        l.setBorder(new EmptyBorder(8, 8, 8, 8));
        return l;
    }

    private JLabel winnerCell(String text, Color fg, Color bg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(fg);
        l.setBackground(bg);
        l.setOpaque(true);
        l.setBorder(new EmptyBorder(8, 8, 8, 8));
        return l;
    }

    private String f(double v) { return String.format("%.2f", v); }
}
