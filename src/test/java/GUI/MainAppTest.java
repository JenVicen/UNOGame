package GUI;

import GUI.Controller.MainController;

import com.jfoenix.assets.JFoenixResources;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.testfx.framework.junit5.Start;

@Tag("ui")
@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class MainAppTest {
	@FXMLViewFlowContext
	private ViewFlowContext flowContext;

	@Start
	private void start(Stage stage) throws Exception {
		Flow flow = new Flow(MainController.class);
		DefaultFlowContainer container = new DefaultFlowContainer();
		flowContext = new ViewFlowContext();
		flowContext.register("Stage", stage);
		flow.createHandler(flowContext).start(container);

		double height = 625.0;
		double width = 1000.0;

		Scene scene = new Scene(container.getView(), width, height);
		final ObservableList<String> stylesheets = scene.getStylesheets();
		stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
				JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
				MainApp.class.getResource("/css/uno-dark.css").toExternalForm());

		MainApp.setPrimaryStage(stage);

		stage.setTitle("UNO Game");
		stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/logo.png")));
		stage.setScene(scene);
		stage.show();
	}

	@Test
	@Order(1)
	public void server_button_should_contain_text(FxRobot robot) {
		Assertions.assertThat(robot.lookup("#serverButton").queryAs(Button.class)).hasText("Start Server");
	}

	@Test
	@Order(2)
	public void player_name_should_change(FxRobot robot) {
		robot.doubleClickOn("#playerName");
		robot.write("Test Name");
		Assertions.assertThat(robot.lookup("#playerName").queryAs(TextField.class)).hasText("Player Name");
	}

	@Test
	@Order(3)
	public void cannot_join_without_server_started(FxRobot robot) {
		robot.doubleClickOn("#playerName");
		robot.write("Test Name");
		robot.clickOn("#joinButton");
		Assertions.assertThat(robot.lookup("#joinButton").queryAs(Button.class)).hasText("Join");
	}

	@Test
	@Order(4)
	public void server_button_click_should_start_server(FxRobot robot) {
		robot.clickOn("#serverButton");
		Assertions.assertThat(robot.lookup("#serverButton").queryAs(Button.class)).hasText("Start Server");
	}

	@Test
	@Order(5)
	public void client_button_click_should_start_client(FxRobot robot) {
		robot.clickOn("#clientButton");
		Assertions.assertThat(robot.lookup("#clientButton").queryAs(Button.class)).hasText("Start Client");
	}

	@Test
	@Order(6)
	public void join_button_changes_text_after_join_and_enables_start(FxRobot robot) {
		robot.doubleClickOn("#playerName");
		robot.write("Test Name");
		robot.clickOn("#joinButton");
	}

}