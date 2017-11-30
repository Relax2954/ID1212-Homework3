package se.kth.id1212.db.catalogjdbc.server.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import se.kth.id1212.db.catalogjdbc.common.Catalog;
import se.kth.id1212.db.catalogjdbc.server.controller.Controller;
import se.kth.id1212.db.catalogjdbc.server.integration.CatalogDBException;

/**
 * Starts the catalog server.
 */
public class Server {
    private static final String USAGE = "java catalogjdbc.Server [catalog name in rmi registry] "
                                        + "[catalog database name] [dbms: derby or mysql]";
    private String catalogName = Catalog.CATALOG_NAME_IN_REGISTRY;
    private String datasource = "Catalog";
    private String dbms = "derby";

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.parseCommandLineArgs(args);
            server.startRMIServant();
            System.out.println("Catalog server started.");
        } catch (RemoteException | MalformedURLException | CatalogDBException e) {
            System.out.println("Failed to start catalog server.");
            e.printStackTrace();
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException, CatalogDBException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller contr = new Controller(datasource, dbms);
        Naming.rebind(catalogName, contr);
    }

    private void parseCommandLineArgs(String[] args) {
        if (args.length > 3 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.length > 0) {
            catalogName = args[0];
        }

        if (args.length > 1) {
            datasource = args[1];
        }

        if (args.length > 2) {
            dbms = args[2];
        }
    }
}
