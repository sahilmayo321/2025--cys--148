import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a completed order and generates its receipt.
 */
public class Order {
    private static int nextOrderNumber = 1001;

    private final int orderNumber;
    private final User customer;
    private final LocalDateTime orderTime;
    private final List<OrderItem> items;
    private final String paymentMethod;
    private final String status;

    public Order(User customer, List<CartItem> cartItems) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required before checkout.");
        }
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order from an empty cart.");
        }

        orderNumber = nextOrderNumber++;
        this.customer = customer;
        orderTime = LocalDateTime.now();
        paymentMethod = "Cash on Delivery";
        status = "Confirmed";

        items = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            items.add(new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()));
        }
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public User getCustomer() {
        return customer;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String formatReceipt() {
        StringBuilder receipt = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        receipt.append("============================================================\n");
        receipt.append("                    ONLINE SHOPPING SYSTEM\n");
        receipt.append("                         FINAL RECEIPT\n");
        receipt.append("============================================================\n");
        receipt.append(String.format("Order No. : %d%n", orderNumber));
        receipt.append(String.format("Date      : %s%n", orderTime.format(formatter)));
        receipt.append(String.format("Customer  : %s%n", customer.getName()));
        receipt.append(String.format("Email     : %s%n", customer.getEmail()));
        receipt.append("------------------------------------------------------------\n");
        receipt.append(String.format("%-4s %-23s %5s %10s %11s%n",
                "ID", "Product", "Qty", "Price", "Subtotal"));
        receipt.append("------------------------------------------------------------\n");

        for (OrderItem item : items) {
            receipt.append(String.format("%-4d %-23s %5d %10.2f %11.2f%n",
                    item.getProductId(),
                    shorten(item.getProductName(), 23),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getSubtotal()));
        }

        receipt.append("------------------------------------------------------------\n");
        receipt.append(String.format("TOTAL AMOUNT:%43.2f%n", getTotal()));
        receipt.append(String.format("Payment Method: %s%n", paymentMethod));
        receipt.append(String.format("Order Status  : %s%n", status));
        receipt.append("============================================================\n");
        receipt.append("              Thank you for shopping with us!\n");

        return receipt.toString();
    }

    private String shorten(String text, int maximumLength) {
        if (text.length() <= maximumLength) {
            return text;
        }
        return text.substring(0, maximumLength - 3) + "...";
    }
}
