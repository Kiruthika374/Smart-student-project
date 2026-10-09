import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * StudentMarksPanel.java
 * - Enter marks for 5 subjects
 * - Semester Pass/Fail: each subject >= 35 AND average >= 50
 * - Decision Tree: Excellent / Average / Poor
 */
public class StudentMarksPanel extends JFrame {

    private static final Color BLUE_DARK  = new Color(29, 78, 216);
    private static final Color BLUE_MID   = new Color(59, 130, 246);
    private static final Color BLUE_LIGHT = new Color(219, 234, 254);
    private static final Color WHITE      = Color.WHITE;
    private static final Color BG         = new Color(241, 245, 249);
    private static final Color TEXT       = new Color(15, 23, 42);
    private static final Color MUTED      = new Color(100, 116, 139);
    private static final Color BORDER_CLR = new Color(203, 213, 225);

    private static final Color GREEN_FG   = new Color(6, 95, 70);
    private static final Color GREEN_BG   = new Color(209, 250, 229);
    private static final Color ORANGE_FG  = new Color(120, 53, 15);
    private static final Color ORANGE_BG  = new Color(254, 243, 199);
    private static final Color RED_FG     = new Color(127, 29, 29);
    private static final Color RED_BG     = new Color(254, 226, 226);
    private static final Color FAIL_FG    = new Color(153, 27, 27);
    private static final Color FAIL_BG    = new Color(255, 237, 213);

    private JTextField tfName;
    private JTextField tfMaths, tfScience, tfEnglish, tfSocial, tfLanguage;
    private JPanel     resultPanel;
    private JLabel     lblSemester, lblPerformance, lblDetails, lblSubjectStatus;

    public StudentMarksPanel() {
        setTitle("Student Performance Prediction System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildHeader(),     BorderLayout.NORTH);
        add(buildFormScroll(), BorderLayout.CENTER);
        add(buildFooter(),     BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BLUE_DARK);
        h.setBorder(new EmptyBorder(18, 24, 0, 24));
        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        JLabel title = new JLabel("  Student Marks Entry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        title.setForeground(WHITE);
        JLabel sub = new JLabel("Enter subject marks to check semester result & performance level");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(186, 210, 255));
        text.add(title); text.add(sub);
        h.add(text, BorderLayout.CENTER);
        JPanel bar = new JPanel();
        bar.setBackground(BLUE_MID);
        bar.setPreferredSize(new Dimension(0, 3));
        h.add(bar, BorderLayout.SOUTH);
        return h;
    }

    private JScrollPane buildFormScroll() {
        JPanel form = new JPanel();
        form.setBackground(BG);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(22, 32, 12, 32));

        JPanel nameCard = card();
        nameCard.add(sectionTitle("  Student Details"));
        nameCard.add(Box.createVerticalStrut(10));
        tfName = styledField("Enter full name");
        nameCard.add(labeledRow("Student Name", tfName));
        form.add(nameCard);
        form.add(Box.createVerticalStrut(14));

        JPanel marksCard = card();
        marksCard.add(sectionTitle("  Subject Marks  (Pass mark per subject: 35 | Overall average: 50)"));
        marksCard.add(Box.createVerticalStrut(12));
        tfMaths    = styledField("0 – 100");
        tfScience  = styledField("0 – 100");
        tfEnglish  = styledField("0 – 100");
        tfSocial   = styledField("0 – 100");
        tfLanguage = styledField("0 – 100");
        marksCard.add(subjectRow("Mathematics",    tfMaths));
        marksCard.add(Box.createVerticalStrut(10));
        marksCard.add(subjectRow("Science",        tfScience));
        marksCard.add(Box.createVerticalStrut(10));
        marksCard.add(subjectRow("English",        tfEnglish));
        marksCard.add(Box.createVerticalStrut(10));
        marksCard.add(subjectRow("Social Science", tfSocial));
        marksCard.add(Box.createVerticalStrut(10));
        marksCard.add(subjectRow("Language",       tfLanguage));
        form.add(marksCard);
        form.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.add(primaryBtn("  Predict Result", e -> predict()));
        btnRow.add(secondaryBtn("  Clear", e -> clearForm()));
        form.add(btnRow);
        form.add(Box.createVerticalStrut(18));

        resultPanel = buildResultPanel();
        resultPanel.setVisible(false);
        form.add(resultPanel);
        form.add(Box.createVerticalStrut(16));

        return new JScrollPane(form,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private JPanel buildResultPanel() {
        JPanel p = card();
        JLabel heading = new JLabel("  Result Summary");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 13));
        heading.setForeground(MUTED);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(heading);
        p.add(Box.createVerticalStrut(12));

        lblSemester = badgeLabel();
        p.add(lblSemester);
        p.add(Box.createVerticalStrut(8));

        lblPerformance = badgeLabel();
        p.add(lblPerformance);
        p.add(Box.createVerticalStrut(14));

        lblSubjectStatus = new JLabel();
        lblSubjectStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubjectStatus.setForeground(TEXT);
        lblSubjectStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblSubjectStatus);
        p.add(Box.createVerticalStrut(10));

        lblDetails = new JLabel();
        lblDetails.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblDetails.setForeground(MUTED);
        lblDetails.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblDetails);
        return p;
    }

    private void predict() {
        String name = tfName.getText().trim();
        if (name.isEmpty()) { showError("Enter the student name."); return; }

        int m, sc, en, so, la;
        try {
            m  = parseMark(tfMaths,    "Mathematics");
            sc = parseMark(tfScience,  "Science");
            en = parseMark(tfEnglish,  "English");
            so = parseMark(tfSocial,   "Social Science");
            la = parseMark(tfLanguage, "Language");
        } catch (IllegalArgumentException ex) { showError(ex.getMessage()); return; }

        double avg = (m + sc + en + so + la) / 5.0;

        // ── Pass / Fail ──────────────────────────────────────
        boolean allPass      = (m >= 35 && sc >= 35 && en >= 35 && so >= 35 && la >= 35);
        boolean avgPass      = avg >= 50;
        boolean semesterPass = allPass && avgPass;

        // ── Decision Tree ────────────────────────────────────
        String perf      = DecisionTreePredictor.predict(m, sc, en, so, la);
        String dtExplain = DecisionTreePredictor.getExplanation(m, sc, en, so, la);

        // Semester badge
        if (semesterPass) {
            setBadge(lblSemester, "  SEMESTER : PASSED  ", GREEN_FG, GREEN_BG);
        } else {
            String why = !allPass ? "  (One or more subjects failed  < 35)"
                                  : "  (Overall average below 50%)";
            setBadge(lblSemester, "  SEMESTER : FAILED" + why + "  ", FAIL_FG, FAIL_BG);
        }

        // Performance badge
        switch (perf) {
            case DecisionTreePredictor.EXCELLENT:
                setBadge(lblPerformance, "  Performance : Excellent  ", GREEN_FG, GREEN_BG);  break;
            case DecisionTreePredictor.AVERAGE:
                setBadge(lblPerformance, "  Performance : Average  ",   ORANGE_FG, ORANGE_BG); break;
            default:
                setBadge(lblPerformance, "  Performance : Poor  ",      RED_FG, RED_BG);       break;
        }

        // Per-subject table
        int[] marks    = {m, sc, en, so, la};
        String[] subs  = {"Mathematics", "Science", "English", "Social Science", "Language"};
        StringBuilder sb = new StringBuilder("<html><table cellpadding='4'>");
        sb.append("<tr><td><b>Subject</b></td><td><b>Mark</b></td><td><b>Status</b></td></tr>");
        for (int i = 0; i < subs.length; i++) {
            String status = marks[i] >= 35
                ? "<font color='#065F46'><b>PASS</b></font>"
                : "<font color='#991B1B'><b>FAIL</b></font>";
            sb.append("<tr><td>").append(subs[i]).append("</td>")
              .append("<td align='center'>").append(marks[i]).append(" / 100</td>")
              .append("<td>").append(status).append("</td></tr>");
        }
        sb.append(String.format(
            "<tr><td><b>Average</b></td><td align='center'><b>%.1f%%</b></td><td>%s</td></tr>",
            avg, avgPass
                ? "<font color='#065F46'><b>OK</b></font>"
                : "<font color='#991B1B'><b>Below 50</b></font>"
        ));
        sb.append("</table></html>");
        lblSubjectStatus.setText(sb.toString());

        String htmlPath = "<html><pre style='font-size:10px'>"
            + dtExplain.replace("<","&lt;").replace(">","&gt;")
            + "</pre></html>";
        lblDetails.setText(htmlPath);

        resultPanel.setVisible(true);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void setBadge(JLabel lbl, String text, Color fg, Color bg) {
        lbl.setText(text);
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
    }

    private int parseMark(JTextField tf, String subject) {
        String v = tf.getText().trim();
        if (v.isEmpty()) throw new IllegalArgumentException("Enter mark for " + subject + ".");
        int mark;
        try { mark = Integer.parseInt(v); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(subject + ": must be a number."); }
        if (mark < 0 || mark > 100) throw new IllegalArgumentException(subject + ": must be 0–100.");
        return mark;
    }

    private void clearForm() {
        tfName.setText("");
        for (JTextField tf : new JTextField[]{tfMaths, tfScience, tfEnglish, tfSocial, tfLanguage})
            tf.setText("");
        resultPanel.setVisible(false);
        tfName.requestFocus();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setOpaque(true);
        p.setBackground(WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(16, 18, 16, 18)
        ));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(BLUE_DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel badgeLabel() {
        JLabel l = new JLabel("  —  ");
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setOpaque(true);
        l.setBackground(new Color(241, 245, 249));
        l.setForeground(MUTED);
        l.setBorder(new EmptyBorder(6, 12, 6, 12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String ph) {
        JTextField tf = new JTextField(20) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    ((Graphics2D)g).setColor(MUTED);
                    g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(ph, 10, getHeight() / 2 + 5);
                }
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT);
        tf.setBackground(new Color(248, 250, 252));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true), new EmptyBorder(7, 10, 7, 10)));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBackground(WHITE);
                tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BLUE_MID, 2, true), new EmptyBorder(6, 9, 6, 9)));
            }
            public void focusLost(FocusEvent e) {
                tf.setBackground(new Color(248, 250, 252));
                tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_CLR, 1, true), new EmptyBorder(7, 10, 7, 10)));
            }
        });
        return tf;
    }

    private JPanel labeledRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT);
        lbl.setPreferredSize(new Dimension(130, 30));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JPanel subjectRow(String subject, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel lbl = new JLabel(subject);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT);
        lbl.setPreferredSize(new Dimension(155, 30));
        field.setMaximumSize(new Dimension(120, 40));
        field.setPreferredSize(new Dimension(120, 36));
        JLabel outOf = new JLabel("/ 100   (Pass >= 35)");
        outOf.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        outOf.setForeground(MUTED);
        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.add(outOf, BorderLayout.EAST);
        return row;
    }

    private JButton primaryBtn(String text, ActionListener al) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BLUE_DARK.darker() : BLUE_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));
        btn.addActionListener(al);
        return btn;
    }

    private JButton secondaryBtn(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BLUE_DARK);
        btn.setBackground(BLUE_LIGHT);
        btn.setBorder(new LineBorder(new Color(147, 197, 253), 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 38));
        btn.addActionListener(al);
        return btn;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
        f.setBackground(BLUE_DARK);
        f.setBorder(new EmptyBorder(6, 20, 6, 20));
        JLabel l = new JLabel("Smart Student Performance Prediction System  |  Semester & Decision Tree Module");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(new Color(147, 181, 255));
        f.add(l);
        return f;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new StudentMarksPanel().setVisible(true));
    }
}