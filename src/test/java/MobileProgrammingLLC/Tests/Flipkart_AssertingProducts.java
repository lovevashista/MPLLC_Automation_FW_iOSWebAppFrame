package MobileProgrammingLLC.Tests;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import MobileProgrammingLLC.PageLibraries.Flipkart_MobileCartPage;
import MobileProgrammingLLC.PageLibraries.Flipkart_MobileDetailedPDP;
import MobileProgrammingLLC.PageLibraries.Flipkart_MobileHomePage;
import MobileProgrammingLLC.PageLibraries.Flipkart_MobilePDP;
import MobileProgrammingLLC.PageLibraries.Flipkart_MobilePLP;
import MobileProgrammingLLC.Resources.Base;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class Flipkart_AssertingProducts {
	IOSDriver<IOSElement> driver;
	Logger log = LogManager.getLogger(Flipkart_AssertingProducts.class.getName());
	Properties locators = new Properties();
	Properties config = new Properties();
	Properties data = new Properties();
	Flipkart_MobileHomePage mhp;
	Flipkart_MobilePLP mplp;
	Flipkart_MobilePDP mpdp;
	Flipkart_MobileCartPage mcp;
	Flipkart_MobileDetailedPDP mdpdp;
	Base b = new Base();
	String ProductNameOnPDP, ProductNameOnCartPage, ProductNameOnDetailsPage;
	
	@BeforeClass
	public void initConfigs() {
		locators = b.loadLocators();
		config = b.loadConfig();
		data = b.loadData();
		driver = b.createAppiumServerConnection(config.getProperty("ip"),config.getProperty("port"), config.getProperty("DeviceType"));
		mhp = new Flipkart_MobileHomePage(driver);
		mplp = new Flipkart_MobilePLP(driver);
		mpdp = new Flipkart_MobilePDP(driver);
		mcp = new Flipkart_MobileCartPage(driver);
		mdpdp = new Flipkart_MobileDetailedPDP(driver);
	}
	
	@Test(dataProvider="getData")
	public void validatingAddToCartFunctionality(String searchTerm) {
		b.navigateToSite(driver, config.getProperty("FlipkartWebSite"));
		
		if(searchTerm.equals("iphone")) {
			 b.flash(mhp.getSignInOverlayCloseIcon());
			 b.clickOn(mhp.getSignInOverlayCloseIcon()); 
		}
		
		b.waitFor(5);
		b.flash(mhp.getSearchTF());
		b.clickOn(mhp.getSearchTF());
		if(searchTerm.equals("iphone")) {
			/*
			 * if(mhp.getVoiceSearchCloseIcon().isDisplayed()) {
			 * 		b.waitFor(By.xpath(locators.getProperty("Flipkart_VoiceSearchCloseIcon")));
			 * }
			 */
			/*
			 * b.waitForSometime();
			 */
			/*
			 * b.flash(mhp.getVoiceSearchCloseIcon());
			 * b.clickOn(mhp.getVoiceSearchCloseIcon());
			 */
		}
		
		/*
		 * b.waitFor(By.xpath(locators.getProperty("Flipkart_AfterVoiceSearchTF")));
		 * b.flash(mhp.getAfterVoiceSearchTF());
		 * b.clickOn(mhp.getAfterVoiceSearchTF());
		 */
		
		b.flash(mhp.getAfterVoiceSearchTF());
		b.enterContentIntoAndSubmit(mhp.getAfterVoiceSearchTF(), searchTerm);
		
		b.waitFor(By.xpath(locators.getProperty("Flipkart_PLP_FirstResultLnk")));
		b.flash(mplp.getFirstResultLnk());
		b.clickOn(mplp.getFirstResultLnk());
		
		b.flash(mpdp.getProductNameOnPDP());
		ProductNameOnPDP = mpdp.getProductNameOnPDP().getText();
		
		b.clickOn(mpdp.getProductDetailsLnk());
		ProductNameOnDetailsPage = mdpdp.getProductNameOnDetailedPDP().getText();

		b.compareContent(ProductNameOnPDP, ProductNameOnDetailsPage);
	}
	
	@DataProvider
	public Object[][] getData() {
		Object[][] obj = new Object[2][1];
		
		obj[0][0] = "iphone";
		obj[1][0] = "laptop";
		
		return obj;
	}

	@AfterClass
	public void tearDown() {
		b.quitDriver(driver);
		b.killAppiumServer();
	}
}
