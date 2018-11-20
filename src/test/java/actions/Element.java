package actions;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.Driver;
import utilities.TestRunner;

public class Element implements WebElement {

	private final WebElement element;
	private final By by;

    public Element(final By by) {
    	this.by = by;
    	WebElement element = null;
		try {
			element = Driver.instanceWeb.findElement(by);
		} catch (NoSuchElementException e) {
			TestRunner.log("NoSuchElementException: " + by + " is not found.");
		} catch (NullPointerException e) {
			TestRunner.log("NullPointerException: " + by + " is null.");
			
		} catch (org.openqa.selenium.StaleElementReferenceException e) {
			TestRunner.log("StaleElementReferenceException: " + by + " is not attached to the page document.");
			
		}
        this.element = element;
    }
    
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return element.getScreenshotAs(target);
	}

	@Override
	public void click() {
		TestRunner.log("click on element (" + this.by.toString() + ")");
		element.click();
	}
	
	@Override
	public void submit() {
		element.submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		element.sendKeys(keysToSend);
		
	}

	@Override
	public void clear() {
		element.clear();
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return element.getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return element.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}

	@Override
	public String getText() {
		return element.getText();
	}

	@Override
	public <T extends WebElement> List<T> findElements(By by) {
		return element.findElements(by);
	}

	@Override
	public <T extends WebElement> T findElement(By by) {
		return element.findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		return element.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return element.getLocation();
	}

	@Override
	public Dimension getSize() {
		return element.getSize();
	}

	@Override
	public Rectangle getRect() {
		return element.getRect();
	}

	@Override
	public String getCssValue(String propertyName) {
		return element.getCssValue(propertyName);
	}
	
	/*************************************************************
	 * IMMEDIATE ACTIONS
	 *************************************************************/
	
	/**
	 * Clear and enter new text to textbox
	 * @param text
	 */
	public void enter(String text) {
		TestRunner.log("enter text to element (" + this.by.toString() + ")");
		clear();
		sendKeys(text);
	}
	
	public void waitVisible(int seconds) {
		TestRunner.log("wait for element (" + this.by.toString() + ") to be visible in " + seconds + " seconds");
		WebDriverWait wait = new WebDriverWait(Driver.instanceWeb, seconds);
		wait.until(ExpectedConditions.visibilityOfElementLocated(this.by));
	}
	
	public void waitClickable(int seconds) {
		TestRunner.log("wait for element (" + this.by.toString() + ") to be clickable in " + seconds + " seconds");
		WebDriverWait wait = new WebDriverWait(Driver.instanceWeb, seconds);
		wait.until(ExpectedConditions.elementToBeClickable(this.by));
	}
	
	/**
	 * Check if element has exactly the same specific text
	 * @param text
	 */
	public void checkText(String text) {
		Assertion.checkEquals(element.getText(), text, "check if text of element (" + this.by.toString() + ") is \"" + text + "\")");
	}
	
	/**
	 * Check if element contains text
	 * @param text
	 */
	public void checkContains(String text) {
		Assertion.checkEquals(element.getText(), text, "check if element (" + this.by.toString() + ") contains \"" + text + "\")");
	}
	
	/**
	 * Check if element exists
	 */
	public void checkExists() {
		Assertion.checkEquals(element.isDisplayed(), true, "check if element (" + this.by.toString() + ") exists.");
	}
	
	
	public void checkAttribute(String attribute, String expected) {
		String attValue = element.getAttribute(attribute);
		
		Assertion.checkEquals(attValue, expected, "check if element (" + this.by.toString() + ") has " + attribute + " is '" + expected + "'");
	}
}
