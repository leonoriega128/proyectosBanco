package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IservidorCentral extends Remote {

    public void LiberarToken(ICliente cliente) throws RemoteException;

    public void SolicitarToken(ICliente cliente) throws RemoteException;

    public void Saludar(IservidorCentral servidor) throws RemoteException;

    public void Responder() throws RemoteException;

    public void estRecibioRespuesta(boolean b) throws RemoteException;

    public boolean ObtenerReciboRespuesta() throws RemoteException;
}
