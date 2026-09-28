import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stage 1 Warehouse facade. Public operations follow the Warehouse class box
 * in CSCI 430_class.pdf. Collections and IDs belong to the list classes.
 */
public class Warehouse {
    public Client addClient(String name, String address) {
        return ClientList.instance().insertClient(name, address);
    }

    /**
     * Entries at the same index describe one product. The three lists must
     * be non-null and have equal lengths. Product validation belongs to
     * ProductList, as specified by the design.
     */
    public List<Product> addProducts(List<String> names,
            List<Integer> amounts, List<Double> prices) {
        if (names == null || amounts == null || prices == null
                || names.size() != amounts.size()
                || names.size() != prices.size()) {
            throw new IllegalArgumentException(
                    "Product lists must be non-null and have equal lengths.");
        }

        List<Product> addedProducts = new ArrayList<Product>();
        for (int i = 0; i < names.size(); i++) {
            addedProducts.add(ProductList.instance().insertProduct(
                    names.get(i), amounts.get(i), prices.get(i)));
        }
        return addedProducts;
    }

    public Client getClientByID(String clientID) {
        return ClientList.instance().search(clientID);
    }

    public Product getProductByID(String productID) {
        return ProductList.instance().search(productID);
    }

    public List<WishListItem> viewClientWishList(String clientID) {
        Client client = getClientByID(clientID);
        return client == null ? null : client.getWishList();
    }

    public WishListItem addProductToWishList(String clientID,
            String productID, int quantity) {
        Client client = getClientByID(clientID);
        Product product = getProductByID(productID);
        if (client == null || product == null) {
            return null;
        }
        return client.addToWishList(product, quantity);
    }

    public Iterator<Client> getClients() {
        return ClientList.instance().getClients();
    }

    public Iterator<Product> getProducts() {
        return ProductList.instance().getProducts();
    }
}
