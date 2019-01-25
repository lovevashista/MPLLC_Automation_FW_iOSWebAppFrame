package MobileProgrammingLLC.PageLibraries;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Flipkart_MobilePDP {
	public Flipkart_MobilePDP(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="(//div[@class='_1lkA0W']/span)[1]") WebElement ProductNameOnPDP;
	public WebElement getProductNameOnPDP() {
		return ProductNameOnPDP;
	}
	
	@FindBy(xpath="//div[@class='_1G9wwo _3r3t-v']") WebElement AddToCartBtn;
	public WebElement getAddToCartBtn() {
		return AddToCartBtn;
	}
	
	@FindBy(xpath="//div[@class='_3zajI2']/a[4]") WebElement CartIcon;
	public WebElement getCartIcon() {
		return CartIcon;
	}
	
	@FindBy(xpath="//div[text()='Product Details']") WebElement ProductDetailsLnk;
	public WebElement getProductDetailsLnk() {
		return ProductDetailsLnk;
	}
}
