package com.tarladalal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.tarladalal.utils.DriverManager;
import com.tarladalal.utils.ExcelManager;

public class ParseRecipes {
	
	public String url="https://www.tarladalal.com/"; 
		
	public void startParsing()
	{
		WebDriver driver = DriverManager.getDriver();
		driver.get(url);
		driver.manage().window().maximize();
		
		//To click Recipe A to Z button
		driver.findElement(By.xpath("/html/body/div/form/div[3]/div[1]/div[2]/a[5]")).click();
		
		List<String> recipelink = new ArrayList<String>();
		
		//Walk through A to Z
		for (char k='V'; k<='Y'; k++ )
		{
		
			String temprecipeurl = "https://www.tarladalal.com/RecipeAtoZ.aspx?pageindex=" + 1 + "&beginswith=" +k;
			driver.get(temprecipeurl);
					
		//Walk through page 1 to page n 
		
		driver.findElement(By.xpath("/html/body/div[2]/form/div[3]/div[2]/div/div[1]/div[2]"));
		List<WebElement> pages = driver.findElements(By.className("respglink"));
		
		//To find no of pages under the alphabet
		
		int p=1;
		
		if (pages.size()!=0)
		{
		String lastpage = pages.get(pages.size()-1).getText();
		p = Integer.valueOf(lastpage);
		System.out.println(p);
		}
		
		// to go in to each page under the alphabet
		
		for(int j=1; j<=p; j++) 
		{
			
		    String recipeurl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith="+k+"&pageindex=" + j;
			driver.get(recipeurl);

		
		List<WebElement> recipes = driver.findElements(By.className("rcc_recipename"));
		System.out.println(recipes.size());
	    //to get recipe link in each page under the alphabet 
		for (int i=0;i < recipes.size(); i ++)
		{
		String link = recipes.get(i).findElement(By.tagName("a")).getAttribute("href");
		System.out.println(link);
		recipelink.add(link);
		//recipies.get(i).click();
			//driver.navigate().back();
		}
		
		}
		}
		
		int NumberOfRecipesFound = recipelink.size();
		System.out.println("Number Of Recipes Found :" + NumberOfRecipesFound);
		int failureCount = 0;
		//To parse recipe and save it in memory
		for(int m=0; m<recipelink.size(); m++)
		{
			//driver.get(recipelink.get(m));
			try {
				parseRecipe(recipelink.get(m));
			}
			catch (Exception e)
			{
				System.out.println("Fail to parse : " + recipelink.get(m));
				failureCount++; 
				e.printStackTrace();
			}
			
				}
		System.out.println("Number Of Recipes Failed :" + failureCount );
		ExcelManager.writeFile();
		driver.close();
		 
	}
	
	
	//to avoid nested try catch for WebElement 
	public WebElement getElement(String xpath)
	{
		try {
			return DriverManager.getDriver().findElement(By.xpath(xpath));
		}
		catch (Exception e){
			
			return null;
			
		}
	}
	
	
	public void parseRecipe(String url)
	{
		
		
		WebDriver driver = DriverManager.getDriver();
		driver.get(url);
		String recipeids[] = url.split("-");
		String recipeid = recipeids[recipeids.length-1];
		String recipename = driver.findElement(By.xpath("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[1]/div[2]/h1/span/span")).getText();
		String foodcategory = "Veg";
		String targetedmorbidity = "To Do";
		String tag = "";
		String allergy = "";
		String preptime = "" ; 
		String cooktime = "" ;
		String method = "";
		String nutrientvalue = "" ; 
		List<WebElement> ingredientlist = driver.findElement(By.id("rcpinglist")).findElements(By.tagName("a"));
		String ingredient = " ";
		for (int n=0; n<ingredientlist.size(); n++)
		{
			if (n!=0) {
				ingredient = ingredient + ",";
				
			}
			ingredient = ingredient + ingredientlist.get(n).findElement(By.tagName("span")).getText();
			//System.out.println(ingredient);
		}
		
		//int moveIndex=0;
		try {
				WebElement tempElement = getElement("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[9]/div/section/p[2]/time[1]");
				if (tempElement==null)
				{
					//moveIndex=1;
					tempElement=getElement("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[10]/div/section/p[2]/time[1]");
				}
				preptime=tempElement.getText();
			}
			catch(Exception e)
			{
				System.out.println("Error Parsing preptime : " + url);
			}

		
		try {
			
			WebElement tempElement = getElement("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[9]/div/section/p[2]/time[2]");
			if (tempElement==null)
			{
				tempElement=getElement("html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[10]/div/section/p[2]/time[2]");
			}
			cooktime=tempElement.getText();
		}
		catch (Exception e)
		{
			System.out.println("Error Parsing cooktime : " + url);
		}
		
		try {
			method=driver.findElement(By.id("recipe_small_steps")).getText();
			
		}
		catch (Exception e)
		{
			System.out.println("Error Parsing method : " + url);
		}
		
			
			try{
				
				WebElement tempElement = getElement("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[19]/div/div[1]/span/table");
				if (tempElement==null)
				{
					tempElement=getElement("/html/body/div[2]/form/div[1]/div[2]/div/main/div/div[1]/article/div[2]/div[20]/div/div[1]/span/table");
				}
				nutrientvalue=tempElement.getText();
														 
				     
			}
			catch (Exception e)
			{
				System.out.println("Error Parsing nutrient value : " + url);
			}
			
			
			List<WebElement> taglist = driver.findElement(By.id("recipe_tags")).findElements(By.tagName("a"));
			
			
			try{
				
				for (int n=0; n<taglist.size(); n++)
				{
					if (n!=0) {
						tag = tag + ",";
						
					}
					tag = tag + taglist.get(n).findElement(By.tagName("span")).getText();
					//System.out.println(ingredient);
				}
				
			}
			catch (Exception e)
			{
				System.out.println("Error Parsing tag : " + url);
			}
		
			/*
			 * System.out.println("recipeid : " + recipeid );
			 * System.out.println("recipename : " + recipename );
			 * System.out.println("foodcategory : " + foodcategory );
			 * System.out.println("Ingredient :" + ingredient);
			 * System.out.println("preperation time : " + preptime);
			 * System.out.println("Cooking time : " + cooktime);
			 * System.out.println("preperation method : " + method);
			 * System.out.println("Nutrient Value : " + nutrientvalue );
			 * System.out.println("Targeted Morbidity : " + targetedmorbidity );
			 * System.out.println("Recipe URL : " + url );
			 */
		
		Object[] row = new Object[13];
		
		row[0] = recipeid;
		row[1] = recipename; 
		row[2] = "NA";
		row[3] = foodcategory; 
		row[4] = ingredient;
		row[5] = preptime;
		row[6] = cooktime;
		row[7] = method;
		row[8] = nutrientvalue;
		row[9] = targetedmorbidity;
		row[10] = url;
		row[11] = tag;
		row[12] = allergy; 
		
		ExcelManager.setData("completelist", row);
					
	}
	
	public void eliminateRecipes()
	{
		List<String> eliminatelist = ExcelManager.getColumn("Hypothyroidism_eliminate", 1);
		List<String[]> completelist = ExcelManager.getRows("completelist");	
		List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<completelist.size();i++)
		{
			String[] recipe = completelist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			boolean found=false;
			for(int j=0;j<ingredient.size();j++)
			{
				if(found)
				{
					break;
				}
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<eliminatelist.size();k++)
				{
					String eliminate=eliminatelist.get(k).toLowerCase();
					if(currentingredient.contains(eliminate))
					{
						found=true;
						break;
					}
				}
			}
			if(found)
			{
				System.out.println("Skiping Recipe : " + recipe[1] + ":" + ingredient);
			}
			else
			{
				recipe[9]="Hypothyroidism";
				filteredlist.add(recipe);
				recipe[3] = getFoodCategory(recipe[11]);
				recipe[2] = getRecipeCategory(recipe[11]);
				ExcelManager.setData("Hypothyroidism", recipe);
			}
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done writing Hypothyroidism");
	}
	
	public void toAadd()
	{
		List<String> toaddlist = ExcelManager.getColumn("Hypothyroidism_toadd_ingredient", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Hypothyroidism");	
				
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String toaddingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<toaddlist.size();k++)
				{
					String toadd=toaddlist.get(k).toLowerCase();
					if(currentingredient.contains(toadd))
					{
						toaddingredient = toaddingredient + "," + toadd; 
						
					}
					
				}
			}
				if (!toaddingredient.equals(""))		
				{
				recipe[12]=toaddingredient.substring(1); 
				
				ExcelManager.setData("Hypothyroidism_toadd_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For To add");
	}
	
	public void addAllergy()
	{
		List<String> allergylist = ExcelManager.getColumn("allergy", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Hypothyroidism");	
		//List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String allergyingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<allergylist.size();k++)
				{
					String allergy=allergylist.get(k).toLowerCase();
					if(currentingredient.contains(allergy))
					{
						allergyingredient = allergyingredient + "," + allergy; 
						
					}
					
				}
			}
				if (!allergyingredient.equals(""))		
				{
				recipe[12]=allergyingredient.substring(1); 
				
				ExcelManager.setData("Hypothyroidism_allergy", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For Allergy");
	}
	
	public void pcoseliminateRecipes()
	{
		List<String> eliminatelist = ExcelManager.getColumn("PCOS_eliminate_ingredient", 1);
		List<String[]> completelist = ExcelManager.getRows("completelist");	
		List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<completelist.size();i++)
		{
			String[] recipe = completelist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			boolean found=false;
			for(int j=0;j<ingredient.size();j++)
			{
				if(found)
				{
					break;
				}
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<eliminatelist.size();k++)
				{
					String eliminate=eliminatelist.get(k).toLowerCase();
					if(currentingredient.contains(eliminate))
					{
						found=true;
						break;
					}
				}
			}
			if(found)
			{
				System.out.println("Skiping Recipe : " + recipe[1] + ":" + ingredient);
			}
			else
			{
				recipe[9]="PCOS";
				filteredlist.add(recipe);
				recipe[3] = getFoodCategory(recipe[11]);
				recipe[2] = getRecipeCategory(recipe[11]);
				ExcelManager.setData("PCOS", recipe);
			}
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done writing PCOS");
	}
	
	public void toAddPCOS()
	{
		List<String> toaddlist = ExcelManager.getColumn("PCOS_toadd_ingredient", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("PCOS");	
				
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String toaddingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<toaddlist.size();k++)
				{
					String toadd=toaddlist.get(k).toLowerCase();
					if(currentingredient.contains(toadd))
					{
						toaddingredient = toaddingredient + "," + toadd; 
						
					}
					
				}
			}
				if (!toaddingredient.equals(""))		
				{
				recipe[12]=toaddingredient.substring(1); 
				
				ExcelManager.setData("PCOS_toadd_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For To add");
	}

	public void pcosAllergy()
	{
		List<String> allergylist = ExcelManager.getColumn("allergy", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("PCOS");	
			
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String allergyingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<allergylist.size();k++)
				{
					String allergy=allergylist.get(k).toLowerCase();
					if(currentingredient.contains(allergy))
					{
						allergyingredient = allergyingredient + "," + allergy; 
						
					}
					
				}
			}
				if (!allergyingredient.equals(""))		
				{
				recipe[12]=allergyingredient.substring(1); 
				
				ExcelManager.setData("PCOS_allergy_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For Allergy");
	}

	public void diabtesEliminateRecipes()
	{
		List<String> eliminatelist = ExcelManager.getColumn("Diabetes_eliminate_ingredient", 1);
		List<String[]> completelist = ExcelManager.getRows("completelist");	
		List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<completelist.size();i++)
		{
			String[] recipe = completelist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			boolean found=false;
			for(int j=0;j<ingredient.size();j++)
			{
				if(found)
				{
					break;
				}
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<eliminatelist.size();k++)
				{
					String eliminate=eliminatelist.get(k).toLowerCase();
					if(currentingredient.contains(eliminate))
					{
						found=true;
						break;
					}
				}
			}
			if(found)
			{
				System.out.println("Skiping Recipe : " + recipe[1] + ":" + ingredient);
			}
			else
			{
				recipe[9]="Diabetes";
				filteredlist.add(recipe);
				recipe[3] = getFoodCategory(recipe[11]);
				recipe[2] = getRecipeCategory(recipe[11]);
				ExcelManager.setData("Diabetes", recipe);
			}
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done writing Diabetes");
	}
	
	public void toAaddDiabetes()
	{
		List<String> toaddlist = ExcelManager.getColumn("Diabetes_toadd_ingredient", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Diabetes");	
				
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String toaddingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<toaddlist.size();k++)
				{
					String toadd=toaddlist.get(k).toLowerCase();
					if(currentingredient.contains(toadd))
					{
						toaddingredient = toaddingredient + "," + toadd; 
						
					}
					
				}
			}
				if (!toaddingredient.equals(""))		
				{
				recipe[12]=toaddingredient.substring(1); 
				
				ExcelManager.setData("Diabetes_toadd_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For To add");
	}
	
	public void addAllergyDiabetes()
	{
		List<String> allergylist = ExcelManager.getColumn("allergy", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Diabetes");	
		//List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String allergyingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<allergylist.size();k++)
				{
					String allergy=allergylist.get(k).toLowerCase();
					if(currentingredient.contains(allergy))
					{
						allergyingredient = allergyingredient + "," + allergy; 
						
					}
					
				}
			}
				if (!allergyingredient.equals(""))		
				{
				recipe[12]=allergyingredient.substring(1); 
				
				ExcelManager.setData("Diabetes_allergy_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For Allergy");
	}
	public void hypertensionEliminateRecipes()
	{
		List<String> eliminatelist = ExcelManager.getColumn("Hypertension_eliminate_ing", 1);
		List<String[]> completelist = ExcelManager.getRows("completelist");	
		List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<completelist.size();i++)
		{
			String[] recipe = completelist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			boolean found=false;
			for(int j=0;j<ingredient.size();j++)
			{
				if(found)
				{
					break;
				}
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<eliminatelist.size();k++)
				{
					String eliminate=eliminatelist.get(k).toLowerCase();
					if(currentingredient.contains(eliminate))
					{
						found=true;
						break;
					}
				}
			}
			if(found)
			{
				System.out.println("Skiping Recipe : " + recipe[1] + ":" + ingredient);
			}
			else
			{
				recipe[9]="Hypertension";
				filteredlist.add(recipe);
				recipe[3] = getFoodCategory(recipe[11]);
				recipe[2] = getRecipeCategory(recipe[11]);
				ExcelManager.setData("Hypertension", recipe);
			}
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done writing Hypertension");
	}
	
	public void toAaddHypertension()
	{
		List<String> toaddlist = ExcelManager.getColumn("Hypertension_toadd_ingredient", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Hypertension");	
				
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String toaddingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<toaddlist.size();k++)
				{
					String toadd=toaddlist.get(k).toLowerCase();
					if(currentingredient.contains(toadd))
					{
						toaddingredient = toaddingredient + "," + toadd; 
						
					}
					
				}
			}
				if (!toaddingredient.equals(""))		
				{
				recipe[12]=toaddingredient.substring(1); 
				
				ExcelManager.setData("Hypertension_toadd_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For To add");
	}
	
	public void addAllergyHypertension()
	{
		List<String> allergylist = ExcelManager.getColumn("allergy", 1);
		List<String[]> fliteredlist = ExcelManager.getRows("Hypertension");	
		//List<String[]> filteredlist = new ArrayList<String[]> ();
		
		for(int i=0; i<fliteredlist.size();i++)
		{
			String[] recipe = fliteredlist.get(i);
			List<String> ingredient = new ArrayList<String>( Arrays.asList(recipe[4].split(",")));
		
			String allergyingredient = "";
			for(int j=0;j<ingredient.size();j++)
			{
				String currentingredient=ingredient.get(j).toLowerCase();
				for(int k=0;k<allergylist.size();k++)
				{
					String allergy=allergylist.get(k).toLowerCase();
					if(currentingredient.contains(allergy))
					{
						allergyingredient = allergyingredient + "," + allergy; 
						
					}
					
				}
			}
				if (!allergyingredient.equals(""))		
				{
				recipe[12]=allergyingredient.substring(1); 
				
				ExcelManager.setData("Hypertension_allergy_recipe", recipe);
				}
			
		}
		
		
		ExcelManager.writeFile();
		System.out.println("Done Writing For Allergy");
	}

	public String getFoodCategory(String tagstring)
	{
		Map<String, String> categorymap = ExcelManager.getMap("foodcategory");
	
		List<String> tags = new ArrayList<String>( Arrays.asList(tagstring.split(",")));
		
			String foodcategory = "Vegetarian";
			
			for(int j=0;j<tags.size();j++)
			{
				String currenttag=tags.get(j).toLowerCase();
				// keySet() returns a set view of all keys
			    // for-each loop access each key from the view
			    for(String key: categorymap.keySet())
			    {
			    	List<String> value = new ArrayList<String>( Arrays.asList(categorymap.get(key).split(",")));
			    	for (int k=0; k<value.size(); k++)
			    	{
			    		String currentvalue=value.get(k).toLowerCase();
						if(currenttag.contains(currentvalue))
						{
							return key;						
						}
			    	}
			    	
					
				}
			
		}
			return foodcategory; 
		
		}
	
	public String getRecipeCategory(String tagstring)
	{
		Map<String, String> categorymap = ExcelManager.getMap("recipecategory");
	
		List<String> tags = new ArrayList<String>( Arrays.asList(tagstring.split(",")));
		
			String recipecategory = "Lunch";
			
			for(int j=0;j<tags.size();j++)
			{
				String currenttag=tags.get(j).toLowerCase();
				// keySet() returns a set view of all keys
			    // for-each loop access each key from the view
			    for(String key: categorymap.keySet())
			    {
			    	List<String> value = new ArrayList<String>( Arrays.asList(categorymap.get(key).split(",")));
			    	for (int k=0; k<value.size(); k++)
			    	{
			    		String currentvalue=value.get(k).toLowerCase();
						if(currenttag.contains(currentvalue))
						{
							return key;						
						}
			    	}
			    	
					
				}
			
		}
			return recipecategory; 
		
		}
	
	
	public static void main(String args[])
	
	{
	ParseRecipes parser = new ParseRecipes(); 
	//To scrape the webpage

	//parser.startParsing(); // to parse all values
	
	//String url = "https://www.tarladalal.com/yam-and-spinach-pulusu-4355r";
	//parser.parseRecipe(url);
	//parser.eliminateRecipes(); //filter for morbidity
	//parser.addAllergy(); //to add allergy
	//parser.toAadd();
	//parser.pcoseliminateRecipes();
	//parser.toAddPCOS();
	//parser.pcosAllergy();
	//parser.diabtesEliminateRecipes();
	//parser.toAaddDiabetes();
	//parser.addAllergyDiabetes();
	parser.hypertensionEliminateRecipes();
	parser.toAaddHypertension();
	parser.addAllergyHypertension();
	 
	}
	
	

}
