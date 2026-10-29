class OutOfStockException extends Exception {
    int shortfall;
    OutOfStockException(int shortfall) {
        super("Out of stock. Shortfall: " + shortfall);
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}
class Warehouse {
    private int stock;
    Warehouse(int stock) {
        this.stock = stock;
    }
    void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException(
                "Quantity must be greater than 0."
            );
        }
        if (qty > stock) {
            int shortfall = qty - stock;
            throw new OutOfStockException(shortfall);
        }
        stock -= qty;
        System.out.println(
            "Issued " + qty + " units of " + item
        );
        System.out.println("Remaining stock: " + stock);
    }
}
public class StockIssue {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(50);
        String[] items = {
            "Laptop",
            "Mouse",
            "Keyboard",
            "Monitor",
            "Printer"
        };
        int[] quantities = {
            20,
            40,
            0,
            60,
            10
        };
        for (int i = 0; i < items.length; i++) {
            System.out.println("\nRequest: "
                    + items[i] + " - " + quantities[i]);
            try {
                warehouse.issue(items[i], quantities[i]);
            } catch (InvalidQuantityException e) {
                System.out.println(
                    "Invalid Quantity: " + e.getMessage()
                );
            } catch (OutOfStockException e) {
                System.out.println(
                    "Out of Stock: " + e.getMessage()
                );
                System.out.println(
                    "Additional units needed: "
                    + e.shortfall
                );
            }
        }
        System.out.println("\nAll requests processed.");
    }
}
