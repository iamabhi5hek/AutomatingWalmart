package com.ddframework.mini.project;



import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import com.ddframework.base.BaseUi;


public class walmartTest extends BaseUi{

	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testOne() throws Exception{
		
		//selecting browser as chrome
		invokeBrowser();
		
		//opening url of the website
		openURL(sheet1.getRow(1).getCell(1).getStringCellValue());
	
////		//first iteration of selecting an item		
		onestep("hamburgerIcon_Id","selectedItem_XPath" ,"selectedItem_XPath", "item_XPath", "added_XPath", 1,"addtocart_XPath");
//		
//		
//		//second iteration of selecting an item and viewing the cart
		onestep("hamburgerIcon_Id","selectedItem_XPath", "selectedItem_XPath", "item_XPath", "added_XPath", 2,"addtocart_XPath");
		seeingCart("viewcart_ClassName");
		
		
		//third iteration of selecting an item and viewing the cart
		onestep("hamburgerIcon_Id", "selectedItem_XPath","selectedItem_XPath", "item_XPath", "added_XPath", 3,"addtocart_XPath");
		seeingCart("viewcart_ClassName");
		value();

		quitBrowser();
		
		writeExcel();
	}

}

