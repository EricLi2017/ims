/**
 * 
 */
package amazon.mws.order;

import java.util.TimerTask;

/**
 * Insert order items into IMS from MWS Orders API
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 10:23:48 PM
 */
public class ListOrderItemsTimerTask extends TimerTask {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " " + this.getClass().getName()
				+ " run, scheduledExecutionTime =" + scheduledExecutionTime());

	}

}
