package interfaces;

import org.openqa.selenium.By;

import actions.Element;

/**
 * Store elements on Login screen
 * @author khoivo
 *
 */
public class Login {

	public static Element txtEmail() {
		return new Element(By.xpath("//div[@data-related-to='ctl00_phlContent_ctlEmailValidationBox_txtEmail']"));
	}
	
	public static Element txtPassword() {
		return new Element(By.id("ctl00_phlContent_txtPassword"));
	}
	
	public static Element btnContinue() {
		return new Element(By.id("ctl00_phlContent_ctlEmailValidationBox_lbtContinue"));
	}
	
	public static Element btnLogin() {
		return new Element(By.id("ctl00_phlContent_btnLogin"));
	}
	
}
