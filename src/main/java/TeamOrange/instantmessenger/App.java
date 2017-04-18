package TeamOrange.instantmessenger;


import java.io.IOException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils.AcceptAllTrustManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;


public class App
{
	
    public static void main( String[] args )
    {
    	System.out.println("Hello!");
    	}
    
    public AbstractXMPPConnection login(AbstractXMPPConnection connection,
    		String userName, String password) {
    	
	 	   // Login with User 
		   try {
			connection.login(userName, password);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
 }
 
	 /**
	  * Create account
	  * @param connection
	  * @param userName
	  * @param password
	  */
	 public AbstractXMPPConnection createAccount(AbstractXMPPConnection connection,
			 String userName, String password) {
	 	
	     AccountManager accountManager = AccountManager.getInstance(connection);
	     accountManager.sensitiveOperationOverInsecureConnection(true);
	     
	     // Create account on Server
	     try {
				accountManager.createAccount(Localpart.from(userName), password);
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMPPErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmppStringprepException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connection;
	 }
    
    /**
     * Create Connection
     * @param hostname
     * @return 
     */
    public AbstractXMPPConnection connect(String hostname) {
    	
    	XMPPTCPConnectionConfiguration connConfig = null;
    	AbstractXMPPConnection connection;
    	
    		// Build connection configuration
			try {
				connConfig = XMPPTCPConnectionConfiguration.builder()
				        .setHost(hostname)  
				        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
				        .setPort(5222)        
				        .setDebuggerEnabled(true)
				        .setXmppDomain(hostname)
				        .build();
			} catch (XmppStringprepException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Connect with Server
			connection = new XMPPTCPConnection(connConfig);
		    try {
				connection.connect();
			} catch (SmackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return connection;
    }
}
