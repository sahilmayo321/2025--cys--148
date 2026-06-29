import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores and manages products selected by the customer.
 */
public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getQuantityOfProduct(int productId) {
        CartItem item = findItem(productId);
        return item == null ? 0 : item.getQuantity();
    }

    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        int newQuantity = getQuantityOfProduct(product.getId()) + quantity;
        if (newQuantity > product.getStock()) {
            return false;
        }

        CartItem existingItem = findItem(product.getId());
        if (existingItem == null) {
            items.add(new CartItem(product, quantity));
        } else {
            existingItem.increaseQuantity(quantity);
        }
        return true;
    }

    public boolean updateQuantity(int productId, int newQuantity) {
        CartItem item = findItem(productId);
        if (item == null) {
            return false;
        }

        if (newQuantity <= 0) {
            return items.remove(item);
        }

        if (newQuantity > item.getProduct().getStock()) {
            return false;
        }

        item.setQuantity(newQuantity);
        return true;
    }

    public boolean removeProduct(int productId) {
        CartItem item = findItem(productId);
        return item != null && items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItems() {
        int totalItems = 0;
        for (CartItem item : items) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    private CartItem findItem(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }
}
