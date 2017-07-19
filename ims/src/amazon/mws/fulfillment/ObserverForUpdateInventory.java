package amazon.mws.fulfillment;

import java.util.Observable;
import java.util.Observer;

/**
 * Observe ListInventoryManager update FAB inventory and store the result to MessageForUpdateInventory
 *
 * @see ListInventoryManager
 * @see MessageForUpdateInventory
 * @see java.util.Observer
 * <p>
 * Created by Eric Li on 3/1/2017.
 */
public class ObserverForUpdateInventory implements Observer {
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param msg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object msg) {
        if (msg == null) return;

        if (msg instanceof Boolean) {//Boolean is a flag for messages start or end: false=start; true=end
            if ((boolean) msg) {//messages end
                MessageForUpdateInventory.getInstance().endMessage();
            } else {//messages start
                MessageForUpdateInventory.getInstance().startMessage();
            }
        } else if (msg instanceof String) {//String is a flag for adding a message
            MessageForUpdateInventory.getInstance().addMessage((String) msg);
        }

    }
}