package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;


public interface IAuto extends Remote {
    public void OtorgarToken () throws RemoteException;    
    public int ObtenerOrientacion () throws RemoteException;    
    public Semaphore ObtenerSemaforo () throws RemoteException;    
}

