package TeamOrange.instantmessenger.models;

public class AppJid {
	private String local;
	private String domain;
	private String resource;

	public AppJid(String local, String domain){
		this.local = local;
		this.domain = domain;
	}

	public AppJid(String local, String domain, String resource){
		this(local, domain);
		this.resource = resource;
	}

	public String getLocal(){
		return local;
	}

	public String getDomain(){
		return domain;
	}

	public String getResource(){
		return resource;
	}

	public String getBareJid(){
		String bareJid = local + "@" + domain;
		return bareJid;
	}

	public String getJid(){
		String jid = getBareJid();
		if(resource != null && resource.length() > 0){
			jid += "/" + resource;
		}
		return jid;
	}

}
