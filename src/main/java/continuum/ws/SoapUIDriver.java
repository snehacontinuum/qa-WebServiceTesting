/**
 * 
 */
package continuum.ws;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import continuum.ws.listeners.CustomTestRunListener;

/**
 * @author sneha.chemburkar
 *
 */
public class SoapUIDriver {
	
	String projectFile=null;
	
  
	
	@Test
	public static void executeProjectFile(){
		HashMap<String, String> apiResults=new HashMap<>();
		//public static HashMap<String, String[]> apiResponse=new HashMap<>();
		System.out.println("**************Execute Soap API project**************************");
		try {

			    SoapUIIntegrator.executeSoapUITC(Utility.getMavenProperties("ProjectFile"));
			
		} catch (Exception e) {
			System.out.println("********************Error in executing Soap API project  **********");
			e.printStackTrace();
		}
		apiResults=CustomTestRunListener.getReport();
		printResults(apiResults);
		HtmlEmailSender.sendReport();
		TestRailIntegrator.updateResultToTestRail();
	}

	
	public static void printResults(HashMap<String, String> apiResults){
		for(Map.Entry<String, String> testC : apiResults.entrySet()) {
			System.out.println("API result "+testC.getKey() + " has result "+testC.getValue());
		}
	}
}
