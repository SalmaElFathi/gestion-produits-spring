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
import io.github.bonigarcia.wdm.WebDriverManager;
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
        WebDriverManager.chromedriver().setup();
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

        // Attendre que la page se charge
        wait.until(d -> {
            try {
                driver.get(baseUrl);
                return driver.getTitle() != null && !driver.getTitle().isEmpty();
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
        driver.findElement(By.className("btn-add")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addModal")));

        driver.findElement(By.id("designation")).sendKeys("Laptop");
        driver.findElement(By.id("quantite")).sendKeys("10");
        driver.findElement(By.id("prix")).sendKeys("5000");

        driver.findElement(By.cssSelector("#addForm button[type='submit']")).click();

        // Vérifier que le produit apparaît dans le tableau
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("tbody"), "Laptop"));
    }

    @Test
    @Order(2)
    void testModifierProduit() {
        // Cliquer sur le bouton modifier du premier produit
        WebElement btnEdit = driver.findElement(By.className("btn-edit"));
        btnEdit.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editModal")));

        WebElement editDesignation = driver.findElement(By.id("editDesignation"));
        editDesignation.clear();
        editDesignation.sendKeys("Laptop Pro");

        driver.findElement(By.cssSelector("#editForm button[type='submit']")).click();

        // Vérifier que le nom du produit a changé
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("tbody"), "Laptop Pro"));
    }

    @Test
    @Order(3)
    void testSupprimerProduit() {
        // Cliquer sur le bouton supprimer du premier produit
        WebElement btnDelete = driver.findElement(By.className("btn-delete"));
        btnDelete.click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Vérifier que le produit a disparu
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//td[contains(text(),'Laptop Pro')]")));
    }

    @Test
    @Order(4)
    void testAfficherListeProduits() {
        String pageSource = driver.getPageSource();
        assertNotNull(pageSource);
        assertTrue(pageSource.contains("<tbody>")); // Vérifie que le tableau est présent
    }
}
