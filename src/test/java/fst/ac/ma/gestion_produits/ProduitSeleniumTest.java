package fst.ac.ma.gestion_produits;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProduitSeleniumTest {

    @LocalServerPort
    private int port;
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080"
        );

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        baseUrl = "http://localhost:" + port;

        wait.until(d -> {
            try {
                driver.get(baseUrl);
                return driver.getTitle() != null;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testAjouterProduit() {
        driver.findElement(By.className("btn-ajouter")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));

        driver.findElement(By.id("designation")).sendKeys("Laptop");
        driver.findElement(By.id("quantite")).sendKeys("10");
        driver.findElement(By.id("prix")).sendKeys("5000");

        driver.findElement(By.xpath("//form[@id='addForm']//button[@type='submit']")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("tbody"), "Laptop"));
    }

    @Test
    @Order(2)
    void testModifierProduit() {
        driver.findElement(By.className("btn-edit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editModal")));

        WebElement editDesignation = driver.findElement(By.id("editDesignation"));
        editDesignation.clear();
        editDesignation.sendKeys("Laptop Pro");

        driver.findElement(By.xpath("//form[@id='editForm']//button[@type='submit']")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("tbody"), "Laptop Pro"));
    }

    @Test
    @Order(3)
    void testSupprimerProduit() {
        driver.findElement(By.className("btn-delete")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//td[contains(text(),'Laptop Pro')]")));
    }

    @Test
    @Order(4)
    void testAfficherListeProduits() {
        String pageSource = driver.getPageSource();
        assertNotNull(pageSource);
    }
}
