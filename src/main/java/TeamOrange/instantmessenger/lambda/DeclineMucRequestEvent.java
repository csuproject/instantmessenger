package TeamOrange.instantmessenger.lambda;

public interface DeclineMucRequestEvent {
	public void decline(String roomID, String from);
}
