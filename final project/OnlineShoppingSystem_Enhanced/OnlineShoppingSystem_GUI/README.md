# ShopEasy — Online Shopping System (Enhanced Edition v2.0)

A feature-rich Java Swing desktop shopping application styled like **Daraz / Alibaba**,
with Login & Signup, product images, 30+ products, and a responsive card-grid UI.

## ✨ New Features (v2.0)

- **Login Page** — authenticate with email & password
- **Signup Page** — create a new account with name, email, password, phone, and address
- **Guest Mode** — browse without an account (click "Browse Products" on landing)
- **30 Products** across 6 categories (Electronics, Fashion, Home & Office, Accessories, Stationery, Health & Beauty)
- **Product images** loaded from the web (requires internet access)
- **Discount badges** and original price strike-through
- **Card grid layout** — responsive product cards like Daraz
- **Orange brand theme** matching Daraz/Alibaba style
- **Hover effects** on product cards
- **Star ratings** display
- **Category + search filter** in the header search bar
- **Logout** button in the header

## Project Files

| File | Purpose |
|---|---|
| `Main.java` | Entry point — run this to start the app |
| `ShoppingSystemGUI.java` | Complete Daraz-style GUI (Login, Signup, Shop) |
| `ShoppingSystem.java` | Business logic, user management, 30-product catalog |
| `Product.java` | Product data with imageUrl, description, discount |
| `User.java` | Customer with name, email, password, phone, address |
| `Cart.java` | Cart operations and totals |
| `CartItem.java` | Product + quantity |
| `Order.java` | Completed order and receipt |
| `OrderItem.java` | Immutable receipt line item |

## How to Run

### VS Code (recommended)
1. Extract the ZIP and open the `OnlineShoppingSystem_GUI` folder in VS Code
2. Install **Extension Pack for Java** if prompted
3. Open `Main.java` → click **Run** above `main()`

### Terminal
```bash
javac *.java
java Main
```

### Windows Batch
```
compile_and_run.bat
```

## Usage Flow

1. **Landing page** → choose Login, Sign Up, or Browse Products
2. **Sign Up** → enter your name, email, and password
3. **Shop** → browse the product card grid, filter by category or search
4. **Add to Cart** → click "Add to Cart" on any product card
5. **Cart tab** → review items, adjust quantities, proceed to checkout
6. **Receipt** → view, copy, and save your order receipt

## Notes

- Product images load from the internet (Unsplash). Offline mode falls back to emoji icons.
- Data is stored in memory and resets when the application closes.
- Payment method: Cash on Delivery.
