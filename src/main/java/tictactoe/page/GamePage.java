package tictactoe.page;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

import tictactoe.base.Logic;

public class GamePage extends Logic {
	public static WebDriver driver;
	WebDriverWait wait;
	public int rounds = 1;

	@FindBy(xpath = "//input[@title='Search']")
	public WebElement searchBox;

	@FindBy(xpath = "//g-raised-button//*[name()='svg' and @aria-label='X']")
	public WebElement optionX;

	@FindBy(xpath = "//g-raised-button//*[name()='svg' and @aria-label='O']")
	public WebElement optionO;

	@FindBy(xpath = "//*[text()='Game Over']")
	public WebElement gameOverTxt;

	@FindBy(xpath = "//g-menu")
	public WebElement menu;

	@FindBy(xpath = "//g-dropdown-menu-button-caption")
	public WebElement dropDownLevel;

	@FindBy(xpath = "//*[text()='Restart game']")
	public WebElement restartGame;

	@FindBy(xpath = "//*[text()='Start game or select player']")
	public WebElement selectPlayerTxt;

	@BeforeSuite
	public void driverSetup() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
		driver.get("https://www.google.com");
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
	}

	public void launchGame() {
		wait.until(ExpectedConditions.visibilityOf(searchBox));
		searchBox.sendKeys("play tic tac toe" + Keys.ENTER);
	}

	public void setDifficulty(String level) {
		if (!dropDownLevel.getText().equals(level)) {
			wait.until(ExpectedConditions.visibilityOf(dropDownLevel)).click();
			wait.until(ExpectedConditions.visibilityOf(menu));
			driver.findElement(By.xpath("//*[text()='" + level + "']")).click();
		}
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public void play() {
		for (int i = 0; i < rounds; i++) {
			while (!gameOverTxt.isDisplayed()) {
				Move bestMove = findBestMove(getboard());
				System.out.printf("ROW: %d COL: %d\n\n", bestMove.row, bestMove.col);
				driver.findElement(By.xpath("//td[@data-row=" + bestMove.row + " and @data-col=" + bestMove.col + "]"))
						.click();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Restarting the game");
			restartGame.click();
			wait.until(ExpectedConditions.visibilityOf(selectPlayerTxt));
			
		}
	}

	public void setXorO(char XorO) {
		switch (XorO) {
		case 'x':
			player = 'x';
			opponent = 'o';
			optionX.click();
			break;
		case 'o':
			player = 'o';
			opponent = 'x';
			optionO.click();
			break;
		}
	}

	protected static char[][] getboard() {
		char board[][] = new char[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!driver
						.findElement(By.xpath("//td[@data-row=" + i + " and @data-col=" + j
								+ "]//*[name()='svg' and @aria-label='X']"))
						.getAttribute("style").contains("display: none")) {
					board[i][j] = 'x';
				} else if (!driver
						.findElement(By.xpath("//td[@data-row=" + i + " and @data-col=" + j
								+ "]//*[name()='svg' and @aria-label='O']"))
						.getAttribute("style").contains("display: none")) {
					board[i][j] = 'o';
				} else {
					board[i][j] = '_';
				}
			}
		}
		return board;
	}
}
