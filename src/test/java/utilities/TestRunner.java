package utilities;

import java.lang.reflect.Method;

public class TestRunner {
	public static Method method;

	public static void log(String message) {
		System.out.println(message);
	}

	/**
	 * This action is used to print out message start 'running testing'.
	 * 
	 * @param method
	 */
	public static void startMethod() {
		String name = method.getName().replaceAll("_", " ");
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		log("TEST STARTED: " + name);
		log("+====================================================================================================================================+");
	}

	/**
	 * This action is used to print out message 'finish testing'.
	 * 
	 * @param method
	 */
	public static void endMethod() {
		String name = method.getName().replaceAll("_", " ");
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		log("+====================================================================================================================================+");
		log("TEST FINISHED: " + name);
		log(".....................................................................................................................................");
	}

	public static void setUp(Class<?> testNGclass) {
		int count = 0;

        testNGclass.getClass();
        Class<?> className = null;
		try {
			className = Class.forName(testNGclass.getName());
			 Method[] methods = className.getMethods();

		        for(int i=0; i< methods.length; i++)
		        {
		            if(methods[i].isAnnotationPresent(org.testng.annotations.Test.class))
		            {
		                count++;
		            }
		        }

		        log("RUN STARTED: " + testNGclass.getName() + ": " + count + " tests");
				log(".....................................................................................................................................");
				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void tearDown(Class<?> testNGclass) {
		
		log("RUN FINISHED: " + testNGclass.getName());
		log(".....................................................................................................................................");
	}
}
