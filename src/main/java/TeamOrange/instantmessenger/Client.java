package TeamOrange.instantmessenger;

interface MessageListener{
	void message();
}
interface PresenceListener{
	void presence();
}
interface RosterListener{
	void roster();
}

public abstract class Client {
	protected String hostName;
	protected MessageListener messageListener;
	protected PresenceListener presenceListener;
	protected RosterListener rosterListener;

	public Client(String hostName, MessageListener messageListener, PresenceListener presenceListener, RosterListener rosterListener){
		this.hostName = hostName;
		this.messageListener = messageListener;
		this.presenceListener = presenceListener;
		this.rosterListener = rosterListener;
	}

	public abstract boolean setupConnection();
	public abstract boolean connect();
	public abstract boolean login(String userName, String password);
	public abstract boolean logout();
	public abstract boolean createUser(String userName, String password);
	public abstract boolean close();
}
