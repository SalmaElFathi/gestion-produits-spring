package fst.ac.ma.gestion_produits;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("selenium")
class ProduitSeleniumTest {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testAjouterProduit() {
        driver.get(baseUrl);
        
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));
        
        driver.findElement(By.id("designation")).sendKeys("Laptop");
        driver.findElement(By.id("quantite")).sendKeys("10");
        driver.findElement(By.id("prix")).sendKeys("5000");
        
        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        assertTrue(driver.getPageSource().contains("Laptop"));
    }

    @Test
    void testModifierProduit() {
        driver.get(baseUrl);
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));
        driver.findElement(By.id("designation")).sendKeys("Souris");
        driver.findElement(By.id("quantite")).sendKeys("5");
        driver.findElement(By.id("prix")).sendKeys("100");
        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        
        driver.findElement(By.className("btn-edit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editModal")));
        
        WebElement editDesignation = driver.findElement(By.id("editDesignation"));
        editDesignation.clear();
        editDesignation.sendKeys("Souris Gaming");
        
        driver.findElement(By.xpath("//form[@id='editForm']//button[@type='submit']")).click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        assertTrue(driver.getPageSource().contains("Souris Gaming"));
    }

    @Test
    void testSupprimerProduit() {
        driver.get(baseUrl);
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));
        driver.findElement(By.id("designation")).sendKeys("Clavier");
        driver.findElement(By.id("quantite")).sendKeys("3");
        driver.findElement(By.id("prix")).sendKeys("200");
        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        
        driver.findElement(By.className("btn-delete")).click();
        
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        assertFalse(driver.getPageSource().contains("Clavier"));
    }

    @Test
    void testAfficherListeProduits() {
        driver.get(baseUrl);
        
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));
        driver.findElement(By.id("designation")).sendKeys("Écran");
        driver.findElement(By.id("quantite")).sendKeys("2");
        driver.findElement(By.id("prix")).sendKeys("1500");
        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));
        driver.findElement(By.id("designation")).sendKeys("Webcam");
        driver.findElement(By.id("quantite")).sendKeys("7");
        driver.findElement(By.id("prix")).sendKeys("300");
        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("tbody")));
        
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Écran"));
        assertTrue(pageSource.contains("Webcam"));
    }
}