package com.mycompany.servidorcentral;

import interfaces.ICliente;
import interfaces.IservidorCentral;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class servcentral extends UnicastRemoteObject implements IservidorCentral {

    private final Queue<ICliente> clienteCajeroQueue;
    private final Queue<ICliente> ultimoclienteCajeroQueue;
    private boolean token;
    private boolean recibioRespuesta;

    public servcentral() throws RemoteException {
        super();
        token = true;
        clienteCajeroQueue = new LinkedList<>();
        ultimoclienteCajeroQueue = new LinkedList<>();
        recibioRespuesta = false;
    }

    @Override
    public void estRecibioRespuesta(boolean b) {
        recibioRespuesta = b;
    }

    @Override
    public boolean ObtenerReciboRespuesta() {
        return recibioRespuesta;
    }

    @Override
    public void LiberarToken(ICliente cliente) throws RemoteException {
        Queue<ICliente> queueToCheck = cliente.ObtenerOrientacion() == 1 ? clienteCajeroQueue : ultimoclienteCajeroQueue;
        queueToCheck.remove(cliente);

        if (queueToCheck.isEmpty()) {
            token = true;
            Queue<ICliente> oppositeQueue = cliente.ObtenerOrientacion() == 1 ? ultimoclienteCajeroQueue : clienteCajeroQueue;
            if (!oppositeQueue.isEmpty()) {
                this.SolicitarToken(oppositeQueue.poll());
            }
        }
    }

    @Override
    public void SolicitarToken(ICliente cliente) throws RemoteException {
        Queue<ICliente> currentQueue = cliente.ObtenerOrientacion() == 1 ? clienteCajeroQueue : ultimoclienteCajeroQueue;
        Queue<ICliente> oppositeQueue = cliente.ObtenerOrientacion() == 1 ? ultimoclienteCajeroQueue : clienteCajeroQueue;

        if (oppositeQueue.isEmpty() && token) {
            cliente.OtorgarToken();
            token = false;
        } else {
            currentQueue.add(cliente);
        }
    }

    public static void main(String[] args) {
        setupSecurityManager();
        try {
            LocateRegistry.createRegistry(1099);
            IservidorCentral servidor = new servcentral();
            Naming.rebind("rmi://localhost:1099/servcentral", servidor);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(servcentral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void setupSecurityManager() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }

    @Override
    public void Saludar(IservidorCentral servidor) throws RemoteException {
        servidor.Responder();
    }

    @Override
    public void Responder() throws RemoteException {
        recibioRespuesta = true;
    }
}

class Saludo extends TimerTask {
    private final IservidorCentral servidorControl;

    public Saludo(IservidorCentral sc) {
        this.servidorControl = sc;
    }

    @Override
    public void run() {
        try {
            servidorControl.estRecibioRespuesta(false);
            IservidorCentral servidor = (IservidorCentral) Naming.lookup("rmi://localhost:1099/servcentral");
            servidor.Saludar(servidorControl);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Saludo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ControlarRespuesta extends TimerTask {
    private final IservidorCentral servidorControl;

    public ControlarRespuesta(IservidorCentral serv) {
        this.servidorControl = serv;
    }

    @Override
    public void run() {
        try {
            if (!servidorControl.ObtenerReciboRespuesta()) {
                System.out.println("El servidor central falló");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ControlarRespuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
