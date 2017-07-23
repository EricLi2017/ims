/**
 * 
 */
package amazon.mws.order;

import java.util.TimerTask;

/**
 * Insert orders into IMS from MWS Orders API
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 23, 2017 Time: 9:40:09 PM
 */
public class ListOrdersTimerTask extends TimerTask {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// TODO
		System.out.println(new java.sql.Timestamp(System.currentTimeMillis()) + " " + this.getClass().getName()
				+ " run, scheduledExecutionTime =" + scheduledExecutionTime());

	}

}
