package com.tarladalal.utils;

import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.sun.tools.javac.util.Assert;

public class DriverManager {

	public static WebDriver driver;

	public static WebDriver getDriver()
	{
		if(driver==null)
		{
			//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+ "\\Drivers\\chromedriver.exe");
			WebDriverManager.chromedriver().setup();
			ChromeOptions ops=new ChromeOptions();
			ops.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(ops);//; 
		}
		return driver;
	}
	
	public static WebDriver getDriver(String browser)
	{
		if(driver==null)
		{
			driver=getDriver(browser,false);
		}
		return driver;
	}
	public static WebDriver getDriver(String browser,boolean force)
	{
		MyLogger.error("Loading browser:"+browser);
		if (force || driver==null)
		{
			if(browser.equals("chrome"))
			{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+ "\\Drivers\\chromedriver.exe");
			//WebDriverManager.chromedriver().browserVersion("108.0.0").setup();
			ChromeOptions ops=new ChromeOptions();
			ops.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(ops);//; 
			}
		else if(browser.equals("firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();
		}
		else if(browser.equals("edge"))
		{
			MyLogger.error("Starting edge browser");
			WebDriverManager.edgedriver().forceDownload();
			WebDriverManager.edgedriver().setup();
		driver = new EdgeDriver();
		}
		else if(browser.equals("safari"))
		{
			WebDriverManager.safaridriver().setup();
		driver = new SafariDriver();
		}
		else
		{
			MyLogger.error("Unsupported browser:"+browser);
		}
		}
		return driver;
	}
	public static void close()
	{
		driver.close();
	}
}
