package continuum.ws.listeners;

import java.util.HashMap;
import java.util.List;

import org.testng.Reporter;

import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestRunListener;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;

public class CustomTestRunListener implements TestRunListener {

	public static HashMap<String, String> apiResuls=new HashMap<>();
	public static HashMap<String, String[]> apiResponse=new HashMap<>();

	public void beforeRun(TestCaseRunner tcRunner, TestCaseRunContext tcRunContext) {
		Reporter.log("*** EXECUTING TEST CASE " + tcRunner.getTestCase().getName());
	}

	public void beforeStep(TestCaseRunner tcRunner, TestCaseRunContext tcRunContext) {
	}

	public void beforeStep(TestCaseRunner tcRunner, TestCaseRunContext tcRunContext, TestStep testStep) {
		//Reporter.log("*** BEFORE TEST STEP: EXECUTING TEST STEP" + testStep.getName());
	}

	public void afterStep(TestCaseRunner tcRunner, TestCaseRunContext tcRunContext, TestStepResult testStepResult) {
	//	Reporter.log("*** AFTER TEST STEP: STATUS OF TEST STEP " +testStepResult.getTestStep().getName()+" HAS "+ testStepResult.getStatus());
		//		for (int i = 0; i < testStepResult.getMessages().length; i++) {
		//			System.out.println(i + ". " + testStepResult.getMessages()[i]);
		//		}
	}

	public void afterRun(TestCaseRunner tcRunner, TestCaseRunContext tcRunContext) {
		//System.out.println("****** AFTER TEST RUN: FETCHING RESULTS ");
		String webServiceTestResult=null;
		String testCaseResult=null;
		String msg="Executed via automation";
		long duration=0;
		List<TestStepResult> testResults =tcRunner.getResults();
		String testCase =tcRunContext.getTestCase().getLabel();
		for (TestStepResult result : testResults) 
		{
			
			//	System.out.println(result.getTestStep().getTestCase().getName() +" has result "+result.getStatus());
			//	System.out.println(result.getTestStep().getTestCase().getName() +" has error "+result.getError() +" time taken "+result.getTimeTaken());
			if(result.getStatus().name().equalsIgnoreCase("FAILED"))
			{
				testCaseResult="FAILED";
           	}
			else
				testCaseResult="OK";
			    duration=duration+result.getTimeTaken();
		}
		Reporter.log("After Test Run :"+testCase +" has result "+testCaseResult);
		webServiceTestResult=testCaseResult+"-"+duration+"-"+msg;
		apiResuls.put(testCase,webServiceTestResult);
}


public static HashMap<String, String> getReport() {
	return apiResuls;
}






}
