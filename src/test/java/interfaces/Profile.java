package interfaces;

import org.openqa.selenium.By;

import actions.Element;

public class Profile {
	public static Element lblDisplayName() {
		return new Element(By.xpath("//h3[contains(text(),'Nom d’utilisateur')]/"
				+ "ancestor::div[@class='layout--columns data-section data-section-with-separator-bottom layout--columns-three']"
				+ "//strong[@class='semibold']"));
	}
	
	public static Element lblEmail() {
		return new Element(By.xpath("//h3[contains(text(),'Adresse e-mail')]/"
				+ "ancestor::div[@class='layout--columns data-section data-section-with-separator-bottom layout--columns-three']"
				+ "//strong[@class='semibold']"));
	}
}
