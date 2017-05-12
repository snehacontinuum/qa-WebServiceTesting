
package continuum.ws;

import org.testng.annotations.Test;

import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunListener;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.plugins.ListenerConfiguration;




public class UpdateResultsToTestRail  {

	
	@Test
	public void updateResultToTest(){
		System.out.println("#################### Updating Results to test rail ##########");
		if(Utility.getMavenProperties("TestRailUpdateFlag").equalsIgnoreCase("true"))
	       TestRailIntegrator.updateResultToTestRail();
	}





}
