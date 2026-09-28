import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Separate individual driver, following BookTest/SimpleTester.java.
 * Prints expected and actual results and fails the run if any check fails.
 * Run in a fresh JVM so the dummy singleton collections begin empty.
 */
public class WarehouseTest {
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        System.out.println("WAREHOUSE INDIVIDUAL TEST");
        System.out.println("Supporting classes are test doubles, not group implementations.");

        section("Empty collections and missing IDs");
        check("No clients initially", 0, collect(warehouse.getClients()).size());
        check("No products initially", 0, collect(warehouse.getProducts()).size());
        check("Missing client", null, warehouse.getClientByID("C99"));
        check("Missing product", null, warehouse.getProductByID("P99"));
        check("Missing client's wishlist", null, warehouse.viewClientWishList("C99"));
        check("Empty bulk insert", 0, warehouse.addProducts(
                Collections.<String>emptyList(), Collections.<Integer>emptyList(),
                Collections.<Double>emptyList()).size());

        section("Add clients and retrieve by ID");
        Client first = warehouse.addClient("Alex", "10 Main Street");
        Client second = warehouse.addClient("Sam", "20 Oak Street");
        check("First client's name", "Alex", first.getName());
        check("First client's address", "10 Main Street", first.getAddress());
        check("Second client's details", "Sam / 20 Oak Street",
                second.getName() + " / " + second.getAddress());
        check("Different clients have unique IDs", true,
                !first.getId().equals(second.getId()));
        same("First client lookup returns inserted object", first,
                warehouse.getClientByID(first.getId()));
        same("Second client lookup returns inserted object", second,
                warehouse.getClientByID(second.getId()));
        check("Client iteration includes both clients", Arrays.asList(first, second),
                collect(warehouse.getClients()));
        check("New client has empty wishlist", 0,
                warehouse.viewClientWishList(first.getId()).size());

        section("Add products in multiple batches");
        List<Product> batch = warehouse.addProducts(Arrays.asList("Notebook", "Pen"),
                Arrays.asList(10, 20), Arrays.asList(1.25, 2.50));
        check("Bulk insert returns two products", 2, batch.size());
        Product notebook = batch.get(0);
        Product pen = batch.get(1);
        check("Product names retain input order", "Notebook / Pen",
                notebook.getName() + " / " + pen.getName());
        check("First product stock", 10, notebook.getAmountInStock());
        check("First product price", 1.25, notebook.getSalePrice());
        check("Second product stock", 20, pen.getAmountInStock());
        check("Second product price", 2.50, pen.getSalePrice());
        same("Product lookup returns inserted object", notebook,
                warehouse.getProductByID(notebook.getId()));
        same("Second product lookup", pen, warehouse.getProductByID(pen.getId()));
        Product folder = warehouse.addProducts(Collections.singletonList("Folder"),
                Collections.singletonList(30), Collections.singletonList(3.75)).get(0);
        check("Second batch appends products", Arrays.asList(notebook, pen, folder),
                collect(warehouse.getProducts()));
        check("Product IDs remain unique across batches", true,
                !notebook.getId().equals(pen.getId())
                && !notebook.getId().equals(folder.getId())
                && !pen.getId().equals(folder.getId()));
        check("Second batch product fields", "Folder / 30 / 3.75",
                folder.getName() + " / " + folder.getAmountInStock()
                + " / " + folder.getSalePrice());
        same("Later product lookup", folder, warehouse.getProductByID(folder.getId()));
        batch.clear();
        check("Returned bulk list is separate from stored collection", 3,
                collect(warehouse.getProducts()).size());

        section("Wishlist insertion, replacement and client independence");
        WishListItem firstItem = warehouse.addProductToWishList(
                first.getId(), notebook.getId(), 5);
        same("Wishlist item references selected product", notebook, firstItem.getProduct());
        check("Requested quantity is forwarded", 5, firstItem.getQuantity());
        same("Returned item appears in client's wishlist", firstItem,
                warehouse.viewClientWishList(first.getId()).get(0));
        warehouse.addProductToWishList(first.getId(), pen.getId(), 2);
        check("Different products create two entries", 2,
                warehouse.viewClientWishList(first.getId()).size());
        warehouse.addProductToWishList(second.getId(), notebook.getId(), 7);
        WishListItem updated = warehouse.addProductToWishList(
                first.getId(), notebook.getId(), 3);
        same("Existing entry is updated", firstItem, updated);
        check("Quantity is replaced, not added", 3, updated.getQuantity());
        check("Replacement does not duplicate entry", 2,
                warehouse.viewClientWishList(first.getId()).size());
        check("Other product's quantity is unchanged", 2,
                warehouse.viewClientWishList(first.getId()).get(1).getQuantity());
        check("Other client's quantity is unchanged", 7,
                warehouse.viewClientWishList(second.getId()).get(0).getQuantity());
        check("Other client has its own wishlist", 1,
                warehouse.viewClientWishList(second.getId()).size());
        check("Wishlist does not deduct product stock", 10, notebook.getAmountInStock());

        section("Invalid lookups and wishlist requests");
        check("Unknown client lookup", null, warehouse.getClientByID("C99"));
        check("Unknown product lookup", null, warehouse.getProductByID("P99"));
        check("Null client ID", null, warehouse.getClientByID(null));
        check("Null product ID", null, warehouse.getProductByID(null));
        check("Unknown client's wishlist", null, warehouse.viewClientWishList("C99"));
        check("Null client's wishlist", null, warehouse.viewClientWishList(null));
        check("Wishlist request with unknown client", null,
                warehouse.addProductToWishList("C99", notebook.getId(), 1));
        check("Wishlist request with unknown product", null,
                warehouse.addProductToWishList(first.getId(), "P99", 1));
        check("Wishlist request with null client", null,
                warehouse.addProductToWishList(null, notebook.getId(), 1));
        check("Wishlist request with null product", null,
                warehouse.addProductToWishList(first.getId(), null, 1));
        check("Rejected requests preserve wishlist size", 2,
                warehouse.viewClientWishList(first.getId()).size());
        check("Rejected requests preserve quantity", 3, firstItem.getQuantity());

        section("Parallel-list input checks");
        rejects("Unequal amount list", new Runnable() {
            public void run() {
                warehouse.addProducts(Arrays.asList("A", "B"),
                        Arrays.asList(1), Arrays.asList(1.0, 2.0));
            }
        });
        rejects("Unequal price list", new Runnable() {
            public void run() {
                warehouse.addProducts(Arrays.asList("A"),
                        Arrays.asList(1), Arrays.asList(1.0, 2.0));
            }
        });
        rejects("Null names list", new Runnable() {
            public void run() {
                warehouse.addProducts(null, Arrays.asList(1), Arrays.asList(1.0));
            }
        });
        rejects("Null amounts list", new Runnable() {
            public void run() {
                warehouse.addProducts(Arrays.asList("A"), null, Arrays.asList(1.0));
            }
        });
        rejects("Null prices list", new Runnable() {
            public void run() {
                warehouse.addProducts(Arrays.asList("A"), Arrays.asList(1), null);
            }
        });
        check("Invalid batch shapes insert nothing", 3,
                collect(warehouse.getProducts()).size());
        warehouse.addProducts(Collections.<String>emptyList(),
                Collections.<Integer>emptyList(), Collections.<Double>emptyList());
        check("Empty batch preserves existing products", 3,
                collect(warehouse.getProducts()).size());

        section("Shared storage and final display");
        Warehouse otherFacade = new Warehouse();
        same("Another facade sees the same client", first,
                otherFacade.getClientByID(first.getId()));
        same("Another facade sees the same product", notebook,
                otherFacade.getProductByID(notebook.getId()));
        check("Final client count", 2, collect(warehouse.getClients()).size());
        check("Final product count", 3, collect(warehouse.getProducts()).size());
        for (Client client : collect(warehouse.getClients())) {
            System.out.println(client.getId() + " | " + client.getName()
                    + " | " + client.getAddress());
            for (WishListItem item : warehouse.viewClientWishList(client.getId())) {
                System.out.println("  " + item.getProduct().getName()
                        + " | wishlist quantity: " + item.getQuantity());
            }
        }
        for (Product product : collect(warehouse.getProducts())) {
            System.out.printf(java.util.Locale.US, "%s | %s | stock: %d | price: $%.2f%n",
                    product.getId(), product.getName(), product.getAmountInStock(),
                    product.getSalePrice());
        }

        System.out.println("\nRESULT: " + passed + " passed, " + failed + " failed.");
        if (failed != 0) {
            throw new AssertionError("Warehouse tests failed: " + failed);
        }
    }

    private static void section(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private static void check(String label, Object expected, Object actual) {
        boolean success = Objects.equals(expected, actual);
        if (success) { passed++; } else { failed++; }
        System.out.println((success ? "PASS " : "FAIL ") + label
                + " | expected: " + describe(expected) + " | actual: " + describe(actual));
    }

    private static void same(String label, Object expected, Object actual) {
        check(label + " (same reference)", true, expected == actual);
    }

    private static String describe(Object value) {
        if (value instanceof Client) { return ((Client) value).getId(); }
        if (value instanceof Product) { return ((Product) value).getId(); }
        if (value instanceof List<?>) {
            List<String> values = new ArrayList<String>();
            for (Object item : (List<?>) value) { values.add(describe(item)); }
            return values.toString();
        }
        return String.valueOf(value);
    }

    private static <T> List<T> collect(Iterator<T> iterator) {
        List<T> values = new ArrayList<T>();
        while (iterator.hasNext()) { values.add(iterator.next()); }
        return values;
    }

    private static void rejects(String label, Runnable operation) {
        try {
            operation.run();
            check(label, "IllegalArgumentException", "no exception");
        } catch (IllegalArgumentException expected) {
            check(label, "IllegalArgumentException", expected.getClass().getSimpleName());
        }
    }
}
