package GUI;

import Model.GanttEntry;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedHashSet;

/**
 * Person 7 — GanttChartPanel.java
 *
 * Draws a Gantt chart for either SRTF or Round Robin.
 * Uses Model.GanttEntry exactly as teammates defined it.
 *
 * ── HOW PERSON 6 USES THIS ──────────────────────────────────────────────────
 *
 *   // After running the schedulers:
 *   List<GanttEntry> srtfGantt = srtfScheduler.getGanttData();
 *   List<GanttEntry> rrGantt   = roundRobin.getGanttData();
 *
 *   GanttChartPanel srtfChart = new GanttChartPanel(srtfGantt, "SRTF");
 *   GanttChartPanel rrChart   = new GanttChartPanel(rrGantt,   "Round Robin");
 *
 *   // Wrap in scroll pane so it scrolls horizontally for long timelines:
 *   JScrollPane srtfScroll = new JScrollPane(srtfChart,
 *       JScrollPane.VERTICAL_SCROLLBAR_NEVER,
 *       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 *
 *   // Optionally add a colour legend below:
 *   JPanel srtfLegend = GanttChartPanel.buildLegend(srtfGantt);
 *
 * ────────────────────────────────────────────────────────────────────────────
 */
public class GanttChartPanel extends JPanel {

    // ── Colour palette — one colour per process, cycles if > 8 processes ──────
    private static final Color[] PROCESS_COLORS = {
        new Color(0x3B82F6),  // blue
        new Color(0xEF4444),  // red
        new Color(0x10B981),  // green
        new Color(0xF59E0B),  // amber
        new Color(0x8B5CF6),  // purple
        new Color(0xEC4899),  // pink
        new Color(0x06B6D4),  // cyan
        new Color(0x84CC16),  // lime
    };
    private static final Color IDLE_COLOR   = new Color(0xD1D5DB);  // grey for idle
    private static final Color BG_COLOR     = new Color(0xF9FAFB);
    private static final Color BORDER_COLOR = new Color(0xE5E7EB);

    // ── Layout constants ───────────────────────────────────────────────────────
    private static final int BAR_HEIGHT    = 52;
    private static final int TOP_PAD       = 40;  // space for the title above bars
    private static final int BOTTOM_PAD    = 28;  // space for time labels below bars
    private static final int SIDE_PAD      = 14;
    private static final int MIN_BAR_WIDTH = 44;  // minimum pixels per Gantt segment

    // ── Fields ─────────────────────────────────────────────────────────────────
    private final List<GanttEntry> ganttData;
    private final String           algorithmName;

    // ── Constructor ────────────────────────────────────────────────────────────
    public GanttChartPanel(List<GanttEntry> ganttData, String algorithmName) {
        this.ganttData     = ganttData;
        this.algorithmName = algorithmName;

        setBackground(BG_COLOR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));

        // preferred width grows with timeline length so bars never get squished
        int totalTime = getTotalTime();
        int prefW     = Math.max(650, totalTime * MIN_BAR_WIDTH + SIDE_PAD * 2);
        int prefH     = TOP_PAD + BAR_HEIGHT + BOTTOM_PAD + 8;
        setPreferredSize(new Dimension(prefW, prefH));
    }

    // ── Painting ───────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ganttData == null || ganttData.isEmpty()) {
            g.setFont(new Font("SansSerif", Font.ITALIC, 13));
            g.setColor(new Color(0x9CA3AF));
            g.drawString("No data — run the simulation first.", SIDE_PAD, TOP_PAD + BAR_HEIGHT / 2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int    totalTime = getTotalTime();
        int    drawW     = getWidth() - SIDE_PAD * 2;
        double scale     = (double) drawW / totalTime;  // pixels per 1 time unit

        // ── Title ──────────────────────────────────────────────────────────────
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(new Color(0x111827));
        g2.drawString(algorithmName + "  —  Gantt Chart", SIDE_PAD, 26);

        // ── Bars ───────────────────────────────────────────────────────────────
        for (GanttEntry entry : ganttData) {
            int x = SIDE_PAD + (int)(entry.getStartTime() * scale);
            int w = Math.max(2, (int)((entry.getEndTime() - entry.getStartTime()) * scale));
            int y = TOP_PAD;

            // Coloured bar
            g2.setColor(pickColor(entry.getProcessLabel()));
            g2.fillRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

            // Subtle border
            g2.setColor(new Color(0, 0, 0, 40));
            g2.drawRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

            // Process label inside bar (only if bar is wide enough)
            if (w > 22) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                String lbl     = entry.getProcessLabel();
                int lx = x + (w - fm.stringWidth(lbl)) / 2;
                int ly = y + (BAR_HEIGHT + fm.getAscent()) / 2 - 3;
                // clamp so text never overflows the bar edge
                lx = Math.max(x + 3, Math.min(lx, x + w - fm.stringWidth(lbl) - 3));
                g2.drawString(lbl, lx, ly);
            }
        }

        // ── Time labels + tick marks below bars ────────────────────────────────
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();

        // draw time = 0
        g2.setColor(new Color(0x374151));
        g2.drawString("0", SIDE_PAD, TOP_PAD + BAR_HEIGHT + 18);
        g2.setColor(new Color(0x9CA3AF));
        g2.drawLine(SIDE_PAD, TOP_PAD + BAR_HEIGHT, SIDE_PAD, TOP_PAD + BAR_HEIGHT + 6);

        // draw end time of each segment
        for (GanttEntry entry : ganttData) {
            int    x  = SIDE_PAD + (int)(entry.getEndTime() * scale);
            String t  = String.valueOf(entry.getEndTime());
            int    tw = fm.stringWidth(t);

            g2.setColor(new Color(0x374151));
            g2.drawString(t, x - tw / 2, TOP_PAD + BAR_HEIGHT + 18);

            g2.setColor(new Color(0x9CA3AF));
            g2.drawLine(x, TOP_PAD + BAR_HEIGHT, x, TOP_PAD + BAR_HEIGHT + 6);
        }
    }

    // ── Colour picker ──────────────────────────────────────────────────────────
    /** Idle → grey. Everything else gets a stable colour from the palette. */
    private Color pickColor(String label) {
        if (label == null || label.equalsIgnoreCase("Idle")) return IDLE_COLOR;
        int idx = Math.abs(label.hashCode()) % PROCESS_COLORS.length;
        return PROCESS_COLORS[idx];
    }

    private int getTotalTime() {
        if (ganttData == null || ganttData.isEmpty()) return 1;
        return ganttData.get(ganttData.size() - 1).getEndTime();
    }

    // ── Static legend builder ──────────────────────────────────────────────────
    /**
     * Builds a small colour legend panel.
     * Person 6 can add this directly below the chart panel.
     *
     * Usage:
     *   somePanel.add(GanttChartPanel.buildLegend(ganttData));
     */
    public static JPanel buildLegend(List<GanttEntry> ganttData) {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        legend.setBackground(new Color(0xF9FAFB));

        LinkedHashSet<String> seen = new LinkedHashSet<>();
        for (GanttEntry e : ganttData) {
            if (!e.getProcessLabel().equalsIgnoreCase("Idle"))
                seen.add(e.getProcessLabel());
        }

        for (String pid : seen) {
            int   idx   = Math.abs(pid.hashCode()) % PROCESS_COLORS.length;
            Color color = PROCESS_COLORS[idx];

            JPanel swatch = new JPanel();
            swatch.setPreferredSize(new Dimension(14, 14));
            swatch.setBackground(color);
            swatch.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 60), 1));

            JLabel lbl = new JLabel(pid);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(new Color(0x374151));

            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            item.setBackground(new Color(0xF9FAFB));
            item.add(swatch);
            item.add(lbl);
            legend.add(item);
        }
        return legend;
    }
}
