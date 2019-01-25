package MobileProgrammingLLC.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class Base {
	AppiumServiceBuilder builder;
	AppiumDriverLocalService service;
	DesiredCapabilities dc;
	IOSDriver<IOSElement> driver;
	String homeDir = System.getProperty("user.home");
	
	Properties configProp = new Properties();
	FileInputStream configFis = null;
	File configFile = new File("src/main/java/MobileProgrammingLLC/Resources");
	File configSrc = new File(configFile, "config.properties");
	Properties locProp = new Properties();
	FileInputStream locFis = null;
	File locFile = new File("src/main/java/MobileProgrammingLLC/Resources"); 
	File locSrc = new File(locFile, "locators.properties");
	Properties dataProp = new Properties();
	FileInputStream dataFis = null;
	File dataFile = new File("src/main/java/MobileProgrammingLLC/Resources");
	File dataSrc = new File(dataFile, "data.properties");
	
	Logger log = LogManager.getLogger(Base.class.getName());
	
	IOSTouchAction t;
	TapOptions tele;
	LongPressOptions lpele;
	PointOption<?> ptOp, start_ptOp, end_ptOp;
	JavascriptExecutor js;

	public void startAppiumServer(String ip, int port) {
		log.debug("Starting Appium Server...");
		try {
			builder = new AppiumServiceBuilder();
			builder.withIPAddress(ip);
			builder.usingPort(port);
			service = AppiumDriverLocalService.buildService(builder);
			service.start();
			Thread.sleep(7000L);
			log.info("Server successfully started.");
		}catch(InterruptedException e) {
			log.error("Sleep Interrupted.");
		}
	}
	
	public void setCapabilities(String deviceType) {
		log.debug("Setting Capabilities...");
		try {
			dc = new DesiredCapabilities();
			dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "IOS");
			dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.3");
			dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
			if(deviceType.equalsIgnoreCase("simulator")) {
				dc.setCapability(MobileCapabilityType.DEVICE_NAME, configProp.getProperty("DeviceName"));
			}else if(deviceType.equalsIgnoreCase("real")) {
				dc.setCapability(MobileCapabilityType.DEVICE_NAME, configProp.getProperty("DeviceName"));
			}
			log.info("Capabilities Successfully set.");
		}catch(Exception e) {
			log.error("Setting capabilities failed.");
		}
	}
	
	public void invokingBrowser() {
		log.debug("Invoking the Browser...");
		try {
			dc.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
			log.info("Browser successfully invoked.");
		}catch(Exception e) {
			log.error("Browser invoking failed.");
		}
	}
	
	public IOSDriver<IOSElement> createAppiumServerConnection(String ip, String port, String deviceType) {
		int p = Integer.parseInt(port);
		killAppiumServer();
		startAppiumServer(ip, p);
		setCapabilities(deviceType);
		invokingBrowser();
		log.debug("Creating connection with Appium Server...");
		try {
			driver = new IOSDriver<IOSElement>(new URL("http://"+ip+":"+port+"/wd/hub"), dc);
			log.info("Connection successfully created.");
		} catch (MalformedURLException e) {
			log.error("Connection with server failed.");
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}
	
	public  void captureScreen(String testCase, IOSDriver<IOSElement> driver) {
		File s = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Date d = new Date();
			File f = new File("screens");
			File src = new File(f,testCase+"_"+d.toString()+"_"+"ss.png");
			FileUtils.copyFile(s, src);
		} catch (IOException e) {
			log.error("Capturing Screenshot failed.");
		}
	}
	
	public void stopAppiumServer() {
		log.debug("Stopping Appium Server...");
		try {
			service.stop();
			log.info("Server successfully stopped.");
		}catch(Exception e) {
			log.error("Stopping Appium Server failed.");
		}
	}
	
	public void killAppiumServer() {
		try {
			String[] command = new String[] {"/usr/bin/killall","-KILL","node"};
			Process process = new ProcessBuilder(command).start();
			process.waitFor(2000,TimeUnit.MILLISECONDS);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public  Properties loadData() {
		log.debug("Loading data...");
		try {
			dataFis = new FileInputStream(dataSrc.getAbsolutePath());
			dataProp.load(dataFis);
			log.info("Data successfully loaded.");
		} catch (FileNotFoundException e) {
			log.error("Data file not found");
		}catch (IOException e) {
			log.error("Reading Data file failed.");
		}
		return dataProp;
	}
	
	public  Properties loadLocators() {
		log.debug("Loading locators...");
		try {
			locFis = new FileInputStream(locSrc.getAbsolutePath());
			locProp.load(locFis);
			log.info("Locators successfully loaded.");
		} catch (FileNotFoundException  e) {
			log.error("Locator file not found");
		} catch (IOException e1) {
			log.error("Reading Locator file failed.");
		}
		return locProp;
	}
	
	public  Properties loadConfig() {
		log.debug("Loading config file...");
		try {
			configFis = new FileInputStream(configSrc.getAbsolutePath());
			configProp.load(configFis);
			log.info("Config file successfully loaded.");
		} catch (FileNotFoundException e) {
			log.error("ERROR: Config file not found");
		} catch (IOException e) {
			log.error("ERROR: Reading Config file failed.");
		}
		return configProp;
	}
	
	public  void waitFor(By ele) {
		try {
			WebDriverWait w = new WebDriverWait(driver, 20);
			w.until(ExpectedConditions.presenceOfElementLocated(ele));
		}catch(Exception e) {
			log.error("Element having locator '"+ele+"'"+" could not be located.");
		}
	}
	
	public  void waitForTillVisibile(By ele) {
		try {
			WebDriverWait w = new WebDriverWait(driver, 20);
			w.until(ExpectedConditions.visibilityOfElementLocated(ele));
		}catch(Exception e) {
			log.error("Element having locator '"+ele+"'"+" could not be located.");
		}
	}
	
	public  void waitForSometime() {
		try {
			log.warn("Sleep feature used. Liberal usage not advised.");
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			log.error("Waiting Interrupted.");
		}
	}
	
	public  void waitForLongtime() {
		try {
			log.warn("Long Sleep feature used (only for exceptional scenarios). Liberal usage strictly discouraged.");
			Thread.sleep(30000L);
		} catch (InterruptedException e) {
			log.error("Waiting Interrupted.");
		}
	}
	
	public void waitFor(int seconds) {
		try {
			log.warn("Sleep feature used. Liberal usage not advised.");
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			log.error("Waiting Interrupted.");
		}
	}
	
	public void tapOn(WebElement element, IOSDriver<IOSElement> driver) {
		log.debug("Attempting to tap on element '"+element + "'...");
		
		try {
			t = new IOSTouchAction(driver);
			tele = TapOptions.tapOptions().withElement(ElementOption.element(element));
			t.tap(tele).perform();
			log.info("Successfully tapped on '" + element + "'");
		}catch(Exception e) {
			log.error("Tapping on the element failed.");
		}
	}
	
	public void longPressOn(IOSElement element, IOSDriver<IOSElement> driver) {
		log.debug("Attempting to long press on element '"+element + "'...");
		try {
			t = new IOSTouchAction(driver);
			lpele = LongPressOptions.longPressOptions().withElement(ElementOption.element(element));
			t.longPress(lpele).perform();
			log.info("Successfully long pressed on '"+ element + "'.");
		}catch(Exception e) {
			log.error("Long Pressing the element failed.");
		}
	}
	
	public void swipeUsingCoordinates(String stXOffset, String stYOffset, String enXOffset, String enYOffset) {
		log.info("Attempting to perform swipe gesture based on coordinates {'"+stXOffset+"', '" + stYOffset + "'}, {'"+enXOffset+"', '"+enYOffset+"'}...");
		try {
			int stXOff = Integer.parseInt(stXOffset);
			int stYOff = Integer.parseInt(stYOffset);
			int enXOff = Integer.parseInt(enXOffset);
			int enYOff = Integer.parseInt(enYOffset);
			start_ptOp = PointOption.point(stXOff, stYOff);
			end_ptOp = PointOption.point(enXOff, enYOff);
			t.longPress(start_ptOp).moveTo(end_ptOp).release().perform();
			log.info("Swipe Successful!");
		}catch(Exception e) {
			log.error("Swipe failed on coordinates {'"+stXOffset+"', '" + stYOffset + "'}, {'"+enXOffset+"', '"+enYOffset+"'}...");
		}
	}
	
	public void moveTo(IOSElement element, IOSDriver<IOSElement> driver, int xOffset, int yOffset) {
		log.debug("Attempting to move the element to '"+element + "'...");
		try {
			int x = element.getLocation().getX() + xOffset;
			int y = element.getLocation().getY() + yOffset;
			ptOp = PointOption.point(x, y);
			t.moveTo(ptOp).release().perform();
			log.info("Successfully moved the element to '"+ element + "'.");
		}catch(Exception e) {
			log.error("Moving to the element failed.");
		}
	}

	public void getAppContext(IOSDriver<IOSElement> driver) {
		log.debug("Fetching app context...");
		try {
			log.info("APP CONTEXT: " + driver.getContext());
		}catch(Exception e) {
			log.error("Fetching app context failed!");
		}
	}
	
	public void getDeviceOrientation(IOSDriver<IOSElement> driver) {
		log.debug("Getting Device Orientation...");
		try {
			log.info("DEVICE ORIENTATION: " + driver.getOrientation());
		}catch(Exception e) {
			log.error("Getting device orientation failed!");
		}
	}
	
	public void hideKeypad(IOSDriver<IOSElement> driver) {
		log.debug("Hiding Device Keypad...");
		try {
			driver.hideKeyboard();
			log.info("Keypad hidden successfully.");
		}catch(Exception e) {
			log.error("Hiding Keyboard unsuccessful.");
		}
	}
	
	public void navigateOnHomeScreen(IOSDriver<IOSElement> driver) {
		log.debug("Navigating on the home screen...");
		try {
			driver.runAppInBackground(Duration.ofSeconds(-1));
			log.info("Home Key pressed successfully.");
		}catch(Exception e) {
			log.error("Navigation on home screen unsuccessful!");
		}
	}
	
	public void compareContent(IOSElement ele, String expected) {
		log.debug("Attempting to compare content returned by \"" + ele + "\" from \"" + expected + "\" ...");
		try {
			Assert.assertEquals(ele.getText(), expected);
			log.info("Comparison Successful!");
		}catch(Exception e) {
			log.error("Comparison Failed! Expected was '"+expected+"' but actual is '"+ele.getText()+"'");
		}
	}
	
	public void compareContentUsingClass(List<IOSElement> className, String index, String expected) {
		log.info("Attempting to compare content returned by \"" + className + "\" at index '"+ index + "' from \"" + expected + "\" ...");
		int i = Integer.parseInt(index);
		try {
			Assert.assertEquals(className.get(i).getText(), expected);
			log.info("Comparison Successful!");
		}catch(Exception e) {
			log.error("Comparison Failed! Expected was '"+expected+"' but actual is '"+className.get(i).getText()+"'");
		}
	}
	
	public void enterContentInto(IOSElement ele, String content) {
		log.debug("Attempting to send \""+ content + "\" to element \"" + ele + "\"...");
		try {
			ele.sendKeys(content);
			log.info("Content \"" + content + "\" entered successfully into element \"" + ele + "\"");
		}catch(Exception e) {
			log.error("Entering content into element '"+ele+"' failed.");
		}
	}
	
	public void isDisplayedOnPage(IOSElement ele) {
		log.debug("Attempting to locate " + ele + " on the page...");
		try {
			if(ele.isDisplayed()) {
				log.info(ele + " is displayed on the page successfully.");
			}
		}catch(Exception e){
			log.error("Presence of " + ele + " on the page failed.");
		}
	}
	
	public void quitDriver(IOSDriver<IOSElement> driver) {
		log.debug("Attempting to quit the driver...");
		try {
			driver.quit();
			driver = null;
			log.info("Driver successfully quitted.");
		}catch(Exception e) {
			log.error("Quitting driver failed!");
		}
	}
	
	public void swipeInDirection(String direction, IOSDriver<IOSElement> driver){
		log.debug("Attempting to swipe the screen in '" + direction + "' direction...");
		try {
			js = (JavascriptExecutor) driver;
			HashMap<String, String> scrollObject = new HashMap<String, String>();
			scrollObject.put("direction", direction);
			js.executeScript("mobile: swipe", scrollObject);
		}catch(Exception e) {
			log.error("Swiping in '" + direction + "' direction failed.");
		}
	}
	
	public  void flash(WebElement ele) {
		String bgColor = ele.getCssValue("backgroundColor");
		for(int i=0; i<2; i++) {
			changeColor(ele, "rgb(173,255,47)");
			changeColor(ele, bgColor);
		}
	}
	
	public  void changeColor(WebElement ele, String bgColor) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].style.backgroundColor = '" + bgColor + "'", ele);
		try {
			Thread.sleep(100);
		}catch(InterruptedException e) {
			log.error("Flashing Element Interrupted.");
		}
	}
	
	public void clickOn(WebElement ele) {
		try {
			log.debug("Attempting to click on element: '"+ele+"'...");
			ele.click();
		}catch(Exception e) {
			log.error("Clicking element '"+ele+"' failed.");
		}
		log.info("Element '"+ele+"' successfully clicked.");
	}
	
	public void clickByActions(WebElement ele) {
		try {
			log.debug("Attempting to click on element: '"+ele+"' uisng Actions...");
			Actions a = new Actions(driver);
			a.moveToElement(ele).click().build().perform();
			//ele.click();
		}catch(Exception e) {
			log.error("Clicking element '"+ele+"' by Actions failed.");
		}
		log.info("Element '"+ele+"' successfully clicked.");
	}
	
	public void enterContentInto(WebElement ele, String content) {
		try {
			log.debug("Attempting to send \""+ content + "\" to element \"" + ele + "\"...");
			ele.sendKeys(content);
		}catch(Exception e) {
			log.error("Entering content into element '"+ele+"' failed.");
		}
		log.info("Content \"" + content + "\" entered successfully into element \"" + ele + "\"");
	}
	
	public void enterContentIntoAndSubmit(WebElement ele, String content) {
		try {
			log.debug("Attempting to send \""+ content + "\" to element \"" + ele + "\"...");
			ele.sendKeys(content, Keys.ENTER);
		}catch(Exception e) {
			log.error("Entering content into element '"+ele+"' failed.");
		}
		log.info("Content \"" + content + "\" entered successfully into element \"" + ele + "\" and successfully submitted.");
	}
	
	public void compareContent(WebElement ele, String expected) {
		try {
			log.debug("Attempting to compare content returned by \"" + ele + "\" from \"" + expected + "\" ...");
			Assert.assertEquals(ele.getText(), expected);
		}catch(Exception e) {
			log.error("Comparison Failed! Expected was '"+expected+"' but actual is '"+ele.getText()+"'");
		}
		log.info("Comparison Successful!");
	}
	
	public void compareContent(WebElement e1, WebElement e2) {
		try {
			log.debug("Attempting to compare content returned by \"" + e1 + "\" from \"" + e2 + "\" ...");
			Assert.assertEquals(e1.getText(), e2.getText());
		}catch(Exception e) {
			log.error("Comparison Failed! Expected was '"+e2.getText()+"' but actual is '"+e1.getText()+"'");
		}
		log.info("Comparison Successful!");
	}
	
	public void compareContent(String s1, String s2) {
		try {
			log.debug("Attempting to compare content \"" + s1 + "\" from \"" + s2 + "\" ...");
			Assert.assertEquals(s1, s2);
		}catch(Exception e) {
			log.error("Comparison Failed! Expected was '"+s2+"' but actual is '"+s1+"'");
		}
		log.info("Comparison Successful!");
	}
	
	public void navigateToSite(WebDriver driver, String URL) {
		try {
			log.debug("Attempting to get navigated to URL: \"" + URL + "\"...");
			driver.get(URL);
		}catch(Exception e) {
			log.error("Navigation to site \""+ URL + "\" failed.");
		}
		log.info("Navigation to site \"" + URL + "\" successful.");
	}
	
	public void deleteAllCookies(IOSDriver<IOSElement> driver) {
		try {
			log.debug("Attempting to delete all cookies...");
			driver.manage().deleteAllCookies();
		}catch(Exception e) {
			log.error("Deleting all cookies failed.");
		}
		log.info("All cookies successfully deleted.");
	}
	
	public void isDisplayedOnPage(WebElement ele) {
		log.debug("Attempting to locate " + ele + " on the page...");
		try {
			if(ele.isDisplayed()) {
				log.info(ele + " is displayed on the page successfully.");
			}
		}catch(Exception e){
			log.error("Presence of " + ele + " on the page failed.");
		}
	}
	
	public void quitDriver(WebDriver driver) {
		try {
			log.debug("Attempting to quit the driver...");
			driver.quit();
			driver = null;
		}catch(Exception e) {
			log.error("Quitting driver failed!");
		}
		log.info("Driver successfully quitted.");
	}
	
	public void scrollPage(IOSDriver<IOSElement> driver) {
		log.debug("Trying to scroll the page...");
		try {
			js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollBy(0,3000)");
			log.info("Page Scrolled Successfully.");
		}catch(Exception e) {
			log.error("Page scrolling failed.");
		}
	}
	
}
