package TeamOrange.instantmessenger.models;

/**
 * Represents the connection status of the application.
 * Either CONNECTED or NOT_CONNECTED
 *
 */
public class AppConnection {
	public static final int CONNECTED = 0;
	public static final int NOT_CONNECTED = 1;

	private int status;

	public AppConnection(int status){
		if(status == 0){
			status = CONNECTED;
		} else {
			status = NOT_CONNECTED;
		}
	}

	public void setStatusToConnected(){
		status = CONNECTED;
	}

	public void setStatusToNotConnected(){
		status = NOT_CONNECTED;
	}

	public int getStatus(){
		return status;
	}

}
