package MobileProgrammingLLC.PageLibraries;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Flipkart_MobilePLP {
	public Flipkart_MobilePLP(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
//	@FindBy(xpath="//div[@class='_1aUjpa']/div[2]/div/div/a/div/div/div[2]/div[1]/div/div") WebElement FirstResultLnk;
	@FindBy(xpath="//a[contains(@href,'dl.flipkart.com')]") WebElement FirstResultLnk;
	public WebElement getFirstResultLnk() {
		return FirstResultLnk;
	}
}
