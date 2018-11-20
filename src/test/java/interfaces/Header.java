package interfaces;

import org.openqa.selenium.By;

import actions.Element;

/**
 * Store elements on Header such as Logo, Research, My Anibis and Sub Menu 
 * @author khoivo
 *
 */
public class Header {
	public static Element imgLogo() {
		return new Element(By.id("logo"));
	}
	
	public static Element lnkMyListings() {
		return new Element(By.xpath("//*[@id='ctl00_Header1_ctlHeaderActionBar_ctlMemberNavigation_divMyListings']/a"));
	}

}
