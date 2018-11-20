package utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Driver {
	public static WebDriver instanceWeb;
	public static String browser;
	
	/**
	 * This action is used to delete all cookies of active domain.
	 */
	public static void deleteCookies() {
		if (instanceWeb == null)
			return;
		instanceWeb.manage().deleteAllCookies();
	}

	/**
	 * This action is used to switch to another window.
	 * 
	 * @param windowName
	 * @throws InterruptedException
	 */
	public static void switchToWindow(String windowName) throws InterruptedException {
		if (instanceWeb == null) 
			return;
		Thread.sleep(5000);
		WebDriver window = null;
		Iterator<String> windowIterator = instanceWeb.getWindowHandles().iterator();
		while (windowIterator.hasNext()) {
			String windowHandle = windowIterator.next();
			
			window = instanceWeb.switchTo().window(windowHandle);
			if (window.getTitle().contains(windowName)) {
				break;
			}
		}
	}
	
	/**
	 * This method is used to initialize web driver.
	 * 
	 * @param node
	 *            : address of slave machine (ex: http://<ip address>:
	 *            <port>/wd/hub) in selenium grid
	 * @param browser
	 *            : Browser to execute test on. Will be retrieved from TestNG
	 * @return WebDriver
	 */
	public static void initializeWeb(String node, String browser) {

		WebDriver driver = null;

		if (node.equalsIgnoreCase("localhost")) {
			if ("firefox".equalsIgnoreCase(browser)) {
				FirefoxProfile p = new FirefoxProfile();
				p.setEnableNativeEvents(true);
				driver = new FirefoxDriver(p);
			} else if ("safari".equalsIgnoreCase(browser)) {
				SafariOptions ops = new SafariOptions();
				ops.setUseCleanSession(true);
				driver = new SafariDriver(ops);
			} else if ("ie".equalsIgnoreCase(browser)) {

				DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
				cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				File file = new File("C:/selenium-tools/IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

				driver = new InternetExplorerDriver(cap);
			} else if ("chrome".equalsIgnoreCase(browser)) {
				System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver");

				ChromeOptions options = new ChromeOptions();
				Map<String, Integer> prefs = new HashMap<String, Integer>();
				prefs.put("profile.managed_default_content_settings.notifications", 1);
				options.setExperimentalOption("prefs", prefs);

				driver = new ChromeDriver(options);
			}

		} else {
			if (browser.equalsIgnoreCase("firefox")) {
				TestRunner.log(" Executing on FireFox");
				DesiredCapabilities cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				try {
					driver = new RemoteWebDriver(new URL(node), cap);
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				}
			} else if (browser.equalsIgnoreCase("chrome")) {
				TestRunner.log(" Executing on CHROME");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");

				ChromeOptions options = new ChromeOptions();
				Map<String, Integer> prefs = new HashMap<String, Integer>();
				prefs.put("profile.default_content_settings.cookies", 1);
				options.setExperimentalOption("prefs", prefs);
				cap.setCapability(ChromeOptions.CAPABILITY, options);

				try {
					driver = new RemoteWebDriver(new URL(node), cap);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} else if (browser.equalsIgnoreCase("ie")) {
				TestRunner.log(" Executing on IE");
				DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
				cap.setBrowserName("ie");
				cap.setCapability("requireWindowFocus", true);
				try {
					driver = new RemoteWebDriver(new URL(node), cap);
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				}
			} else {
				throw new IllegalArgumentException("The Browser Type is Undefined");
			}

		}

		Driver.browser = browser;
		instanceWeb = driver;
		
	}
	
	/**
	 * (Web + Mobile) This action is used to quit driver.
	 */
	public static void close() {
		if (instanceWeb != null)
			instanceWeb.close();
	}
	
	/**
	 * (Web) This action is used to set wait time for web element.
	 * @param seconds
	 */
	public static void setWebElementWait(int seconds) {
		// Set wait time for element.
		instanceWeb.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	/**
	 * (Web) This action is used to set wait time for web page. 
	 * @param seconds
	 */
	public static void setPageWait(int seconds) {
		// Set wait time for page load.
		instanceWeb.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
	}
	
}
