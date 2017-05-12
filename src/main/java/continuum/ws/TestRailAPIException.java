/**
 * TestRail API binding for Java (API v2, available since TestRail 3.0)
 *
 * Learn more:
 *
 * http://docs.gurock.com/testrail-api2/start
 * http://docs.gurock.com/testrail-api2/accessing
 *
 * Copyright Gurock Software GmbH. See license.md for details.
 */

package continuum.ws;
 
/***************
 * Test rail exception
 *
 */

public class TestRailAPIException extends Exception
{
	public TestRailAPIException(String message)
	{
		super(message);
	}
}
