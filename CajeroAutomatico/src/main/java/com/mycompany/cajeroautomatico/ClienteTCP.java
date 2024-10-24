/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajeroautomatico;

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

            //out.writeUTF("2");
            System.out.println("Bienvenido al sistema de Cajero Automatico. Ingrese numero de cuenta");
            String idCuenta = sn.nextLine();
            out.writeUTF("CajeroAutomatico," + idCuenta + ",0,0");
            String idCuentaExiste = in.readUTF();

            int opcion = 0;
            do {

                float fondosCuenta = 0;
                // Enviar opciones al cliente
                System.out.println("\nHola " + idCuentaExiste + " elige una opción:\n"
                        + "1. Retiro\n"
                        + "2. Depósito\n"
                        + "3. Transferencia\n"
                        + "4. Salir\n"
                        + "Ingrese el número de la opción:");

                // Leer la opción seleccionada por el cliente
                String opcionStr = sn.nextLine();
                switch (opcionStr) {
                    case "1":
                        System.out.println("ingrese monto a descontar");
                        String montoPagar = sn.nextLine();
                        out.writeUTF("Descuento," + idCuenta + "," + montoPagar);
                        break;
                    case "2":
                        System.out.println("ingrese monto a depositar");
                        String montoDepositar = sn.nextLine();
                        out.writeUTF("Deposito," + idCuenta + "," + montoDepositar);
                        break;
                    case "3":
                        System.out.println("ingrese ID del destino a transferir");
                        String idDestino = sn.nextLine();
                        System.out.println("ingrese monto a transferir");
                        String montoTrnasferencia = sn.nextLine();
                        out.writeUTF("Transferencia," + idCuenta + "," + montoTrnasferencia + "," + idDestino);
                        System.out.println("Transferencia exitosa: " + in.readUTF());
                        break;
                    default:
                        break;
                }

                System.out.println("Operacion exitosa: " + in.readUTF());
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
