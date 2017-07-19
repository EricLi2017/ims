package amazon.mws.fulfillment;

import java.util.ArrayList;
import java.util.List;

/**
 * Store message for updating FBA inventory
 * Created by Eric Li on 3/1/2017.
 */
public class MessageForUpdateInventory {
    /* message list*/
    private List<String> messages = new ArrayList<>();
    /*if the messages is end/complete*/
    private boolean isComplete;
    /*lazy initialization*/
    private static MessageForUpdateInventory ourInstance = null;

    public synchronized static MessageForUpdateInventory getInstance() {

        if (ourInstance == null) {
            ourInstance = new MessageForUpdateInventory();
        }
        return ourInstance;
    }

    private MessageForUpdateInventory() {
    }

    /**
     * clear all the messages and ready to receive/add message
     */
    public synchronized void startMessage() {
        clearMessage();
        isComplete = false;
    }

    /**
     * add a message if it is ready
     *
     * @param message to be added message
     */
    public synchronized void addMessage(String message) {
        if (isComplete()) return;

        messages.add(message);
    }

    /**
     * end messages, could not add new message in
     */
    public synchronized void endMessage() {
        isComplete = true;
    }

    /**
     * clear all the messages only
     */
    public synchronized void clearMessage() {
        messages = new ArrayList<>();
    }


    public List<String> getMessage() {
        return messages;
    }

    public boolean isComplete() {
        return isComplete;
    }
}