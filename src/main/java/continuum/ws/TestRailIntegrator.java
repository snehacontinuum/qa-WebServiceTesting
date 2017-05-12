package continuum.ws;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;

import continuum.ws.listeners.CustomTestRunListener;





/***************
 * test rail java API
 *  Method used to Save the Test Case Results in TestRail
	 * 
	 * @param testCaseID
	 *            TestRail Test Case ID "CXXXX"
	 * @param test Run
	 *            TestRail Test Set ID "RXXX"
	 * @param executionStatus
	 *            The execution result "Blocked", "Error", "Fail",
	 *            "Inconclusive", "Pass"
 *@author sneha.chemburkar
 */


public class TestRailIntegrator {
	static TestRailAPIClient client;

	public static void updateResultToTestRail(){
		int result=0;
			
			
		connectToTestRail();
		String testRun=Utility.getMavenProperties("TestRun");
		//String testRun="R1252";
		if(testRun.contains("R"))
		           testRun=testRun.split("R")[1];
		
		Map<String, String> apiResults=CustomTestRunListener.getReport();
				
		if(!apiResults.isEmpty())
		{
		  for(Map.Entry<String, String> testC : apiResults.entrySet()) {
			  result=getTestRailStatus(testC.getValue());
			    
			     addResultTestRail(testRun,getTcID(testC.getKey()),result,getDuration(testC.getValue()),getErrorMsg(testC.getValue()));
		     }
		}
		else
			System.out.println("*****Results not updated in Test Rail  ***** ");
	  
	}

	
	private static String getTcID(String key) {
		if(key.contains("-"))
		{
			 
		String tcID=key.split("-")[0].trim();
		
		if(tcID.contains("C"))
			tcID=tcID.split("C")[1];
		System.out.println("Test case id= "+tcID);
		return tcID;
		}
		else
			 Assert.fail("Test case id is not mentioned against Scenario");
	     return null;	  
	}

	private static String getErrorMsg(String result) {
		String errormsg=result.split("-")[2];
		return errormsg;
		
	}


	private static long getDuration(String result) {
		String duration=result.split("-")[1];
		
	
		//System.out.println("total execution time "+durSec);
		return Long.valueOf(duration);
		
		
	}




	
	public static int getTestRailStatus(String result){
		String status=result.split("-")[0];
	    int testRailStatus=0;
		switch(status){
        case "OK":
        	testRailStatus = 1;
        	break;
        case "FAILED":
        	testRailStatus = 5;
        	break;
        case "UNKNOWN":
        	testRailStatus = 2;
        	break;
        }
		return testRailStatus;
	}
	
	public static void connectToTestRail(){
		
		client = new TestRailAPIClient(Utility.getMavenProperties("TestRailUrl"));
		client.setUser(Utility.getMavenProperties("TestRailUserName"));
		client.setPassword(Utility.getMavenProperties("TestRailPassword"));
	}
	
	public static void addResultTestRail( String testRunID,String testCaseID,int status, Long duration ,String errorMsg)  {
		try {
			JSONObject obj = (JSONObject) client.sendGet("get_case/"
					+ Integer.parseInt(testCaseID));
			Map<String, Comparable> data = new HashMap<String, Comparable>();

			data.put("status_id", status);
			data.put("elapsed",duration+"s");
			data.put("comment", errorMsg);
			JSONObject r = (JSONObject) client.sendPost("add_result_for_case/"+
					testRunID + "/" + testCaseID, data);
			System.out.println("*******Result updated in  Test Rail  Successfully   ***************************");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TestRailAPIException e) {
			e.printStackTrace();
		}
	}

	
	
	  
	
	



}



