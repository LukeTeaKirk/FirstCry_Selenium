
import net.bytebuddy.description.type.TypeDescription;
import org.junit.jupiter.api.Order;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Element;
import javax.sql.rowset.BaseRowSet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.ErrorCodes.TIMEOUT;

public class FCVendorTest {
    public static ArrayList<String> tabs2;
    public static WebDriver browser;
    public static String ID;
    public static String ConfigURL;
    public static String OrderID;
    public static String Weight;
    public static String BagSize;
    public static void main(String[] args){
        //InputCache();
        Input();
        CheckDatabase();
        //SetBrowserDriver();
        //Login();
        //CookieSave();
        //CookieLoad();
        //Login();
        //intermed("http://vendor.firstcry.com/dashboard.aspx", browser);
        //openConfig(OrderID);
        //detectFormType();
        //looper();
    }
    public static void looper(){
        System.out.println(1);
        Input();
        System.out.println(2);
        openConfig(OrderID);
        detectFormType();
        looper();
    }
    public static void CheckCookieValid(){
        try{
            waitForUrl("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx", browser);
        }catch (Exception E){
            InputCache();
            main(null);
        }
    }
    public static void InputCache(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter SessionID:");
        String CookieID[]= new String[3];
        CookieID[1] = myObj.nextLine();
        System.out.println("Enter .ASPXAUTH");
        CookieID[2] = myObj.nextLine();
        String strline;
        File file = new File("Cookies.data");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader Buffreader = new BufferedReader(fileReader);
            int x=1;
            String name;
            String value;
            String domain;
            String path;
            String expiry;
            String val;
            String SessionIDFinal[] = new String[5];
            while((strline=Buffreader.readLine())!=null){
                    StringTokenizer token = new StringTokenizer(strline,";");
                    name = token.nextToken();
                    value = token.nextToken();
                    domain = token.nextToken();
                    path = token.nextToken();
                    expiry = null;
                    val = "false";
                    SessionIDFinal[x] = name + ";" + CookieID[x] + ";" + domain + ";" + path + ";" + expiry + ";" + val;
                    System.out.println(SessionIDFinal[x]);
                    System.out.println(x);
                    x = x + 1;
            }
            new FileOutputStream("Cookies.data").close();
            FileWriter fw = new FileWriter(file);
            fw.write(SessionIDFinal[1] + "\n" + SessionIDFinal[2]);
            fw.flush();
            fw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void SetBrowserDriver(){
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-print-preview");
        browser = new ChromeDriver(options);
    }
    public static void Input(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter ID:");
        ID = myObj.nextLine();
        if (ID.equals("0")){
            System.out.println("Ending Program");
            System.exit(0);
        }
        OrderID = "W" + ID;
    }
    public static void CheckDatabase(){
        try {
            File file = new File("Database.txt");
            FileReader fileReader = null;
            fileReader = new FileReader(file);
            BufferedReader Buffreader = new BufferedReader(fileReader);
            String strline;
            while((strline=Buffreader.readLine())!=null){
                StringTokenizer token = new StringTokenizer(strline,";");
                while(token.hasMoreTokens())
                {
                    String OrderID2 = token.nextToken();
                    if(OrderID2.equals(ID))
                    {
                        OrderID = "W" + OrderID2;
                        Weight = token.nextToken();
                        BagSize = token.nextToken();

                    }else {
                        token.nextToken();
                        token.nextToken();
                    };
                    System.out.println(OrderID);
                    System.out.println(Weight);
                    System.out.println(BagSize);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void Login(){
        browser.get("http://vendor.firstcry.com/");
        WebElement username=browser.findElement(By.xpath("//input[@name='Login1$UserName']"));
        username.sendKeys("**************");
        WebElement password=browser.findElement(By.xpath("//input[@name='Login1$Password']"));
        password.sendKeys("********");
    }
    public static void intermed(String url, WebDriver browser) {
        waitForUrl(url, browser);
        browser.get("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx");
        waitForUrl("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx", browser);
    }
    public static void waitForUrl(String url, WebDriver browser){
        new WebDriverWait(browser, 20).until(ExpectedConditions.urlToBe(url));
    }
    public static void openConfig(String OrderID){
        System.out.println(3);
        browser.get("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx");
        CheckCookieValid();
        System.out.println(4);
        WebElement ID = browser.findElement(By.xpath("//a[contains(@href, '" + OrderID + "')]"));
        System.out.println(5);
        ConfigURL = ID.getAttribute("href");
        System.out.println("URL:" + ConfigURL);
        ID.click();
        System.out.println(6);
    }
    public static void detectFormType(){
        tabs2 = new ArrayList<String> (browser.getWindowHandles());
        browser.switchTo().window(tabs2.get(1));
        String URL = browser.getCurrentUrl();
        if(URL.contains("B2B")){
            setValues2();
        }
        else{
            setValues1();
        }
    }
    public static void setValues2(){
        ReturnDefault();
    }
    public static void setValues1(){
        int x = 0;
        boolean present;
        present = true;
        waitForUrl(ConfigURL, browser);
        System.out.println("URL arrived");
        waitForElement("gvPOItems_cbxForShipping_0");
        System.out.println("Element arrived");
        while(present){
            String y = Integer.toString(x);
            try {
                browser.findElement(By.id("gvPOItems_cbxForShipping_" + y));
                present = true;
                WebElement TickBox = browser.findElement(By.id("gvPOItems_cbxForShipping_" + y));
                Select OptionBox = new Select(browser.findElement(By.id("gvPOItems_ddlStatus_" + y)));
                OptionBox.selectByVisibleText("Accepted");
                TickBox.click();
                x= x+1;
            } catch (NoSuchElementException e) {
                present = false;
            }

        }
        Select OptionBox2 = new Select(browser.findElement((By.id("ddlPOs"))));
        OptionBox2.selectByVisibleText("New Shipment");
        WebElement SendItemButton = browser.findElement(By.id("btnCreatePO"));
        SendItemButton.click();
        String URL = browser.getCurrentUrl();
        String WPID = OrderID + "1"; //URL.substring(URL.length() - 1);
        String ElementID = WPID + "1";

        waitForElement("SCCS" + ElementID + "_txtActualWeight");
        WebElement WeightText = browser.findElement(By.id("SCCS" + ElementID + "_txtActualWeight"));
        WeightText.sendKeys(Keys.BACK_SPACE + "1");
        Select SelectBox = new Select( browser.findElement(By.id("SCCS" + ElementID + "_ddlBoxType")));
        SelectBox.selectByVisibleText("Bag2 - Medium");
        WebElement SaveButton = browser.findElement(By.id("btnSave"));
        SaveButton.click();
        waitForUrl("http://vendor.firstcry.com/orderworkflow/DSConfigureOrderV1.aspx?pid=" + OrderID + "&wpid=" + WPID + "1", browser);
        browser.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
        WebElement link = browser.findElement(By.linkText("Print Invoice"));
        link.click();
        browser.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        tabs2 = new ArrayList<String> (browser.getWindowHandles());
        browser.switchTo().window(tabs2.get(1));
        try {
            PrintPage();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        ReturnDefault();
    }
    public static void PrintPage() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_P);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_P);
        /*Set<String> windowHandles = browser.getWindowHandles();
        if (!windowHandles.isEmpty()) {
            browser.switchTo().window((String) windowHandles.toArray()[windowHandles.size() - 1]);
        }
        Select mdselect = new Select(browser.findElement(By.className("md-select")));
        mdselect.selectByVisibleText("Custom");
        WebElement inputtext = browser.findElement(By.id("input"));
        inputtext.clear();
        inputtext.sendKeys("1");
        WebElement printButton = browser.findElement(By.className("action-button"));
        printButton.click();*/
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_2);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        //robot.keyPress(KeyEvent.VK_ESCAPE);
        //robot.keyRelease(KeyEvent.VK_ESCAPE);
        ReturnDefault();
    }
    public static void waitForElement(String id){
        WebDriverWait wait = new WebDriverWait(browser, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }
    public static void VerifyResults(){

    }
    public static void ReturnDefault(){
        String originalHandle = browser.getWindowHandle();

        //Do something to open new tabs

        for(String handle : browser.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                browser.switchTo().window(handle);
                browser.close();
            }
        }

        browser.switchTo().window(originalHandle);
        browser.get("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx");
        waitForUrl("http://vendor.firstcry.com/orderworkflow/orderlistv1.aspx", browser);
    }
    public static void CookieSave(){
        waitForUrl("http://vendor.firstcry.com/dashboard.aspx", browser);
        File file = new File("Cookies.data");
        try
        {
            // Delete old file if exists
            file.delete();
            file.createNewFile();
            FileWriter fileWrite = new FileWriter(file);
            BufferedWriter Bwrite = new BufferedWriter(fileWrite);
            // loop for getting the cookie information

            // loop for getting the cookie information
            for(Cookie ck : browser.manage().getCookies())
            {
                Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));
                Bwrite.newLine();
            }
            Bwrite.close();
            System.out.println("Cookie Saved");
            fileWrite.close();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void  CookieLoad(){
        try{
            browser.manage().deleteAllCookies();
            File file = new File("Cookies.data");
            FileReader fileReader = new FileReader(file);
            BufferedReader Buffreader = new BufferedReader(fileReader);
            String strline;
            while((strline=Buffreader.readLine())!=null){
                StringTokenizer token = new StringTokenizer(strline,";");
                while(token.hasMoreTokens()){
                    String name = token.nextToken();
                    String value = token.nextToken();
                    String domain = token.nextToken();
                    String path = token.nextToken();
                    java.util.Date expiry = null;
                    String val;
                    if(!(val=token.nextToken()).equals("null"))
                    {
                        expiry = new java.util.Date(val);
                    }
                    Boolean isSecure = new Boolean(token.nextToken()).
                            booleanValue();
                    Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);
                    System.out.println(ck);
                    browser.manage().addCookie(ck);
                    System.out.println("cookie added");
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
