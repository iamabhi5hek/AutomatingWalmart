package com.ddframework.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseUi {

	public WebDriver driver;
	public Properties prop;
	public String browserName;
	public XSSFWorkbook wb;
	public XSSFSheet sheet1;
	static String Price;
	static String names;

	public void readExcel() throws Exception {
		File src = new File(System.getProperty("user.dir") + "\\Book1.xlsx");
		FileInputStream fis = new FileInputStream(src);
		wb = new XSSFWorkbook(fis);
		sheet1 = wb.getSheetAt(0);

		wb.close();
	}

	

	
	public void invokeBrowser() throws Exception {

		readExcel();

		browserName = sheet1.getRow(0).getCell(1).getStringCellValue();

		try {
			if (browserName.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browserName.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\geckodriver.exe");
				driver = new FirefoxDriver();

			} else {
				System.setProperty("webdriver.edge.driver",
						System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\msedgedriver.exe");
				driver = new EdgeDriver();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream file = new FileInputStream(System.getProperty("user.dir")
						+ "\\src\\test\\resources\\ObjectRepositiries\\Config.properties");
				prop.load(file);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	// getting website url
	public void openURL(String url) throws Exception
	{
		readExcel();
		driver.get(url);
	}

	// closing the browser
	public void tearDown() {
		driver.close();
	}

	// quitting the browser
	public void quitBrowser() {
		driver.quit();
	}

	// global function to click an element
	public void ElementClick(String xpathkey) 
	{
//		WebDriverWait wait=new WebDriverWait(driver,15);
//		wait.until(ExpectedConditions.elementToBeClickable(getElement(xpathkey)));
		getElement(xpathkey).click();
	}

	// global function for thread.sleep
	public void waitSome() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void scroll(String xpathkeylocator) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement ab = getElement(xpathkeylocator);
		js.executeScript("arguments[0].scrollIntoView(true)", ab);
	}

	// global function for mouse hover event
	public void mouseHover(String dropdownkey, String listitemkey) {
		WebElement electronicItem = getElement(dropdownkey);
		waitSome();
		Actions action = new Actions(driver);
		action.moveToElement(electronicItem).build().perform(); // since it is composite action so dont forget to use
																// build and perform

		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(getElement(listitemkey)));



		WebElement heater = getElement(listitemkey);
		heater.click();
		waitSome();

	}

	// global function for getting element based on locator used
	public WebElement getElement(String locatorKey) {
		WebElement element = null;

		try {

			if (locatorKey.endsWith("_Id")) {
				element = driver.findElement(By.id(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_ClassName")) {
				element = driver.findElement(By.className(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_Name")) {
				element = driver.findElement(By.name(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_XPath")) {
				element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		return element;
	}

	// global function for getting list of items and selecting one at a tome from it
	public void listOfProducts(String xpathkey, int product_no) {
		List<WebElement> list = driver.findElements(By.xpath(prop.getProperty(xpathkey)));
		list.get(product_no).click();
	}


	// global function to add a respective element
	public void onestep(String xpathkey, String scrolldown, String dropdownkey, String listitemkey, String item,
			int product, String addtocart) {
		ElementClick(xpathkey);
		scroll(scrolldown);
		mouseHover(dropdownkey, listitemkey);
		listOfProducts(item, product);
		itemName();
		ElementClick(addtocart);

		waitSome();
	}

	// global function to view the cart
	public void seeingCart(String xpathkey) {
		ElementClick(xpathkey);
	}
	
	
	
	// global function for getting value of products added
	public void value() {
		String price=sheet1.getRow(3).getCell(1).getStringCellValue();
		WebElement ab = driver.findElement(By.xpath(price));
		Price=ab.getText();
		System.out.println("price after selecting these elements is : " + Price);
	}

	

	public void itemName() {
		String Names=sheet1.getRow(2).getCell(1).getStringCellValue();
		WebElement name = driver.findElement(By.xpath(Names));
		names = name.getText();
		System.out.println("Name of the product you selected is : " + names);
	}
	
	
	public void writeExcel()  throws Exception {
		
		File src = new File(System.getProperty("user.dir") + "\\src\\TestData.xlsx");
		
		//Workbook workbook = WorkbookFactory.create(new FileInputStream(src));
				//
		XSSFWorkbook wb=new XSSFWorkbook();
		
		XSSFSheet sh=wb.createSheet("project");
		
		XSSFRow row1=sh.createRow((short)0);
		row1.createCell(0).setCellValue(Price);

		
		FileOutputStream fos=new FileOutputStream(src);
		
		wb.write(fos);
		wb.close();
	}
}
