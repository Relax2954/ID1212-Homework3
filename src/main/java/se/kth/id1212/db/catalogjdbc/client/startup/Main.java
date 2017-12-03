
package se.kth.id1212.db.catalogjdbc.client.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import se.kth.id1212.db.catalogjdbc.client.view.NonBlockingInterpreter;
import se.kth.id1212.db.catalogjdbc.common.Catalog;

/**
 * Starts the client.
 */
public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
            Catalog catalog = (Catalog) Naming.lookup(Catalog.CATALOG_NAME_IN_REGISTRY);
            new NonBlockingInterpreter().start(catalog);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Could not start catalog client.");
        }
    }
}
