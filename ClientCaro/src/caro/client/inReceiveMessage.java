/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caro.client;

import caro.common.KMessage;
import java.io.IOException;

/**
 *
 * @author kietdang
 */
public interface inReceiveMessage {
    
    public void ReceiveMessage(KMessage msg) throws IOException;
}
