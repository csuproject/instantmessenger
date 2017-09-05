package TeamOrange.instantmessenger.lambda;

public interface AcceptMucRequestEvent {
	public void accept(String roomID, String from);
}
