/**
 * This class represents a message and contains all the data of the message
 */
package client.model;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private static final long serialVersionUID = 3319185104051485657L;
    private User receiver;
    private User sender;
    private String text;
    private ImageIcon image;
    private LocalDateTime dateSend;
    private LocalDateTime dateReceived;
    private String imagePath;
    private boolean imageHasBeenShown = false;

    /**
     * Constructor for message
     * @param text
     * @param image
     * @param sender
     * @param receiver
     */
    public Message(String text, ImageIcon image, User sender, User receiver){
        this.text = text;
        this.image = image;
        this.sender = sender;
        this.receiver = receiver;
        this.dateSend = LocalDateTime.now();
    }

    public Message(String text, ImageIcon image , User sender, User receiver, LocalDate dateSend){
        this.text = text;
        this.image = image;
        this.sender = sender;
        this.receiver = receiver;
        this.dateSend = LocalDateTime.of(1,1,1,0,0);
    }

    /**
     * Getters and setters for the variables
     * @return
     */
    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {return text;}

    public ImageIcon getImage() {return image;}
    public void setImage(ImageIcon image) {this.image = image;}

    public LocalDateTime getDate() {return dateSend;}

    public boolean isImageHasBeenShown() {
        return imageHasBeenShown;
    }
}
