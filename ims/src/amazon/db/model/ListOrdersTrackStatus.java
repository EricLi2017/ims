/**
 * 
 */
package amazon.db.model;

/**
 * Created by Eclipse. User: Eric Li Date: Jul 27, 2017 Time: 10:50:31 PM
 */
public enum ListOrdersTrackStatus {
	Pending((byte) 0), Completed((byte) 1);
	private byte status;

	private ListOrdersTrackStatus(byte status) {

		this.status = status;
	}

	public byte value() {
		return status;
	}
}
