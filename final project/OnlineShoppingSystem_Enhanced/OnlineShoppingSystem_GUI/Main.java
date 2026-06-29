import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point of the GUI Online Shopping System.
 * Run only this file to start the complete application.
 */
public class Main {
    public static void main(String[] args) {
        setApplicationLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            ShoppingSystemGUI application = new ShoppingSystemGUI();
            application.setVisible(true);
        });
    }

    private static void setApplicationLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // The application can still run with Java's default look and feel.
        }
    }
}
