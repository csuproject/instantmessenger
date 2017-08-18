package TeamOrange.instantmessenger.lambda;

public interface SendNewMessageEvent {
	public void send(String message, String userName);
}
