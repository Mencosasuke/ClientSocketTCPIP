/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clientsockettcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author David Mencos
 */
public class ClientSocketTCP {

    /**
     * @param args the command line arguments
     */
    
    private Socket client = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    private String username = "";
    
    public void ChatClient(String serverName, int serverPort, String username){
        //System.out.println("Estableciendo conexión...");
        ChatWindow.gui.ActualizarNotificaciones("Estableciendo conexión...");
        try{
            client = new Socket(serverName, serverPort);
            //System.out.println("Conectado: " + client);
            ChatWindow.gui.ActualizarNotificaciones("Conectado: " + client);
            start();
            sendUserValues(username);
        }
        catch(UnknownHostException e){
            //System.out.println("Host desconocido: " + e.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Host desconocido: " + e.getMessage());
        }
        catch(IOException ex){
            //System.out.println("Error de I/O: " + ex.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Error de I/O: " + ex.getMessage());
        }
    }

    private void start() throws IOException{
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
    }
    
    public void listen(){
        String line = "";
        boolean done = false;
        while (!done){
            try{
                byte tipoMensaje = input.readByte();
                
                line = input.readUTF();
                
                switch(tipoMensaje){
                    case 0:
                        ChatWindow.gui.ActualizarNotificaciones(line);
                        break;
                    case 1:
                        ChatWindow.gui.ActualizarNotificaciones(line);
                        break;
                    case 2:
                        ChatWindow.gui.ActualizarNotificaciones(line);
                        break;
                    case 3:
                        ChatWindow.gui.ActualizarNotificaciones(line);
                        break;
                }
            }
            catch(IOException e){
                //System.out.println("Error en envío: " + e.getMessage());
                ChatWindow.gui.ActualizarNotificaciones("El servidor no esta disponible o se ha desconectado. " + e.getMessage());
                done = true;
                stop();
            }
        }
        stop();
    }
    
    public void sendMessage(String mensaje, String emisor, int tipoMensaje, int puertoCliente){
        try{
            output.writeByte(tipoMensaje);
            ChatWindow.gui.ActualizarNotificaciones("Tú: " + mensaje);
            if(tipoMensaje == 3){
                output.writeUTF(emisor + ":" + mensaje.substring(mensaje.indexOf(' '), mensaje.length()) + mensaje.substring(mensaje.indexOf('@'), mensaje.indexOf(' ')));
            }else{
                output.writeUTF(emisor + ": " + mensaje + puertoCliente);
            }
            output.flush();
        }
        catch(IOException e){
            //System.out.println("Error en envío: " + e.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Error en envío: " + e.getMessage());
        }
    }
    
    private void sendUserValues(String username){
        try{
            output.writeByte(0);
            output.writeUTF(username + getPortClient());
            output.flush();
        }
        catch(IOException e){
            //System.out.println("Error en envío: " + e.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Error en envío: " + e.getMessage());
        }
    }
    
    public int getPortClient(){
        return client.getLocalPort();
    }
    
    private void stop(){
        try{
            if (input != null){
                input.close();
            }
            if (output != null){
                output.close();
            }
            if (client != null){
                client.close();
            }
        }
        catch(IOException e){
            //System.out.println("Error cerrando conexiones: " +  e.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Error cerrando conexiones: " +  e.getMessage());
        }
    }
    
//    public static void main(String[] args) {
//    }
}
