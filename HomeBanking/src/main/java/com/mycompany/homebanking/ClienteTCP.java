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

            out.writeUTF("2");
            // Esperar e imprimir el mensaje inicial del servidor (solicitud de cuenta)
            String mensajeInicial = in.readUTF();
            System.out.println(mensajeInicial);

            // Ingresar número de cuenta
            String NumCuenta = sn.next();
            out.writeUTF(NumCuenta);

            // Bucle para manejar el menú
            String respuestaServidor;
            do {
                // Leer el menú del servidor
                respuestaServidor = in.readUTF();
                System.out.println(respuestaServidor);

                // Leer la opción seleccionada por el cliente
                String opcion = sn.next();
                out.writeUTF(opcion);  // Enviar la opción al servidor

                // Leer la respuesta del servidor
                respuestaServidor = in.readUTF();
                System.out.println(respuestaServidor);

                // Si la opción es 1 (Transferencia), enviar datos adicionales
                if (opcion.equals("1")) {
                    // Ingresar monto a transferir
                    String monto = sn.next();
                    out.writeUTF(monto);
                    
                     respuestaServidor = in.readUTF();
                    System.out.println(respuestaServidor);

                    // Ingresar número de destinatario
                    String destinatario = sn.next();
                    out.writeUTF(destinatario);

                    // Leer confirmación de transferencia
                    respuestaServidor = in.readUTF();
                    System.out.println(respuestaServidor);
                }

            } while (!respuestaServidor.contains("hasta luego"));  // Finaliza cuando el servidor manda mensaje de salida
            
        } catch (UnknownHostException e) {
            System.err.println("host unreachable: localhost");
        } catch (IOException e) {
            System.err.println("cannot connect to: localhost");
        }
    }
}
