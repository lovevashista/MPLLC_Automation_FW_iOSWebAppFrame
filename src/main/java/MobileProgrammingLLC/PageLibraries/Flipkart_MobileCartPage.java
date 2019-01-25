package MobileProgrammingLLC.PageLibraries;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Flipkart_MobileCartPage {
	public Flipkart_MobileCartPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//div[@data-aid='Product_TITLE']") WebElement ProductTitleOnCartPage;
	public WebElement getProductTitleOnCartPage() {
		return ProductTitleOnCartPage;
	}
	
}
