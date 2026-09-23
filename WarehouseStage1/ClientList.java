import java.util.*;
import java.io.*;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List clients = new LinkedList<Client>();
    private static ClientList clientlist;
    private static final String CLIENT_STRING = "C";
    private static int idNum = 1;

    private ClientList(){}

    public static ClientList instance()  {
        if (clientList == null ) {
            return (clientList = new ClientList());
        }
        else{
            return clientList;
        }
    }

    public void insertClient(String name, String address){
        String id = CLIENT_STRING + idNum;
        idNum++;
        Client client = new Client(id,name,address);
        clients.add(client);

    }

    public Iterator getClients(){
        return clients.iterator();
    }

    public Client search(String clientID){
        for (Client client : clients){
            if(client.getID().equals(clientID)){
                return client;
            }
        }
        return null;
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
        output.defaultWriteObject();
        output.writeObject(clientList);
        } catch (IOException ioe) {
        ioe.printStackTrace();
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
    try {
      if (clientList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (clientList == null) {
          clientList = (ClientList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public String toString() {
    return clients.toString();
  }



}