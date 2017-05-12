package continuum.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.runners.model.TestClass;

public class Utility {
	static String filePath = new File("maven.properties").getAbsolutePath();
	public static String  getMavenProperties(String key){

		String value=null;
		Properties mavenProps = new Properties();
		String prop = System.getProperty("jenkins");
		//  System.out.println("Jenkins value"+prop);
		if(prop==null)
		{
			//System.out.println("Executing on local machine");
		}
		else if(prop.equalsIgnoreCase("true"))
			value=System.getProperty(key);

		if(value==null)
		{ 	 
			System.out.println("File path :"+filePath);
			InputStream inStream = TestClass.class.getResourceAsStream(filePath);
			try {
				mavenProps.load(inStream);
			} catch (IOException e) {
				System.out.println("***Not able to load propeties from maven");
				e.printStackTrace();
			}
			value= mavenProps.getProperty(key);
		}

		return value;
	}
	


}
