/**
 * Represents a product available in the shopping system.
 * Now supports imageUrl and description fields.
 */
public class Product {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private int stock;
    private final String imageUrl;
    private final String description;
    private final double originalPrice; // for showing discount

    public Product(int id, String name, String category, double price, int stock) {
        this(id, name, category, price, stock, "", "", price);
    }

    public Product(int id, String name, String category, double price, int stock,
                   String imageUrl, String description, double originalPrice) {
        if (id <= 0)                         throw new IllegalArgumentException("Product ID must be positive.");
        if (name == null || name.trim().isEmpty())     throw new IllegalArgumentException("Product name cannot be empty.");
        if (category == null || category.trim().isEmpty()) throw new IllegalArgumentException("Category cannot be empty.");
        if (price < 0)                       throw new IllegalArgumentException("Price cannot be negative.");
        if (stock < 0)                       throw new IllegalArgumentException("Stock cannot be negative.");

        this.id            = id;
        this.name          = name.trim();
        this.category      = category.trim();
        this.price         = price;
        this.stock         = stock;
        this.imageUrl      = (imageUrl == null) ? "" : imageUrl;
        this.description   = (description == null) ? "" : description;
        this.originalPrice = originalPrice > price ? originalPrice : price;
    }

    public int    getId()            { return id; }
    public String getName()          { return name; }
    public String getCategory()      { return category; }
    public double getPrice()         { return price; }
    public int    getStock()         { return stock; }
    public String getImageUrl()      { return imageUrl; }
    public String getDescription()   { return description; }
    public double getOriginalPrice() { return originalPrice; }

    public int getDiscountPercent() {
        if (originalPrice <= price) return 0;
        return (int) Math.round((1.0 - price / originalPrice) * 100);
    }

    public boolean hasEnoughStock(int quantity) {
        return quantity > 0 && stock >= quantity;
    }

    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("Not enough stock available for " + name + ".");
        }
        stock -= quantity;
    }
}
