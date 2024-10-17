/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.homebanking;

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
        Socket sock = null;
        Scanner sn = new Scanner(System.in);
        sn.useDelimiter("\n");

        try {
            // Conectar con el servidor
            sock = new Socket("localhost", 7777);
            
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());

            //out.writeUTF("2");
            System.out.println("Bienvenido. Ingrese numero de cuenta"); 
            String idCuenta = sn.nextLine();
            out.writeUTF("HomeBanking,"+idCuenta+",0,0");
            String idCuentaExiste = in.readUTF();
            
            int opcion = 0;
            do {
                
                float fondosCuenta = 0;
                // Enviar opciones al cliente
                System.out.println("\nHola, ingresó a HomeBanking de "+idCuentaExiste+". Elige una opción:\n"
                        + "1. Transferencia\n"
                        + "4. Salir\n"
                        + "Ingrese el número de la opción:");

                // Leer la opción seleccionada por el cliente
                String opcionStr = sn.nextLine();
                System.out.println("ingrese ID del destino a transferir");
                String idDestino = sn.nextLine();
                System.out.println("ingrese monto a transferir");
                String montoTrnasferencia = sn.nextLine();
                out.writeUTF("Transferencia,"+idCuenta+","+montoTrnasferencia+","+idDestino);
                System.out.println("Transferencia exitosa: "+in.readUTF());
                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    System.out.println("Opción no válida, por favor ingrese un número.");
                    continue; // Volver al inicio del bucle
                }
            } while (opcion != 4);
            
        } catch (UnknownHostException e) {
            System.err.println("host unreachable: localhost");
        } catch (IOException e) {
            System.err.println("cannot connect to: localhost");
        }
    }    
}
