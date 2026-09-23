import java.util.*;
import java.io.*;


public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String address;
    private List<WishListItem> wishlist = new LinkedList<WishListItem>();

    public Client(String id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
        //NEED to potentially add a field to initialize an empty wishlist?
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setId(String id){
        this.id = id;
    }

    public List<WishListItem> getWishList(){
        return wishlist;
    }

    public boolean equals(String id){
        return this.id.equals(id);
    }

    public String toString(){
        String string = "Client name " + name + " address " + address + " id " + id;
        return string;
    }
}