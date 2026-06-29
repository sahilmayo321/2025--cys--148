import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

/**
 * Daraz / Alibaba-style Online Shopping System GUI.
 * Features: Login, Signup, product grid with images, cart, checkout.
 */
public class ShoppingSystemGUI extends JFrame {

    // ── Brand colors (Daraz orange palette) ──────────────────────────────────
    private static final Color BRAND_ORANGE   = new Color(255, 94, 0);
    private static final Color BRAND_DARK     = new Color(230, 70, 0);
    private static final Color BRAND_LIGHT    = new Color(255, 245, 240);
    private static final Color HEADER_BG      = new Color(240, 50, 0);      // deep red-orange
    private static final Color SECONDARY_BG   = new Color(249, 249, 249);
    private static final Color WHITE          = Color.WHITE;
    private static final Color TEXT_DARK      = new Color(17, 17, 17);
    private static final Color TEXT_GREY      = new Color(119, 119, 119);
    private static final Color TEXT_LIGHT     = new Color(180, 180, 180);
    private static final Color PRICE_RED      = new Color(220, 30, 30);
    private static final Color SUCCESS_GREEN  = new Color(0, 160, 80);
    private static final Color BORDER_COLOR   = new Color(225, 225, 225);
    private static final Color STAR_YELLOW    = new Color(255, 193, 7);
    private static final Color CARD_HOVER     = new Color(250, 248, 246);
    private static final Color BADGE_BG       = new Color(255, 50, 50);
    private static final Color NAV_ORANGE     = new Color(255, 94, 0);

    // ── Fonts ────────────────────────────────────────────────────────────────
    private static final Font F_HEADER    = new Font("SansSerif", Font.BOLD, 22);
    private static final Font F_NAV       = new Font("SansSerif", Font.BOLD, 14);
    private static final Font F_BODY      = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font F_BODY_B    = new Font("SansSerif", Font.BOLD, 13);
    private static final Font F_SMALL     = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font F_PRICE     = new Font("SansSerif", Font.BOLD, 17);
    private static final Font F_TITLE_LG  = new Font("SansSerif", Font.BOLD, 28);

    private final ShoppingSystem system     = new ShoppingSystem();
    private final NumberFormat currency     = NumberFormat.getNumberInstance(new Locale("en", "PK"));
    private final ExecutorService imgLoader = Executors.newFixedThreadPool(4);
    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    // ── Root card layout ─────────────────────────────────────────────────────
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel      = new JPanel(rootLayout);

    // ── Shared UI refs ───────────────────────────────────────────────────────
    private JLabel customerLabel;
    private JLabel cartBadgeLabel;
    private JLabel statusLabel;
    private JLabel totalItemsLabel;
    private JLabel totalPriceLabel;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JPanel productGridPanel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JTabbedPane tabs;
    private JPanel cartBadgePanel;

    // ── Login / Signup fields ────────────────────────────────────────────────
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JTextField signupNameField;
    private JTextField signupEmailField;
    private JPasswordField signupPasswordField;
    private JPasswordField signupConfirmField;
    private JTextField signupPhoneField;
    private JTextField signupAddressField;

    public ShoppingSystemGUI() {
        currency.setMinimumFractionDigits(0);
        currency.setMaximumFractionDigits(0);

        setTitle("ShopEasy — Online Shopping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 820);
        setLocationRelativeTo(null);

        rootPanel.add(createLandingPanel(),   "LANDING");
        rootPanel.add(createLoginPanel(),     "LOGIN");
        rootPanel.add(createSignupPanel(),    "SIGNUP");
        rootPanel.add(createShoppingPanel(),  "SHOP");
        setContentPane(rootPanel);

        setJMenuBar(createMenuBar());
    }

    // =========================================================================
    //  LANDING PAGE
    // =========================================================================
    private JPanel createLandingPanel() {
        JPanel bg = new JPanel(new BorderLayout());
        bg.setBackground(SECONDARY_BG);
        bg.add(createTopBanner(), BorderLayout.NORTH);

        // hero strip
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(new Color(255, 94, 0));
        hero.setPreferredSize(new Dimension(0, 180));
        JLabel heroTitle = new JLabel("<html><div style='color:#fff;font-size:26px;font-weight:bold;'>Welcome to ShopEasy</div>"
                + "<div style='color:#ffe0cc;font-size:15px;'>Pakistan's Favourite Online Store</div></html>");
        hero.add(heroTitle);
        bg.add(hero, BorderLayout.CENTER);

        // card row
        JPanel cardRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        cardRow.setBackground(SECONDARY_BG);
        cardRow.add(makeInfoCard("🛍️ Shop", "Thousands of products across every category.", "BROWSE PRODUCTS", "SHOP"));
        cardRow.add(makeInfoCard("🔐 Login", "Already have an account? Sign in to continue.", "LOGIN NOW", "LOGIN"));
        cardRow.add(makeInfoCard("✨ Sign Up", "New customer? Create your free account today.", "CREATE ACCOUNT", "SIGNUP"));
        bg.add(cardRow, BorderLayout.SOUTH);
        return bg;
    }

    private JPanel makeInfoCard(String icon, String text, String btnText, String target) {
        JPanel card = new RoundPanel(16, WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));
        card.setPreferredSize(new Dimension(280, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)));

        JLabel ico = new JLabel(icon, SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("<html><div style='text-align:center;width:200px'>" + text + "</div></html>");
        desc.setFont(F_BODY);
        desc.setForeground(TEXT_GREY);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = makeOrangeButton(btnText);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            if ("SHOP".equals(target)) {
                system.registerUser("Guest", "guest@shopeasy.pk");
                updateShopHeader();
                rootLayout.show(rootPanel, "SHOP");
                refreshProductGrid();
            } else {
                rootLayout.show(rootPanel, target);
            }
        });

        card.add(ico);
        card.add(Box.createVerticalStrut(10));
        card.add(desc);
        card.add(Box.createVerticalStrut(16));
        card.add(btn);
        return card;
    }

    // =========================================================================
    //  TOP BANNER (shared across login/signup/shop pages)
    // =========================================================================
    private JPanel createTopBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(HEADER_BG);
        banner.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Logo
        JLabel logo = new JLabel("ShopEasy");
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setForeground(WHITE);
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { rootLayout.show(rootPanel, "LANDING"); }
        });

        JPanel leftP = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftP.setOpaque(false);
        leftP.add(logo);

        JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightP.setOpaque(false);
        JButton loginBtn  = makeOutlineWhiteButton("Login");
        JButton signupBtn = makeOutlineWhiteButton("Sign Up");
        loginBtn.addActionListener(e  -> rootLayout.show(rootPanel, "LOGIN"));
        signupBtn.addActionListener(e -> rootLayout.show(rootPanel, "SIGNUP"));
        rightP.add(loginBtn);
        rightP.add(signupBtn);

        banner.add(leftP,  BorderLayout.WEST);
        banner.add(rightP, BorderLayout.EAST);
        return banner;
    }

    // =========================================================================
    //  LOGIN PAGE
    // =========================================================================
    private JPanel createLoginPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(SECONDARY_BG);
        root.add(createTopBanner(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(SECONDARY_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(36, 44, 36, 44)));
        card.setPreferredSize(new Dimension(460, 440));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0, 0, 6, 0);

        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(F_TITLE_LG); title.setForeground(TEXT_DARK);
        card.add(title, c);

        c.gridy++; c.insets = new Insets(0, 0, 28, 0);
        JLabel sub = new JLabel("Login to your ShopEasy account", SwingConstants.CENTER);
        sub.setFont(F_BODY); sub.setForeground(TEXT_GREY);
        card.add(sub, c);

        c.gridwidth = 1; c.weightx = 1; c.insets = new Insets(8, 0, 8, 0);

        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        card.add(fieldLabel("Email Address"), c);
        c.gridy++;
        loginEmailField = styledField("your@email.com");
        card.add(loginEmailField, c);

        c.gridy++;
        card.add(fieldLabel("Password"), c);
        c.gridy++;
        loginPasswordField = new JPasswordField();
        styleTextField(loginPasswordField);
        card.add(loginPasswordField, c);

        c.gridy++; c.insets = new Insets(24, 0, 12, 0);
        JButton loginBtn = makeOrangeButton("LOGIN TO MY ACCOUNT");
        loginBtn.setPreferredSize(new Dimension(0, 46));
        loginBtn.addActionListener(this::performLogin);
        card.add(loginBtn, c);

        c.gridy++; c.insets = new Insets(4, 0, 0, 0);
        JLabel switchLabel = new JLabel("<html>Don't have an account? "
            + "<span style='color:#f50;'>Sign Up</span></html>", SwingConstants.CENTER);
        switchLabel.setFont(F_BODY);
        switchLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { rootLayout.show(rootPanel, "SIGNUP"); }
        });
        card.add(switchLabel, c);

        center.add(card);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =========================================================================
    //  SIGNUP PAGE
    // =========================================================================
    private JPanel createSignupPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(SECONDARY_BG);
        root.add(createTopBanner(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(SECONDARY_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(32, 44, 32, 44)));
        card.setPreferredSize(new Dimension(520, 640));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0, 0, 6, 0);

        JLabel title = new JLabel("Create Your Account", SwingConstants.CENTER);
        title.setFont(F_TITLE_LG); title.setForeground(TEXT_DARK);
        card.add(title, c);

        c.gridy++; c.insets = new Insets(0, 0, 22, 0);
        JLabel sub = new JLabel("Join millions of happy shoppers!", SwingConstants.CENTER);
        sub.setFont(F_BODY); sub.setForeground(TEXT_GREY);
        card.add(sub, c);

        // Two-column layout for fields
        c.gridwidth = 1; c.weightx = 1; c.insets = new Insets(7, 0, 7, 8);

        c.gridy++; c.gridx = 0; c.gridwidth = 2; c.insets = new Insets(7, 0, 7, 0);
        card.add(fieldLabel("Full Name *"), c);
        c.gridy++;
        signupNameField = styledField("Your full name");
        card.add(signupNameField, c);

        c.gridy++;
        card.add(fieldLabel("Email Address *"), c);
        c.gridy++;
        signupEmailField = styledField("your@email.com");
        card.add(signupEmailField, c);

        c.gridy++;
        card.add(fieldLabel("Phone Number"), c);
        c.gridy++;
        signupPhoneField = styledField("03xxxxxxxxx");
        card.add(signupPhoneField, c);

        c.gridy++;
        card.add(fieldLabel("Password *"), c);
        c.gridy++;
        signupPasswordField = new JPasswordField();
        styleTextField(signupPasswordField);
        card.add(signupPasswordField, c);

        c.gridy++;
        card.add(fieldLabel("Confirm Password *"), c);
        c.gridy++;
        signupConfirmField = new JPasswordField();
        styleTextField(signupConfirmField);
        card.add(signupConfirmField, c);

        c.gridy++;
        card.add(fieldLabel("Delivery Address"), c);
        c.gridy++;
        signupAddressField = styledField("Street, City, Province");
        card.add(signupAddressField, c);

        c.gridy++; c.insets = new Insets(22, 0, 12, 0);
        JButton signupBtn = makeOrangeButton("CREATE MY ACCOUNT");
        signupBtn.setPreferredSize(new Dimension(0, 46));
        signupBtn.addActionListener(this::performSignup);
        card.add(signupBtn, c);

        c.gridy++; c.insets = new Insets(4, 0, 0, 0);
        JLabel switchLabel = new JLabel("<html>Already have an account? "
            + "<span style='color:#f50;'>Login</span></html>", SwingConstants.CENTER);
        switchLabel.setFont(F_BODY);
        switchLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { rootLayout.show(rootPanel, "LOGIN"); }
        });
        card.add(switchLabel, c);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.setBackground(SECONDARY_BG);
        center.add(scroll);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =========================================================================
    //  MAIN SHOPPING PANEL
    // =========================================================================
    private JPanel createShoppingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_BG);
        panel.add(createShopHeader(), BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(F_NAV);
        tabs.setBackground(SECONDARY_BG);
        tabs.addTab("🏠  Products", createProductsTab());
        tabs.addTab("🛒  Shopping Cart", createCartTab());
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) refreshCartTable();
        });
        panel.add(tabs, BorderLayout.CENTER);

        statusLabel = new JLabel("  ✓  Ready to shop");
        statusLabel.setFont(F_SMALL);
        statusLabel.setForeground(SUCCESS_GREEN);
        statusLabel.setBackground(new Color(240, 255, 240));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(new EmptyBorder(6, 14, 6, 14));
        panel.add(statusLabel, BorderLayout.SOUTH);
        return panel;
    }

    // ── Shop header (orange Daraz-style) ─────────────────────────────────────
    private JPanel createShopHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Logo
        JLabel logo = new JLabel("ShopEasy");
        logo.setFont(new Font("SansSerif", Font.BOLD, 26));
        logo.setForeground(WHITE);

        // Search bar
        JPanel searchBar = new JPanel(new BorderLayout(0, 0));
        searchBar.setBackground(WHITE);
        searchBar.setBorder(BorderFactory.createLineBorder(WHITE, 1));
        searchField = new JTextField();
        searchField.setFont(F_BODY);
        searchField.setBorder(new EmptyBorder(8, 14, 8, 14));
        searchField.setForeground(TEXT_DARK);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshProductGrid(); }
            public void removeUpdate(DocumentEvent e) { refreshProductGrid(); }
            public void changedUpdate(DocumentEvent e) { refreshProductGrid(); }
        });
        JButton searchBtn = new JButton("🔍");
        searchBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchBtn.setBackground(BRAND_ORANGE);
        searchBtn.setForeground(WHITE);
        searchBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchBtn, BorderLayout.EAST);

        // Right: user info + cart
        customerLabel = new JLabel("👤 Guest");
        customerLabel.setForeground(WHITE);
        customerLabel.setFont(F_NAV);

        cartBadgeLabel = new JLabel("🛒  Cart (0)");
        cartBadgeLabel.setForeground(WHITE);
        cartBadgeLabel.setFont(F_NAV);
        cartBadgeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 150), 1),
            new EmptyBorder(6, 12, 6, 12)));
        cartBadgeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cartBadgeLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (tabs != null) tabs.setSelectedIndex(1);
            }
        });

        JButton logoutBtn = makeSmallWhiteButton("Logout");
        logoutBtn.addActionListener(e -> {
            system.logoutUser();
            rootLayout.show(rootPanel, "LANDING");
        });

        JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        rightP.setOpaque(false);
        rightP.add(customerLabel);
        rightP.add(cartBadgeLabel);
        rightP.add(logoutBtn);

        header.add(logo,      BorderLayout.WEST);
        header.add(searchBar, BorderLayout.CENTER);
        header.add(rightP,    BorderLayout.EAST);
        return header;
    }

    // ── Products tab ─────────────────────────────────────────────────────────
    private JPanel createProductsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(SECONDARY_BG);
        panel.setBorder(new EmptyBorder(12, 14, 12, 14));

        // Category filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        filterBar.setBackground(WHITE);
        filterBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(6, 10, 6, 10)));

        JLabel filterLabel = new JLabel("Category:");
        filterLabel.setFont(F_BODY_B);
        filterBar.add(filterLabel);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        for (String cat : system.getCategories()) categoryComboBox.addItem(cat);
        categoryComboBox.setFont(F_BODY);
        categoryComboBox.setPreferredSize(new Dimension(200, 34));
        categoryComboBox.addActionListener(e -> refreshProductGrid());
        filterBar.add(categoryComboBox);

        JLabel sortLabel = new JLabel("  Sort:");
        sortLabel.setFont(F_BODY_B);
        filterBar.add(sortLabel);
        JComboBox<String> sortBox = new JComboBox<>(new String[]{
            "Default", "Price: Low to High", "Price: High to Low"});
        sortBox.setFont(F_BODY);
        sortBox.setPreferredSize(new Dimension(180, 34));
        filterBar.add(sortBox);
        panel.add(filterBar, BorderLayout.NORTH);

        // Product grid
        productGridPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));
        productGridPanel.setBackground(SECONDARY_BG);
        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(SECONDARY_BG);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ── Cart tab ─────────────────────────────────────────────────────────────
    private JPanel createCartTab() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(SECONDARY_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        cartTableModel = new DefaultTableModel(
            new Object[]{"ID", "Product", "Qty", "Unit Price (Rs.)", "Subtotal (Rs.)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        configureTable(cartTable);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        cartTable.getColumnModel().getColumn(2).setCellRenderer(new CenteredRenderer());
        cartTable.getColumnModel().getColumn(3).setCellRenderer(new RightAlignedRenderer());
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new RightAlignedRenderer());

        JScrollPane tableScroll = new JScrollPane(cartTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(tableScroll, BorderLayout.CENTER);

        // Right summary panel
        JPanel rightPanel = new JPanel(new BorderLayout(0, 14));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Summary card
        JPanel summaryCard = new JPanel();
        summaryCard.setLayout(new BoxLayout(summaryCard, BoxLayout.Y_AXIS));
        summaryCard.setBackground(WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)));

        JLabel sumTitle = new JLabel("ORDER SUMMARY");
        sumTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        sumTitle.setForeground(TEXT_DARK);
        sumTitle.setAlignmentX(LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER_COLOR);

        totalItemsLabel = new JLabel("Items: 0");
        totalItemsLabel.setFont(F_BODY);
        totalItemsLabel.setAlignmentX(LEFT_ALIGNMENT);

        totalPriceLabel = new JLabel("Total: Rs. 0");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalPriceLabel.setForeground(PRICE_RED);
        totalPriceLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel payLabel = new JLabel("💳  Cash on Delivery");
        payLabel.setFont(F_SMALL);
        payLabel.setForeground(SUCCESS_GREEN);
        payLabel.setAlignmentX(LEFT_ALIGNMENT);

        summaryCard.add(sumTitle);
        summaryCard.add(Box.createVerticalStrut(10));
        summaryCard.add(sep);
        summaryCard.add(Box.createVerticalStrut(12));
        summaryCard.add(totalItemsLabel);
        summaryCard.add(Box.createVerticalStrut(8));
        summaryCard.add(totalPriceLabel);
        summaryCard.add(Box.createVerticalStrut(8));
        summaryCard.add(payLabel);
        rightPanel.add(summaryCard, BorderLayout.NORTH);

        // Cart action buttons
        JPanel actionsPanel = new JPanel();
        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

        JPanel smallBtns = new JPanel(new GridLayout(2, 2, 8, 8));
        smallBtns.setOpaque(false);
        JButton incBtn  = makeColorBtn("+ Qty",      new Color(48, 109, 191));
        JButton decBtn  = makeColorBtn("- Qty",      new Color(111, 85, 148));
        JButton remBtn  = makeColorBtn("Remove",     new Color(191, 55, 55));
        JButton clrBtn  = makeColorBtn("Clear All",  TEXT_GREY);
        incBtn.addActionListener(e -> changeSelectedCartQty(1));
        decBtn.addActionListener(e -> changeSelectedCartQty(-1));
        remBtn.addActionListener(e -> removeSelectedCartItem());
        clrBtn.addActionListener(e -> clearCart());
        smallBtns.add(incBtn); smallBtns.add(decBtn);
        smallBtns.add(remBtn); smallBtns.add(clrBtn);

        JButton checkoutBtn = makeOrangeButton("✅  CHECKOUT NOW");
        checkoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        checkoutBtn.setAlignmentX(LEFT_ALIGNMENT);
        checkoutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        checkoutBtn.addActionListener(e -> checkout());

        actionsPanel.add(smallBtns);
        actionsPanel.add(Box.createVerticalStrut(12));
        actionsPanel.add(checkoutBtn);
        rightPanel.add(actionsPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // =========================================================================
    //  PRODUCT CARD (grid item)
    // =========================================================================
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        card.setPreferredSize(new Dimension(210, 320));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(CARD_HOVER);
                card.setBorder(BorderFactory.createLineBorder(BRAND_ORANGE, 1));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(WHITE);
                card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            }
        });

        // Product image
        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(210, 170));
        imgLabel.setBackground(new Color(248, 248, 248));
        imgLabel.setOpaque(true);
        imgLabel.setText("⏳ Loading...");
        imgLabel.setFont(F_SMALL);
        imgLabel.setForeground(TEXT_LIGHT);
        card.add(imgLabel, BorderLayout.NORTH);

        // Async image load
        String imgUrl = p.getImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            loadImageAsync(imgUrl, imgLabel, 210, 170);
        } else {
            imgLabel.setText(getCategoryEmoji(p.getCategory()));
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        }

        // Details
        JPanel details = new JPanel();
        details.setBackground(WHITE);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(new EmptyBorder(8, 10, 10, 10));

        JLabel nameLabel = new JLabel("<html><div style='width:180px'>" + p.getName() + "</div></html>");
        nameLabel.setFont(F_BODY_B);
        nameLabel.setForeground(TEXT_DARK);

        // Stars
        JLabel stars = new JLabel("★★★★☆  (128)");
        stars.setFont(F_SMALL);
        stars.setForeground(STAR_YELLOW);

        // Price row
        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        priceRow.setBackground(WHITE);
        JLabel priceLabel = new JLabel("Rs. " + currency.format(p.getPrice()));
        priceLabel.setFont(F_PRICE);
        priceLabel.setForeground(PRICE_RED);
        priceRow.add(priceLabel);
        if (p.getDiscountPercent() > 0) {
            JLabel origLabel = new JLabel("  Rs. " + currency.format(p.getOriginalPrice()));
            origLabel.setFont(F_SMALL);
            origLabel.setForeground(TEXT_LIGHT);
            origLabel.setText("<html><strike>" + origLabel.getText() + "</strike></html>");
            priceRow.add(origLabel);
        }

        // Discount badge
        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgeRow.setBackground(WHITE);
        if (p.getDiscountPercent() > 0) {
            JLabel discLabel = new JLabel(" -" + p.getDiscountPercent() + "% ");
            discLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
            discLabel.setForeground(WHITE);
            discLabel.setBackground(BADGE_BG);
            discLabel.setOpaque(true);
            discLabel.setBorder(new EmptyBorder(2, 4, 2, 4));
            badgeRow.add(discLabel);
        }
        if (p.getStock() <= 5 && p.getStock() > 0) {
            JLabel stockLabel = new JLabel("  Only " + p.getStock() + " left!");
            stockLabel.setFont(F_SMALL);
            stockLabel.setForeground(PRICE_RED);
            badgeRow.add(stockLabel);
        } else if (p.getStock() == 0) {
            JLabel oos = new JLabel("  OUT OF STOCK");
            oos.setFont(F_SMALL);
            oos.setForeground(TEXT_GREY);
            badgeRow.add(oos);
        }

        JButton addBtn = makeOrangeButton("Add to Cart");
        addBtn.setFont(F_SMALL);
        addBtn.setEnabled(p.getStock() > 0);
        if (p.getStock() == 0) {
            addBtn.setBackground(TEXT_LIGHT);
            addBtn.setBorder(BorderFactory.createLineBorder(TEXT_LIGHT));
        }
        addBtn.addActionListener(e -> addProductToCart(p, 1));

        details.add(nameLabel);
        details.add(Box.createVerticalStrut(4));
        details.add(stars);
        details.add(Box.createVerticalStrut(4));
        details.add(priceRow);
        details.add(badgeRow);
        details.add(Box.createVerticalStrut(8));
        details.add(addBtn);
        card.add(details, BorderLayout.CENTER);

        return card;
    }

    // =========================================================================
    //  IMAGE LOADING
    // =========================================================================
    private void loadImageAsync(String url, JLabel label, int w, int h) {
        if (imageCache.containsKey(url)) {
            label.setIcon(imageCache.get(url));
            label.setText("");
            return;
        }
        imgLoader.submit(() -> {
            try {
                BufferedImage img = ImageIO.read(new URL(url));
                if (img != null) {
                    Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaled);
                    imageCache.put(url, icon);
                    SwingUtilities.invokeLater(() -> {
                        label.setIcon(icon);
                        label.setText("");
                    });
                } else {
                    SwingUtilities.invokeLater(() -> setPlaceholder(label, w, h));
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> setPlaceholder(label, w, h));
            }
        });
    }

    private void setPlaceholder(JLabel label, int w, int h) {
        BufferedImage placeholder = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = placeholder.createGraphics();
        g2.setColor(new Color(245, 245, 245));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 40));
        FontMetrics fm = g2.getFontMetrics();
        String txt = "🛍";
        g2.drawString(txt, (w - fm.stringWidth(txt)) / 2, h / 2 + fm.getAscent() / 2);
        g2.dispose();
        label.setIcon(new ImageIcon(placeholder));
        label.setText("");
    }

    // =========================================================================
    //  LOGIN / SIGNUP ACTIONS
    // =========================================================================
    private void performLogin(ActionEvent e) {
        String email    = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();

        if (email.isEmpty()) { showWarn("Please enter your email address."); return; }
        if (password.isEmpty()) { showWarn("Please enter your password."); return; }

        try {
            system.loginUser(email, password);
            updateShopHeader();
            rootLayout.show(rootPanel, "SHOP");
            refreshProductGrid();
            setStatus("Welcome back, " + system.getCurrentUser().getName() + "!");
            loginPasswordField.setText("");
        } catch (IllegalArgumentException ex) {
            showWarn(ex.getMessage());
        }
    }

    private void performSignup(ActionEvent e) {
        String name     = signupNameField.getText().trim();
        String email    = signupEmailField.getText().trim();
        String phone    = signupPhoneField.getText().trim();
        String pass     = new String(signupPasswordField.getPassword()).trim();
        String confirm  = new String(signupConfirmField.getPassword()).trim();
        String address  = signupAddressField.getText().trim();

        if (name.length() < 2)  { showWarn("Please enter your full name."); return; }
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showWarn("Please enter a valid email address."); return; }
        if (pass.length() < 6)  { showWarn("Password must be at least 6 characters."); return; }
        if (!pass.equals(confirm)) { showWarn("Passwords do not match."); return; }

        try {
            system.signupUser(name, email, pass, phone, address);
            updateShopHeader();
            rootLayout.show(rootPanel, "SHOP");
            refreshProductGrid();
            setStatus("Account created! Welcome, " + name + "!");
            signupPasswordField.setText(""); signupConfirmField.setText("");
        } catch (IllegalArgumentException ex) {
            showWarn(ex.getMessage());
        }
    }

    private void updateShopHeader() {
        if (system.getCurrentUser() != null) {
            customerLabel.setText("👤  " + system.getCurrentUser().getName());
        }
        refreshCartBadge();
    }

    // =========================================================================
    //  CART ACTIONS
    // =========================================================================
    private void addProductToCart(Product p, int qty) {
        if (system.getCurrentUser() == null) {
            showWarn("Please login or sign up before adding to cart.");
            return;
        }
        if (system.getCart().addProduct(p, qty)) {
            refreshCartBadge();
            setStatus("✓  " + p.getName() + " added to cart.");
            JOptionPane.showMessageDialog(this,
                "<html><b>" + p.getName() + "</b><br>has been added to your cart!<br>"
                + "Cart total: <b>Rs. " + currency.format(system.getCart().calculateTotal()) + "</b></html>",
                "Added to Cart ✅", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int has = system.getCart().getQuantityOfProduct(p.getId());
            showWarn("Only " + Math.max(p.getStock() - has, 0) + " more unit(s) available.");
        }
    }

    private void changeSelectedCartQty(int delta) {
        int row = cartTable.getSelectedRow();
        if (row < 0) { showWarn("Select a cart item first."); return; }
        int pid  = (Integer) cartTableModel.getValueAt(row, 0);
        int cur  = system.getCart().getQuantityOfProduct(pid);
        int nq   = cur + delta;
        Product p = system.findProductById(pid);
        if (nq <= 0) {
            system.getCart().removeProduct(pid);
            setStatus("Item removed.");
        } else if (!system.getCart().updateQuantity(pid, nq)) {
            showWarn("Cannot exceed stock of " + (p == null ? 0 : p.getStock()) + ".");
            return;
        } else setStatus("Quantity updated.");
        refreshCartTable();
    }

    private void removeSelectedCartItem() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { showWarn("Select a cart item first."); return; }
        int    pid  = (Integer) cartTableModel.getValueAt(row, 0);
        String name = (String)  cartTableModel.getValueAt(row, 1);
        if (JOptionPane.showConfirmDialog(this, "Remove \"" + name + "\" from cart?",
            "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            system.getCart().removeProduct(pid);
            refreshCartTable();
            setStatus(name + " removed.");
        }
    }

    private void clearCart() {
        if (system.getCart().isEmpty()) { showWarn("Cart is already empty."); return; }
        if (JOptionPane.showConfirmDialog(this, "Clear entire cart?",
            "Clear Cart", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            system.getCart().clear();
            refreshCartTable();
            setStatus("Cart cleared.");
        }
    }

    private void checkout() {
        if (system.getCurrentUser() == null) { showWarn("Please login first."); return; }
        if (system.getCart().isEmpty()) { showWarn("Your cart is empty."); return; }

        String msg = "<html><b>Customer:</b> " + system.getCurrentUser().getName()
            + "<br><b>Items:</b> "    + system.getCart().getTotalItems()
            + "<br><b>Total:</b> Rs. " + currency.format(system.getCart().calculateTotal())
            + "<br><b>Payment:</b> Cash on Delivery"
            + "<br><br>Confirm this order?</html>";

        if (JOptionPane.showConfirmDialog(this, msg, "Confirm Order",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            setStatus("Checkout cancelled."); return;
        }
        try {
            Order order = system.checkout();
            refreshProductGrid();
            refreshCartTable();
            setStatus("Order #" + order.getOrderNumber() + " placed successfully! 🎉");
            showReceiptDialog(order);
        } catch (Exception ex) {
            showWarn(ex.getMessage());
        }
    }

    private void showReceiptDialog(Order order) {
        JDialog dlg = new JDialog(this, "Order Receipt #" + order.getOrderNumber(), true);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

        JTextArea area = new JTextArea(order.formatReceipt());
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setBackground(new Color(250, 250, 247));
        area.setBorder(new EmptyBorder(14, 14, 14, 14));
        dlg.add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton copy  = makeColorBtn("Copy Receipt", new Color(48, 109, 191));
        JButton close = makeOrangeButton("Close");
        copy.addActionListener(e  -> { area.selectAll(); area.copy(); area.select(0,0); });
        close.addActionListener(e -> dlg.dispose());
        btns.add(copy); btns.add(close);
        dlg.add(btns, BorderLayout.SOUTH);
        dlg.setSize(720, 600);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    // =========================================================================
    //  REFRESH HELPERS
    // =========================================================================
    private void refreshProductGrid() {
        if (productGridPanel == null) return;
        productGridPanel.removeAll();

        String search  = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        String selCat  = categoryComboBox == null ? "All Categories"
                       : String.valueOf(categoryComboBox.getSelectedItem());

        int count = 0;
        for (Product p : system.getProducts()) {
            boolean ms = p.getName().toLowerCase().contains(search)
                      || p.getCategory().toLowerCase().contains(search)
                      || p.getDescription().toLowerCase().contains(search);
            boolean mc = "All Categories".equals(selCat) || p.getCategory().equals(selCat);
            if (ms && mc) {
                productGridPanel.add(createProductCard(p));
                count++;
            }
        }
        if (count == 0) {
            JLabel none = new JLabel("No products found for \"" + search + "\"", SwingConstants.CENTER);
            none.setFont(F_BODY);
            none.setForeground(TEXT_GREY);
            productGridPanel.add(none);
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private void refreshCartTable() {
        if (cartTableModel == null) return;
        cartTableModel.setRowCount(0);
        for (CartItem item : system.getCart().getItems()) {
            Product p = item.getProduct();
            cartTableModel.addRow(new Object[]{
                p.getId(), p.getName(), item.getQuantity(),
                "Rs. " + currency.format(p.getPrice()),
                "Rs. " + currency.format(item.getSubtotal())
            });
        }
        int items   = system.getCart().getTotalItems();
        double total = system.getCart().calculateTotal();
        if (totalItemsLabel != null) totalItemsLabel.setText("Items in cart: " + items);
        if (totalPriceLabel != null) totalPriceLabel.setText("Rs. " + currency.format(total));
        refreshCartBadge();
    }

    private void refreshCartBadge() {
        if (cartBadgeLabel != null) {
            int n = system.getCart().getTotalItems();
            cartBadgeLabel.setText("🛒  Cart (" + n + ")");
        }
    }

    // =========================================================================
    //  MENU BAR
    // =========================================================================
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(HEADER_BG);

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        file.add(exit);

        JMenu shop = new JMenu("Shop");
        JMenuItem products = new JMenuItem("Products");
        products.addActionListener(e -> { if (tabs != null) tabs.setSelectedIndex(0); });
        JMenuItem cart = new JMenuItem("Cart");
        cart.addActionListener(e -> { if (tabs != null) tabs.setSelectedIndex(1); });
        JMenuItem checkout = new JMenuItem("Checkout");
        checkout.addActionListener(e -> checkout());
        shop.add(products); shop.add(cart); shop.addSeparator(); shop.add(checkout);

        JMenu account = new JMenu("Account");
        JMenuItem login  = new JMenuItem("Login");
        JMenuItem signup = new JMenuItem("Sign Up");
        JMenuItem logout = new JMenuItem("Logout");
        login.addActionListener(e  -> rootLayout.show(rootPanel, "LOGIN"));
        signup.addActionListener(e -> rootLayout.show(rootPanel, "SIGNUP"));
        logout.addActionListener(e -> { system.logoutUser(); rootLayout.show(rootPanel, "LANDING"); });
        account.add(login); account.add(signup); account.addSeparator(); account.add(logout);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "ShopEasy — Online Shopping System\nJava Swing GUI · Daraz-style UI\n\nv2.0 Enhanced Edition",
            "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file); bar.add(shop); bar.add(account); bar.add(help);
        return bar;
    }

    // =========================================================================
    //  UTILITY METHODS
    // =========================================================================
    private void configureTable(JTable t) {
        t.setRowHeight(38);
        t.setFont(F_BODY);
        t.setSelectionBackground(new Color(255, 237, 224));
        t.setSelectionForeground(TEXT_DARK);
        t.setGridColor(BORDER_COLOR);
        t.setShowVerticalLines(false);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JTableHeader h = t.getTableHeader();
        h.setFont(F_BODY_B);
        h.setBackground(BRAND_LIGHT);
        h.setForeground(TEXT_DARK);
        h.setPreferredSize(new Dimension(h.getWidth(), 40));
        h.setReorderingAllowed(false);
    }

    private JButton makeOrangeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BODY_B);
        b.setForeground(WHITE);
        b.setBackground(BRAND_ORANGE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BRAND_DARK),
            new EmptyBorder(9, 18, 9, 18)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(BRAND_DARK); }
            public void mouseExited(MouseEvent e)  { b.setBackground(BRAND_ORANGE); }
        });
        return b;
    }

    private JButton makeColorBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(F_BODY_B);
        b.setForeground(WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker()),
            new EmptyBorder(8, 12, 8, 12)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeOutlineWhiteButton(String text) {
        JButton b = new JButton(text);
        b.setFont(F_SMALL);
        b.setForeground(WHITE);
        b.setBackground(HEADER_BG);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            new EmptyBorder(5, 10, 5, 10)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeSmallWhiteButton(String text) {
        JButton b = new JButton(text);
        b.setFont(F_SMALL);
        b.setForeground(HEADER_BG);
        b.setBackground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WHITE, 1),
            new EmptyBorder(4, 8, 4, 8)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTextField(JTextField f) {
        f.setFont(F_BODY);
        f.setPreferredSize(new Dimension(350, 42));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(8, 12, 8, 12)));
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        styleTextField(f);
        f.setForeground(TEXT_GREY);
        f.setText(placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_DARK); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_GREY); }
            }
        });
        return f;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_BODY_B);
        l.setForeground(TEXT_DARK);
        return l;
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void setStatus(String msg) {
        if (statusLabel != null) statusLabel.setText("  " + msg);
    }

    private String getCategoryEmoji(String cat) {
        switch (cat) {
            case "Electronics":    return "💻";
            case "Fashion":        return "👗";
            case "Home & Office":  return "🏠";
            case "Accessories":    return "🎒";
            case "Stationery":     return "📝";
            case "Health & Beauty":return "💊";
            default:               return "🛍️";
        }
    }

    // ── Inner classes ─────────────────────────────────────────────────────────
    private static class RightAlignedRenderer extends DefaultTableCellRenderer {
        RightAlignedRenderer() { setHorizontalAlignment(SwingConstants.RIGHT); }
    }
    private static class CenteredRenderer extends DefaultTableCellRenderer {
        CenteredRenderer() { setHorizontalAlignment(SwingConstants.CENTER); }
    }
    private static class RoundPanel extends JPanel {
        private final int arc;
        RoundPanel(int arc, Color bg) {
            this.arc = arc;
            setBackground(bg);
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * FlowLayout that wraps children to the next row (like CSS flex-wrap).
     */
    private static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }
        private Dimension layoutSize(Container target, boolean pref) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets ins = target.getInsets();
                int maxWidth = targetWidth - ins.left - ins.right - hgap * 2;
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component c = target.getComponent(i);
                    if (c.isVisible()) {
                        Dimension d = pref ? c.getPreferredSize() : c.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.width = Math.max(dim.width, rowWidth);
                            dim.height += rowHeight + vgap;
                            rowWidth = 0; rowHeight = 0;
                        }
                        if (rowWidth != 0) rowWidth += hgap;
                        rowWidth  += d.width;
                        rowHeight  = Math.max(rowHeight, d.height);
                    }
                }
                dim.width  = Math.max(dim.width, rowWidth);
                dim.height += rowHeight + ins.top + ins.bottom + vgap * 2;
                return dim;
            }
        }
    }
}
