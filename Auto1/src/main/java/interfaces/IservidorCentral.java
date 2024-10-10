package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IservidorCentral extends Remote {
    public void LiberarToken (IAuto auto) throws RemoteException;
    public void SolicitarToken (IAuto auto) throws RemoteException;
    
    
}
