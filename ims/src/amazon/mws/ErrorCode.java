/**
 * 
 */
package amazon.mws;

/**
 * Error codes
 * 
 * These error codes are common to all Amazon MWS API sections. For error codes
 * that are specific to an API section, see that API's corresponding error codes
 * section reference.
 * 
 * 
 * Error code HTTP status code Description
 * 
 * InputStreamDisconnected 400 There was an error reading the input stream.
 * 
 * InvalidParameterValue 400 An invalid parameter value was used, or the request
 * size exceeded the maximum accepted size, or the request expired.
 * 
 * AccessDenied 401 Access was denied.
 * 
 * InvalidAccessKeyId 403 An invalid AWSAccessKeyId value was used.
 * 
 * SignatureDoesNotMatch 403 The signature used does not match the server's
 * calculated signature value.
 * 
 * InvalidAddress 404 An invalid API section or operation value was used, or an
 * invalid path was used.
 * 
 * InternalError 500 There was an internal service failure.
 * 
 * QuotaExceeded 503 The total number of requests in an hour was exceeded.
 * 
 * RequestThrottled 503 The frequency of requests was greater than allowed.
 * 
 * Created by Eclipse. User: Eric Li Date: Jul 26, 2017 Time: 10:51:59 PM
 */
public enum ErrorCode {
	InputStreamDisconnected(400), InvalidParameterValue(400), AccessDenied(401), InvalidAccessKeyId(
			403), SignatureDoesNotMatch(
					403), InvalidAddress(404), InternalError(500), QuotaExceeded(503), RequestThrottled(503);

	private int statusCode;

	private ErrorCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int value() {
		return statusCode;
	}

}
