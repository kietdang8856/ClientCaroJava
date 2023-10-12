package caro.common;

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kietdang
 */
public class Users implements Serializable{
    private int Id;
    private String username;
    private String password;
    private int win;

    public Users(int Id, String username, String password, int win, int lose, int score) {
        this.Id = Id;
        this.username = username;
        this.password = password;
        this.win = win;
        this.lose = lose;
        this.score = score;
    }
    private int lose;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
    private int score;
    
    public Users()
    {
    
    }
    
  
    
    public int getId()
    {
        return Id;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }                    
    public int getWin()
    {
        return win;
    }        
    public int getLose()
    {
        return lose;
    }
    public int getScore()
    {
        return score;
    }
    public void setId(int id)
    {
        this.Id = id;
    }
    public void setUsername(String username)
    {
         this.username = username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }                    
    public void setWin(int win)
    {
        this.win = win;
    }        
    public void setLose(int lose)
    {
        this.lose = lose;
    }
    public void setScore(int score)
    {
        this.score = score;
    }
}
