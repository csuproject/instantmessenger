package TeamOrange.instantmessenger.models;

import rocks.xmpp.core.stanza.model.Presence;

public class AppPresence {
	
	public enum Type {
		AVAILIBLE, UNAVAILIVLE
	}
	
	private Type type;
	
	public AppPresence(Type type){
		this.type = type;
	}
	
	public AppPresence(Presence presence){
		if(presence.getType() == null){
			this.type = AppPresence.Type.AVAILIBLE;
		} else if(presence.getType() == Presence.Type.UNAVAILABLE){
			this.type = AppPresence.Type.UNAVAILIVLE;
		} else {
			this.type = null;
		}
	}
	
	public Type getType(){
		return type;
	}
	
	public String sGetType(){
		return typeString(type);
	}
	
	public void set(Type type){
		this.type = type;
	}
	
	public static String typeString(Type type){
		if(type == Type.AVAILIBLE){
			return "AVAILIBLE";
		} else if(type == Type.UNAVAILIVLE){
			return "UNAVAILIBLE";
		} else{
			return "OTHER";
		}
	}
	
}
