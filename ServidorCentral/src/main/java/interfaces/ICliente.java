/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

/**
 *
 * @author leo_N
 */
public interface ICliente extends Remote {
    public void OtorgarToken () throws RemoteException;    
    public int ObtenerOrientacion () throws RemoteException;    
    public Semaphore ObtenerSemaforo () throws RemoteException;    
}
