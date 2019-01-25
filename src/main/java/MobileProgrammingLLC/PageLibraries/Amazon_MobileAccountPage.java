package MobileProgrammingLLC.PageLibraries;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class Amazon_MobileAccountPage {
	public Amazon_MobileAccountPage(IOSDriver<IOSElement> driver) {
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="ap_email_login") WebElement UserNameTF;
	public WebElement getUserNameTF() {
		return UserNameTF;
	}
	
	@FindBy(xpath="//input[@aria-labelledby='continue-announce']") WebElement ContinueBtn;
	public WebElement getContinueBtn() {
		return ContinueBtn;
	}
	
	@FindBy(css="span.a-list-item") WebElement errorMsg;
	public WebElement geterrorMsg() {
		return errorMsg;
	}
}
