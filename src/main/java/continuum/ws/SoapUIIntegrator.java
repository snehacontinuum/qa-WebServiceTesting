package continuum.ws;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.eviware.soapui.impl.wsdl.WsdlProjectPro;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.TestAssertionRegistry;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathContentAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathCountAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.json.JsonPathExistenceAssertion;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.smartbear.ready.cmd.runner.pro.SoapUIProTestCaseRunner;


public class SoapUIIntegrator {
	static String filePath = new File("maven.properties").getAbsolutePath();
	private static String apiFilePath = Utility.getMavenProperties("SoapUIFilePath");;

	static SoapUIProTestCaseRunner  runner = new SoapUIProTestCaseRunner ();


		public static void executeSoapUITC(String soapuiProjectPath)   {

			TestAssertionRegistry.getInstance().addAssertion(new JsonPathContentAssertion.Factory());
			TestAssertionRegistry.getInstance().addAssertion(new JsonPathCountAssertion.Factory());
			TestAssertionRegistry.getInstance().addAssertion(new JsonPathExistenceAssertion.Factory());
			String readyAPIProjectPath=apiFilePath+soapuiProjectPath;
		

			String prop=Utility.getMavenProperties("EndPoint");
			runner.setProjectFile(readyAPIProjectPath);
			settingProperty(readyAPIProjectPath);
			runner.setJUnitReport(true);
			//runner.setEndpoint(prop);
			//runner.setOutputFolder("test-report");
			runner.setOutputFolder("test-report-new");
		
			runner.setIgnoreError(true);
			try {
				runner.run();
			} catch (Exception e) {
				System.out.println("Not able to excute Soapui Project "+e.getMessage());

			}  
		}

		private static void settingProperty(String readyAPIProjectPath) {
			String prop=Utility.getMavenProperties("EndPoint");
			WsdlProjectPro project = new WsdlProjectPro(readyAPIProjectPath);
			List<WsdlTestSuite> testSuites = project.getTestSuiteList();
			for (TestSuite suite : testSuites) {

				suite.setPropertyValue("ServerHost",prop);

			}
		}
		/**
		 * Method to run a ReadyApi project. It can run Standard SOAP UI, as well
		 * Composite project
		 * 
		 * @param projectFile
		 *            project file name if the project is Standard SOAP UI project,
		 *            project's folder path if the project is composite
		 * @throws Exception
		 */
		public static void runProject(String projectFile) throws Exception {
			WsdlProjectPro project = new WsdlProjectPro(apiFilePath + projectFile);
			PropertiesMap prop=new PropertiesMap();
			prop.put("ServerHost",Utility.getMavenProperties("EndPoint"));
			List<WsdlTestSuite> testSuites = project.getTestSuiteList();
			for (TestSuite suite : testSuites) {
					List<TestCase> testCases = (List<TestCase>) suite.getTestCaseList();
					for (TestCase testCase : testCases) {
						System.out.println("Running SoapUI test [" + testCase.getName() + "]");
						TestRunner runner2 = testCase.run(new PropertiesMap(), false);
						
					}
				}
			
		}

		/**
		 * Method to run a specific suite. It can run suite from Standard SOAP UI,
		 * as well Composite project
		 * 
		 * @param projectFile
		 *            project file name if the project is Standard SOAP UI project,
		 *            project's folder path if the project is composite
		 * @param suiteName
		 *            suite name
		 * @throws Exception
		 */
		public static void runTestsuiteByName(String projectFile, String suiteName) throws Exception {
			WsdlProjectPro project = new WsdlProjectPro(apiFilePath + projectFile);
			List<WsdlTestSuite> testSuites = project.getTestSuiteList();
			for (TestSuite suite : testSuites) {
				if (suite.getName().trim().equalsIgnoreCase(suiteName)) {
					suite.run(new PropertiesMap(), true);
				}
			}
		}

		/**
		 * Method to run a specific test case. It can run specific test case from
		 * Standard SOAP UI, as well Composite project
		 * 
		 * @param projectFile
		 *            project file name if the project is Standard SOAP UI project,
		 *            project's folder path if the project is composite
		 * @param tcId
		 *            test case id to run
		 * @throws Exception
		 */
		public static void runTestCaseById(String projectFile, String tcId) throws Exception {
			WsdlProjectPro project = new WsdlProjectPro(apiFilePath + projectFile);
			List<WsdlTestSuite> testSuites = project.getTestSuiteList();
			for (TestSuite suite : testSuites) {
				suite.run(new PropertiesMap(), true);
				List<TestCase> testCases = (List<TestCase>) suite.getTestCaseList();
				for (TestCase testCase : testCases) {
					if (testCase.getName().split("-")[0].trim().equalsIgnoreCase(tcId)) {
						testCase.run(new PropertiesMap(), false);
					}
				}
			}
		}

		/**
		 * Method to run a ReadyApi project sequentially. It can run Standard SOAP
		 * UI, as well Composite project
		 * 
		 * @param projectFile
		 *            project file name if the project is Standard SOAP UI project,
		 *            project's folder path if the project is composite
		 * @return 
		 * @throws Exception
		 */
		public static List<String> getAllTestCases(String projectFile)  {
			List<String> testCase=new ArrayList<String>();
			WsdlProjectPro project = new WsdlProjectPro(apiFilePath + projectFile);
			List<WsdlTestSuite> testSuites = project.getTestSuiteList();
			for (TestSuite suite : testSuites) {
				List<? extends TestCase> testCases =suite.getTestCaseList();
				for (TestCase tc : testCases) {
					testCase.add(tc.getName());
				}
			}

			return testCase;
		}


	




}
