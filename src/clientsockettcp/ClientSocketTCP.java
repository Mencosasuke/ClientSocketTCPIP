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
                        System.out.println("Es un mensaje de chat");
                        break;
                }
            }
            catch(IOException e){
                //System.out.println("Error en envío: " + e.getMessage());
                ChatWindow.gui.ActualizarNotificaciones("Error en escucha: " + e.getMessage());
                done = true;
                stop();
            }
        }
        stop();
    }
    
    public void sendMessage(String mensaje, String emisor, int receptor){
        try{
            output.writeByte(1);
            ChatWindow.gui.ActualizarNotificaciones("Tú: " + mensaje);
            output.writeUTF(emisor + ": " + mensaje);
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
            output.writeUTF(username);
            output.flush();
        }
        catch(IOException e){
            //System.out.println("Error en envío: " + e.getMessage());
            ChatWindow.gui.ActualizarNotificaciones("Error en envío: " + e.getMessage());
        }
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