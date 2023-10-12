/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caro.client;

import caro.common.KMessage;
import caro.common.Users;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kietdang
 */
class ListenServer extends Thread {
        Socket socket;
        OutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        InputStream inputStream;
        ObjectInputStream objectInputStream;

        public Users user;
        
        public inReceiveMessage receive;
        
        ListenServer(Socket socket) throws IOException {
            this.socket = socket;
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

        }

        @Override
        public void run() {
            do {
                try {
                    Object o = objectInputStream.readObject();
                    if (o != null && receive!=null) {
                        receive.ReceiveMessage((KMessage) o);
                    }

                } catch (IOException e) {
                    //System.out.println("There're some error");
                } catch (ClassNotFoundException ex) {
                    
                }
            }while (true);
    
        }

        public void SendMessage(int ty, Object obj) throws IOException {
            KMessage temp = new KMessage(ty, obj);
            SendMessage(temp);
        }
        
        public void SendMessage(KMessage msg) throws IOException {
            objectOutputStream.reset();
            objectOutputStream.writeObject(msg);
        }
    }
    