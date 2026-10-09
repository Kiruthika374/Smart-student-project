import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * LoginFrame.java
 * Teacher Login — Clean Blue/White Swing Design
 * On success → opens StudentMarksPanel
 */
public class LoginFrame extends JFrame {

    // ── Palette ─────────────────────────────────────────────
    private static final Color BLUE_DARK   = new Color(29, 78, 216);
    private static final Color BLUE_MID    = new Color(59, 130, 246);
    private static final Color BLUE_LIGHT  = new Color(219, 234, 254);
    private static final Color WHITE       = Color.WHITE;
    private static final Color BG          = new Color(241, 245, 249);
    private static final Color TEXT        = new Color(15, 23, 42);
    private static final Color MUTED       = new Color(100, 116, 139);
    private static final Color BORDER_CLR  = new Color(203, 213, 225);
    private static final Color ERROR_CLR   = new Color(220, 38, 38);
    private static final Color SUCCESS_CLR = new Color(22, 163, 74);

    private JTextField     tfUsername;
    private JPasswordField tfPassword;
    private JLabel         lblError;
    private JButton        btnLogin;

    // Demo credentials
    private static final String VALID_USER = "teacher";
    private static final String VALID_PASS = "admin123";

    public LoginFrame() {
        setTitle("Teacher Login — Student Performance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

        add(buildCard(), new GridBagConstraints());
    }

    // ── Gradient background ──────────────────────────────────
    class BackgroundPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(239, 246, 255),
                0, getHeight(), new Color(219, 234, 254)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ── Card ─────────────────────────────────────────────────
    private JPanel buildCard() {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(380, 480));

        // Drop shadow effect via border
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            new EmptyBorder(36, 40, 36, 40)
        ));

        card.add(buildTop());
        card.add(Box.createVerticalStrut(28));
        card.add(buildForm());
        return card;
    }

    // ── Top icon + title ─────────────────────────────────────
    private JPanel buildTop() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        // Circle avatar icon
        JPanel icon = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillOval(0, 0, 64, 64);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
                FontMetrics fm = g2.getFontMetrics();
                String emoji = "🎓";
                g2.drawString(emoji, (64 - fm.stringWidth(emoji)) / 2, 44);
                g2.dispose();
            }
        };
        icon.setOpaque(false);
        icon.setPreferredSize(new Dimension(64, 64));
        icon.setMaximumSize(new Dimension(64, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your teacher account");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(icon);
        top.add(Box.createVerticalStrut(14));
        top.add(title);
        top.add(Box.createVerticalStrut(4));
        top.add(sub);
        return top;
    }

    // ── Form ─────────────────────────────────────────────────
    private JPanel buildForm() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // Username
        form.add(fieldLabel("Username"));
        form.add(Box.createVerticalStrut(5));
        tfUsername = new JTextField();
        styleField(tfUsername);
        form.add(tfUsername);
        form.add(Box.createVerticalStrut(16));

        // Password
        form.add(fieldLabel("Password"));
        form.add(Box.createVerticalStrut(5));
        tfPassword = new JPasswordField();
        styleField(tfPassword);
        form.add(passwordRow());
        form.add(Box.createVerticalStrut(6));

        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(ERROR_CLR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblError);
        form.add(Box.createVerticalStrut(18));

        // Login button
        btnLogin = buildLoginBtn();
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(18));

        // Hint
        JLabel hint = new JLabel("Demo → user: teacher  |  pass: admin123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(hint);

        return form;
    }

    private JPanel passwordRow() {
        tfPassword = new JPasswordField();
        styleField(tfPassword);

        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(tfPassword, BorderLayout.CENTER);

        JButton eye = new JButton("👁");
        eye.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        eye.setPreferredSize(new Dimension(42, 42));
        eye.setBackground(BLUE_LIGHT);
        eye.setForeground(BLUE_DARK);
        eye.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        eye.setFocusPainted(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.addActionListener(e -> {
            if (tfPassword.getEchoChar() == 0) {
                tfPassword.setEchoChar('•');
                eye.setText("👁");
            } else {
                tfPassword.setEchoChar((char) 0);
                eye.setText("🙈");
            }
        });
        row.add(eye, BorderLayout.EAST);
        return row;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(51, 65, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(TEXT);
        tf.setBackground(new Color(248, 250, 252));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBackground(WHITE);
                tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BLUE_MID, 2, true),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                tf.setBackground(new Color(248, 250, 252));
                tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_CLR, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private JButton buildLoginBtn() {
        JButton btn = new JButton("Sign In") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? BLUE_DARK.darker()
                        : getModel().isRollover() ? new Color(37, 99, 235)
                        : BLUE_DARK;
                GradientPaint gp = new GradientPaint(0, 0, c, 0, getHeight(), BLUE_MID);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> doLogin());

        // Enter key
        getRootPane().setDefaultButton(btn);
        return btn;
    }

    // ── Login Logic ──────────────────────────────────────────
    private void doLogin() {
        String user = tfUsername.getText().trim();
        String pass = new String(tfPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("⚠  Username and password are required.");
            return;
        }

        if (user.equals(VALID_USER) && pass.equals(VALID_PASS)) {
            lblError.setForeground(SUCCESS_CLR);
            lblError.setText("✔  Login successful! Opening...");
            btnLogin.setEnabled(false);

            Timer t = new Timer(900, e -> {
                dispose();
                new StudentMarksPanel().setVisible(true);
            });
            t.setRepeats(false);
            t.start();
        } else {
            lblError.setForeground(ERROR_CLR);
            lblError.setText("✖  Invalid username or password.");
            tfPassword.setText("");
            tfPassword.requestFocus();
        }
    }

    // ── Shadow border ────────────────────────────────────────
    static class ShadowBorder extends AbstractBorder {
        private static final int SHADOW = 6;
        public Insets getBorderInsets(Component c) {
            return new Insets(SHADOW, SHADOW, SHADOW, SHADOW);
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < SHADOW; i++) {
                float alpha = 0.06f - (i * 0.008f);
                g2.setColor(new Color(0, 0, 0, Math.max(0, (int)(alpha * 255))));
                g2.drawRoundRect(x + i, y + i, w - i * 2, h - i * 2, 20, 20);
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}