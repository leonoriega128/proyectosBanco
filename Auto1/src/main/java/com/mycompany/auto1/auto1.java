package com.mycompany.auto1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import interfaces.*;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class ThreadAuto extends Thread {

    Semaphore s;
    IservidorCentral servidorCentral;
    IAuto auto;

    public ThreadAuto(IservidorCentral server, IAuto c) {
        servidorCentral = server;
        auto = c;
        try {
            s = auto.ObtenerSemaforo();
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            int orientacion = -1;
            try {
                orientacion = auto.ObtenerOrientacion();
            } catch (RemoteException ex) {
                java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
            }
            String orient = "";
            if (orientacion == 1) {
                orient = "Norte a Sur";
            } else {
                orient = "Sur a Norte";
            }
            System.out.println("Auto avanzando de " + orient);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("El auto llegó al puente. Esperando habilitación para cruzar " + orient);
            try {
                servidorCentral.SolicitarToken(auto);
            } catch (RemoteException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                s.acquire();
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Cruce habilitado. Cruzando de " + orient);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                servidorCentral.LiberarToken(auto);
            } catch (RemoteException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("El auto finalizó de cruzar el puente desde " + orient);
            try {
                Thread.sleep(11000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ThreadAuto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

public class auto1 extends UnicastRemoteObject implements IAuto {

    int orientacion, id, puerto;
    Semaphore semaforo;
    IservidorCentral servidor;

    public auto1(int ident, int nroPuerto) throws RemoteException {
        super();
        id = ident;
        puerto = nroPuerto;
        orientacion = ThreadLocalRandom.current().nextInt(1, 2);
        semaforo = new Semaphore(0);
    }

    @Override
    public void OtorgarToken() throws RemoteException {
        semaforo.release();
    }

    @Override
    public int ObtenerOrientacion() throws RemoteException {
        return this.orientacion;
    }

    @Override
    public Semaphore ObtenerSemaforo() throws RemoteException {
        return semaforo;

    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(1101);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }

        IservidorCentral servidor = null;
        try {
            servidor = (IservidorCentral) Naming.lookup("rmi://localhost:1099/servcentral");
        } catch (NotBoundException | RemoteException | MalformedURLException ex) {
            //java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            servidor = (IservidorCentral) Naming.lookup("rmi://localhost:1110/servcentral2");
        } catch (NotBoundException | RemoteException | MalformedURLException ex) {
            //java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            servidor = (IservidorCentral) Naming.lookup("rmi://localhost:1500/servcentral3");
        } catch (NotBoundException | RemoteException | MalformedURLException ex) {
            //java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }

        IAuto auto = null;
        int id = 1;
        int puerto = 1101;
        try {
            auto = new auto1(id, puerto);
            Naming.rebind("rmi://localhost:" + puerto + "/auto" + id, auto);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(auto1.class.getName()).log(Level.SEVERE, null, ex);
        }
        ThreadAuto hiloAutomotor = new ThreadAuto(servidor, auto);
        hiloAutomotor.start();
    }
}
