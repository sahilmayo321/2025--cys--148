import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Business-logic/controller class for the Online Shopping System.
 * Now supports user registration, login, and an expanded product catalog.
 */
public class ShoppingSystem {

    private final List<Product> products = new ArrayList<>();
    private final Cart cart = new Cart();
    private User currentUser;

    // Simulated user database: email -> User
    private final Map<String, User> registeredUsers = new HashMap<>();

    public ShoppingSystem() {
        loadSampleProducts();
    }

    // ─── User management ───────────────────────────────────────────────────────

    /** Register a new user (Signup). Throws if email already taken. */
    public void signupUser(String name, String email, String password, String phone, String address) {
        String key = email.trim().toLowerCase();
        if (registeredUsers.containsKey(key)) {
            throw new IllegalArgumentException("An account with this email already exists. Please log in.");
        }
        User user = new User(name, email, password, phone, address);
        registeredUsers.put(key, user);
        currentUser = user;
    }

    /** Authenticate an existing user (Login). Throws if credentials don't match. */
    public void loginUser(String email, String password) {
        String key = email.trim().toLowerCase();
        User user = registeredUsers.get(key);
        if (user == null) {
            throw new IllegalArgumentException("No account found with this email. Please sign up first.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password. Please try again.");
        }
        currentUser = user;
    }

    /** Quick guest entry (legacy – keeps backward compatibility). */
    public void registerUser(String name, String email) {
        currentUser = new User(name, email);
    }

    public void logoutUser() {
        currentUser = null;
        cart.clear();
    }

    // ─── Products ──────────────────────────────────────────────────────────────

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Cart getCart() { return cart; }

    public User getCurrentUser() { return currentUser; }

    public Product findProductById(int productId) {
        for (Product p : products) {
            if (p.getId() == productId) return p;
        }
        return null;
    }

    public List<String> getCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (Product p : products) categories.add(p.getCategory());
        return new ArrayList<>(categories);
    }

    // ─── Checkout ──────────────────────────────────────────────────────────────

    public Order checkout() {
        if (currentUser == null) throw new IllegalStateException("Enter customer information before checkout.");
        if (cart.isEmpty())      throw new IllegalStateException("Your cart is empty.");

        Order order = new Order(currentUser, cart.getItems());
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceStock(item.getQuantity());
        }
        cart.clear();
        return order;
    }

    // ─── Product catalog ───────────────────────────────────────────────────────

    private void loadSampleProducts() {

        // Electronics
        add(1,  "Gaming Laptop",         "Electronics",  145000, 5,
            "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400",
            "High-performance gaming laptop with RTX 3060, 16GB RAM, 512GB SSD", 175000);

        add(2,  "Wireless Mouse",         "Electronics",   2500, 15,
            "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400",
            "Ergonomic wireless mouse with long battery life, 2.4GHz connectivity", 3500);

        add(3,  "Mechanical Keyboard",    "Electronics",   7800, 10,
            "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400",
            "RGB mechanical keyboard with tactile switches, anti-ghosting", 9500);

        add(4,  "Noise-Cancel Headphones","Electronics",   6500, 12,
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400",
            "Over-ear headphones with ANC, 30hr battery, premium sound", 10000);

        add(5,  "Smart Watch",            "Electronics",  18500,  8,
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400",
            "Fitness tracker with heart rate monitor, GPS, AMOLED display", 25000);

        add(6,  "USB-C Hub 7-in-1",       "Electronics",   4200, 11,
            "https://images.unsplash.com/photo-1625895197185-efcec01cffe0?w=400",
            "7-port USB-C hub with HDMI 4K, USB 3.0, SD card reader, PD 100W", 6000);

        add(7,  "Bluetooth Speaker",      "Electronics",   3800, 14,
            "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400",
            "Portable waterproof speaker, 360° sound, 12hr battery", 5500);

        add(8,  "Tablet 10-inch",         "Electronics",  42000,  6,
            "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400",
            "10-inch FHD display tablet, 4GB RAM, 64GB storage, 6000mAh battery", 55000);

        add(9,  "Webcam HD 1080p",        "Electronics",   3200, 18,
            "https://images.unsplash.com/photo-1616499370260-485b3e5ed653?w=400",
            "Full HD webcam with built-in mic, plug-and-play, low light correction", 4500);

        add(10, "Power Bank 20000mAh",    "Electronics",   4500, 20,
            "https://images.unsplash.com/photo-1609592806596-b60d6e0d797d?w=400",
            "20000mAh power bank with PD 22.5W fast charging, dual USB-A + USB-C", 6500);

        // Fashion
        add(11, "Men's Casual T-Shirt",   "Fashion",        950, 50,
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400",
            "100% cotton round-neck T-shirt, available in multiple colors", 1500);

        add(12, "Women's Kurti",          "Fashion",       1800, 35,
            "https://images.unsplash.com/photo-1598554747436-c9293d6a588f?w=400",
            "Elegant printed kurti, breathable fabric, perfect for casual & formal", 2800);

        add(13, "Running Sneakers",       "Fashion",       5500, 22,
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400",
            "Lightweight running shoes with air cushion sole and mesh upper", 8000);

        add(14, "Leather Wallet",         "Fashion",       1200, 40,
            "https://images.unsplash.com/photo-1627123424574-724758594e93?w=400",
            "Genuine leather bi-fold wallet with RFID blocking, 6 card slots", 2000);

        add(15, "Sunglasses UV400",       "Fashion",        750, 30,
            "https://images.unsplash.com/photo-1577803645773-f96470509666?w=400",
            "Polarized UV400 sunglasses, anti-glare, stylish frame", 1500);

        // Home & Office
        add(16, "Desk Lamp LED",          "Home & Office", 2900, 14,
            "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400",
            "Touch-control LED desk lamp, 3 color modes, USB charging port", 4000);

        add(17, "Office Chair",           "Home & Office", 18500, 7,
            "https://images.unsplash.com/photo-1592078615290-033ee584e267?w=400",
            "Ergonomic mesh office chair with lumbar support and adjustable armrests", 28000);

        add(18, "Coffee Mug Set",         "Home & Office",  850, 25,
            "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=400",
            "Set of 2 ceramic mugs, microwave & dishwasher safe, 350ml", 1200);

        add(19, "Air Purifier",           "Home & Office", 12500, 9,
            "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=400",
            "HEPA air purifier removes 99.97% particles, covers 400 sq ft, ultra quiet", 18000);

        add(20, "Wall Clock",             "Home & Office",  1650, 20,
            "https://images.unsplash.com/photo-1563861826100-9cb868fdbe1c?w=400",
            "Silent quartz wall clock, 12-inch, wooden frame, battery operated", 2500);

        // Accessories
        add(21, "Travel Backpack",        "Accessories",   3200,  9,
            "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400",
            "30L water-resistant backpack with USB charging port, laptop compartment", 5000);

        add(22, "Stainless Water Bottle", "Accessories",   1200, 20,
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400",
            "500ml stainless steel insulated bottle, keeps hot 12hr / cold 24hr", 1800);

        add(23, "Phone Case iPhone 15",   "Accessories",    450, 60,
            "https://images.unsplash.com/photo-1601593346740-925612772716?w=400",
            "Shockproof TPU phone case with clear back, precise cutouts", 800);

        add(24, "Screen Protector Pack",  "Accessories",    250, 80,
            "https://images.unsplash.com/photo-1601597111158-2fceff292cdc?w=400",
            "Tempered glass screen protector 9H hardness, easy bubble-free install", 500);

        // Stationery
        add(25, "Premium Notebook A5",   "Stationery",     350, 30,
            "https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=400",
            "200-page A5 dotted notebook, hardcover, lay-flat binding", 600);

        add(26, "Ballpoint Pen Set",      "Stationery",     280, 50,
            "https://images.unsplash.com/photo-1585336261022-680e295ce3fe?w=400",
            "12-pack smooth-writing ballpoint pens, assorted colors", 500);

        add(27, "Sticky Notes Pack",      "Stationery",     180, 45,
            "https://images.unsplash.com/photo-1586281380349-632531db7ed4?w=400",
            "400-sheet sticky notes in 4 colors, 3×3 inch size", 350);

        // Health & Beauty
        add(28, "Face Wash SPF30",        "Health & Beauty", 890, 40,
            "https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=400",
            "Gentle foaming face wash with SPF30, for all skin types", 1400);

        add(29, "Yoga Mat",               "Health & Beauty",1950, 16,
            "https://images.unsplash.com/photo-1592432678016-e910b452f9a2?w=400",
            "6mm non-slip eco yoga mat with carrying strap, 183×61cm", 3000);

        add(30, "Protein Powder 1kg",     "Health & Beauty",3500, 12,
            "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400",
            "Whey protein concentrate, 24g protein per serving, chocolate flavour", 4800);
    }

    private void add(int id, String name, String cat, double price, int stock,
                     String imgUrl, String desc, double origPrice) {
        products.add(new Product(id, name, cat, price, stock, imgUrl, desc, origPrice));
    }
}
