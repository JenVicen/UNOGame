package GUI.Controller;

import Card.UnoColor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorDialogController {

	private static Logger logger = LoggerFactory.getLogger(WelcomeScreenController.class);

	private Stage dialogStage;
	private UnoColor chosenColor;
	@FXML
	private Button redButton;
	@FXML
	private Button blueButton;
	@FXML
	private Button yellowButton;
	@FXML
	private Button greenButton;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Needs to be empty
	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage provide the DialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public UnoColor getChosenColor() {
		return this.chosenColor;
	}

	public void handleRedButtonAction() {
		logger.info("Red button pressed");
		this.chosenColor = UnoColor.RED;
		dialogStage.close();
	}

	public void handleBlueButtonAction() {
		logger.info("Blue button pressed");
		this.chosenColor = UnoColor.BLUE;
		dialogStage.close();
	}

	public void handleYellowButtonAction() {
		logger.info("Yellow button pressed");
		this.chosenColor = UnoColor.YELLOW;
		dialogStage.close();
	}

	public void handleGreenButtonAction() {
		logger.info("Green button pressed");
		this.chosenColor = UnoColor.GREEN;
		dialogStage.close();
	}
}
