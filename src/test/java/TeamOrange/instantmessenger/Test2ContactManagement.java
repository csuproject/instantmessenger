package TeamOrange.instantmessenger;

import org.junit.Test;
import org.junit.runner.RunWith;


import TeamOrange.instantmessenger.controllers.CreateAccountController;
import TeamOrange.instantmessenger.controllers.LoginController;
import TeamOrange.instantmessenger.models.AppContacts;
import TeamOrange.instantmessenger.views.AccountScreen;
import TeamOrange.instantmessenger.xmpp.BabblerBase;
import TeamOrange.instantmessenger.xmpp.MessageListener;
import TeamOrange.instantmessenger.xmpp.PresenceListener;
import TeamOrange.instantmessenger.xmpp.RosterListener;

//JFX runner 
import de.saxsys.javafx.test.JfxRunner;

//Mockito
import static org.mockito.Mockito.*;
import org.mockito.Mock;

@RunWith(JfxRunner.class)
public class Test2ContactManagement {
	
	//pre-requisites for test setup
	//@Before
	//Babbler base pre-requisites
	private String hostName = "teamorange.space";
	private MessageListener ml;
	private PresenceListener pl;
	private RosterListener rl;
	private AppContacts contacts;
	
	//BabblerBase bb = new BabblerBase(hostName, ml,pl, rl);
	BabblerBase bb = null;
		
	//Team Orange View Layer
	@Mock
	AccountScreen as = new AccountScreen();
	
	LoginController lc = new LoginController(bb, as, contacts);
	
	@Test
	public void TestCreateContacts(){
		CreateAccountController cac = new CreateAccountController(bb, as);
		cac.createAccount("JoeBlogg", "Password1");
		//if user is created successfully then user can login
		lc.login("JoeBlogg", "Password1");
	}
	
	// clean up/tear down after tests are complete
	

}
