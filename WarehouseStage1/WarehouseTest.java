import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

public class WarehouseTest {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        System.out.println("WAREHOUSE TEST WALKTHROUGH");

        section("Create five clients");
        for (int i = 1; i <= 5; i++) {
            warehouse.addClient("Client " + i, "Address " + i);
        }
        printClients(warehouse);

        section("Create five products");
        warehouse.addProducts(Arrays.asList("Product 1", "Product 2", "Product 3",
                        "Product 4", "Product 5"),
                Arrays.asList(10, 20, 30, 40, 50),
                Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        printProducts(warehouse);

        section("C1 wishlist: 5 each of P1, P3, P5");
        addItems(warehouse, "C1", new String[] {"P1", "P3", "P5"}, 5);
        printWishlist(warehouse, "C1");

        section("C2 wishlist: 7 each of P1, P2, P4");
        addItems(warehouse, "C2", new String[] {"P1", "P2", "P4"}, 7);
        printWishlist(warehouse, "C2");

        section("C3 wishlist: 6 each of P1, P2, P5");
        addItems(warehouse, "C3", new String[] {"P1", "P2", "P5"}, 6);
        printWishlist(warehouse, "C3");

        section("Add P3 and P5 to C2's wishlist, quantity 7 each");
        addItems(warehouse, "C2", new String[] {"P3", "P5"}, 7);
        printWishlist(warehouse, "C2");

        section("C3 wishlist after C2 updates");
        printWishlist(warehouse, "C3");

    }

    private static void printClients(Warehouse warehouse) {
        System.out.printf("%-4s | %-12s | %-12s | %-12s%n",
            "ID", "Name", "Address", "Balance");
        for (Client client : collect(warehouse.getClients())) {
            System.out.printf(Locale.US, "%-4s | %-12s | %-12s | $%-11.2f%n",
                client.getId(), client.getName(), client.getAddress(), client.getBalance());
        }
    }

    private static void printProducts(Warehouse warehouse) {
        System.out.printf("%-4s | %-12s | %-8s | %-10s%n", "ID", "Name", "Quantity", "Unit price");
        for (Product product : collect(warehouse.getProducts())) {
            System.out.printf(Locale.US, "%-4s | %-12s | %-8d | $%-9.2f%n",
                    product.getId(), product.getName(), product.getAmountInStock(),
                    product.getSalePrice());
        }
    }

    private static void addItems(Warehouse warehouse, String clientId,
            String[] productIds, int quantity) {
        for (String productId : productIds) {
            warehouse.addProductToWishList(clientId, productId, quantity);
        }
    }

    private static void printWishlist(Warehouse warehouse, String clientId) {
        System.out.println(clientId + " wishlist");
        System.out.printf("%-4s | %-12s | %-8s%n", "ID", "Product", "Quantity");
        for (WishListItem item : warehouse.viewClientWishList(clientId)) {
            Product product = item.getProduct();
            System.out.printf("%-4s | %-12s | %-8d%n", product.getId(),
                    product.getName(), item.getQuantity());
        }
    }

    private static void section(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private static <T> List<T> collect(Iterator<T> iterator) {
        java.util.ArrayList<T> values = new java.util.ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        return values;
    }
}
