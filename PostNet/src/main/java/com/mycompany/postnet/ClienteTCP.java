/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.postnet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leo_N
 */
public class ClienteTCP {

    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
                Socket sock = null;
                Scanner sn = new Scanner(System.in);
                sn.useDelimiter("\n");
                
		//PrintWriter sockOut = null;
		//BufferedReader sockIn = null;
		try {   
                        sock = new Socket("localhost", 7777); // the communication socket.
			//sockOut = new PrintWriter(sock.getOutputStream(), true); //force write
			//sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        
                        //
                        DataInputStream in = new DataInputStream(sock.getInputStream());
                        DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            
                         // Escribe el nombre y se lo manda al servidor
                         
                        out.writeUTF("3");
                        
                        // Leer mensaje del servidor
                        String mensaje = in.readUTF();
                        System.out.println(mensaje);
                        
                        String NumCuenta = sn.next();
                        out.writeUTF(NumCuenta);
                        
                        // opcion Menu
                         String Menu = in.readUTF();
                         System.out.println(Menu);
                         Menu = sn.next();
                         //
                         out.writeUTF(Menu);
                         Menu = in.readUTF();
                         System.out.println(Menu);
                         // 
                         Menu = sn.next();
                         out.writeUTF(Menu);
                         //
                         Menu = in.readUTF();
                         out.writeUTF(Menu);
                         System.out.println(Menu); 
                         String mensajeMonto = sn.next();
                        out.writeUTF(mensajeMonto);
                         
                        String mensajeMonto2 = in.readUTF();  // Espera el mensaje del servidor
                        out.writeUTF(mensajeMonto2);    // Imprime "Has seleccionado: Transferencia\nIngrese el monto a transferir:"
                        System.out.println(mensajeMonto2); 
                 

                        // ejecutamos el hilo
                        ClienteHilo hilo = new ClienteHilo(in, out);
                        hilo.start();
                        hilo.join();
                        
		} catch (UnknownHostException e) {
		System.err.println("host unreachable: localhost");
		System.exit(1);
		} catch (IOException e) {
			System.err.println("cannot connect to: localhost");
			System.exit(1);
		} catch (InterruptedException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        } 
		//Scanner scan = new Scanner(System.in);
		//String message = scan.next().toLowerCase();
		//while (! message.equals("no")) {
			//sockOut.println(message);
			//String recu = sockIn.readLine();
			//System.out.println("server -> client:" + recu);
			//message = scan.next().toLowerCase();
		//}
		//sockOut.close();
		//sockIn.close();  
		//Scanner scan = new Scanner(System.in);
		//String message = scan.next().toLowerCase();
		//while (! message.equals("no")) {
			//sockOut.println(message);
			//String recu = sockIn.readLine();
			//System.out.println("server -> client:" + recu);
			//message = scan.next().toLowerCase();
		//}
		//sockOut.close();
		//sockIn.close(); 
    }
    
}
