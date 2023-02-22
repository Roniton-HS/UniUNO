package de.uniks.pmws2223.uno;

import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

import static org.junit.Assert.*;

public class GameTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        App app = new App();
        app.start(stage);
    }

    @Test
    public void run() {
        assertEquals("UNO - Setup", stage.getTitle());
        clickOn("#nameField");
        write("Bob");
        clickOn("#plusButton");
        clickOn("#plusButton");
        clickOn("#seedBox");
        verifyThat("#botCount", hasText("3"));

        clickOn("#startButton");

        assertEquals("UNO - Ingame", stage.getTitle());
        sleep(500);

        clickOn("#cardButton2");
        verifyThat("#cardButton-1", hasText("+2"));
        sleep(1000);
        verifyThat("#bot0CardCount", hasText("Cards: 9"));
        sleep(1000);
        verifyThat("#bot1CardCount", hasText("Cards: 6"));
        sleep(2000);
        verifyThat("#bot2CardCount", hasText("Cards: 6"));
        sleep(500);

        clickOn("#cardButton3");
        verifyThat("#cardButton-1", hasText("+2"));
        verifyThat("#bot0CardCount", hasText("Cards: 11"));
        sleep(3000);
        verifyThat("#bot1CardCount", hasText("Cards: 5"));
        sleep(2000);
        verifyThat("#bot2CardCount", hasText("Cards: 5"));
        sleep(500);

        clickOn("#cardButton3");
        verifyThat("#cardButton-1", hasText("2"));
        sleep(3000);
        verifyThat("#bot0CardCount", hasText("Cards: 10"));
        sleep(3000);
        verifyThat("#bot1CardCount", hasText("Cards: 6"));
        sleep(2000);
        verifyThat("#bot2CardCount", hasText("Cards: 4"));
    }
}
