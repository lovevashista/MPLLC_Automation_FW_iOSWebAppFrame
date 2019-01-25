package MobileProgrammingLLC.PageLibraries;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Flipkart_MobileHomePage {
	public Flipkart_MobileHomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css="div.DARYYB") WebElement SignInOverlayCloseIcon;
	public WebElement getSignInOverlayCloseIcon() {
		return SignInOverlayCloseIcon;
	}
	
	@FindBy(css="input.lzMbl5") WebElement SearchTF;
	public WebElement getSearchTF() {
		return SearchTF;
	}
	
	@FindBy(xpath="(//img[@class='_1V3kw-'])[4]") WebElement VoiceSearchCloseIcon;
	public WebElement getVoiceSearchCloseIcon() {
		return VoiceSearchCloseIcon;
	}
	
	@FindBy(xpath="//input[@id='input-search']") WebElement AfterVoiceSearchTF;
	public WebElement getAfterVoiceSearchTF() {
		return AfterVoiceSearchTF;
	}
}
