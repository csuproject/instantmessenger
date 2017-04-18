package TeamOrange.instantmessenger;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;

public class TestPlan1 {
	
	TestPlan1() {};
	
	public void runTest() {
	    try {
	    	// Creating a connection
	    	System.out.println("Creating a connection");
	    	XMPPTCPConnectionConfiguration connConfig =
	    	        XMPPTCPConnectionConfiguration.builder()
	    	                .setHost("teamorange.space")  // Name of your Host
	    	                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
	    	                .setPort(5222)          // Your Port for accepting c2s connection
	    	                .setDebuggerEnabled(true)
	    	                .setXmppDomain("teamorange.space")
	    	                .build();
	    	AbstractXMPPConnection connection = new XMPPTCPConnection(connConfig);
	        connection.connect();
	        
	        System.out.println("Connection to teamorange.space XXMP Server is " + connection.isConnected());

	        // Registering the user
	        AccountManager accountManager = AccountManager.getInstance(connection);
	        System.out.println("Acount Manager Object Created");
	        accountManager.sensitiveOperationOverInsecureConnection(true);
	        accountManager.createAccount(Localpart.from("smackuser"), "smackuser");
	        System.out.println("Created account smackuser");
	        
	 	   // Login with User 
		   connection.login("smackuser", "smackuser");
		   
		   System.out.println("Login with smackuser is " + connection.isAuthenticated());
		   
	    } catch (Exception e) {
	        System.out.print(e.getMessage());
	    	}
	    
	}
	    
	
	    
}
