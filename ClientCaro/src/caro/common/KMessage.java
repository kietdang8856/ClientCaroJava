/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package caro.common;

import java.io.Serializable;

/**
 *
 * @author kietdang
 */
public class KMessage implements Serializable{
    private int type;
    private Object object;

    public KMessage(int type, Object object) {
        this.type = type;
        this.object = object;
    }
    
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }
    
    
}
