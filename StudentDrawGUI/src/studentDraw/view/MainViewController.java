package studentDraw.view;

import studentDraw.FileHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MainViewController {
	private final int EXITEMENT_TIME = 2000;
	private final String classListFileName = "classList.txt";
	private final String notPresentFileName = "notPresent.txt";
	private final String winnersFileName = "winners.txt";
	
	@FXML
	private ListView<String> studentList;
	private ObservableList<String> list;
	private Hashtable<String, Integer> availableStudents;
	@FXML
	private Label winnerLabel;
	@FXML
	private Label winnerHeader;

	public MainViewController() {
		availableStudents = FileHelper.getAvailableStudents(classListFileName, notPresentFileName , winnersFileName);
	}

	@FXML
	private void initialize() {
		list = FXCollections.observableArrayList();
		studentList.setItems(list);
		winnerLabel.visibleProperty().set(false);
		winnerHeader.visibleProperty().set(false);
	}

	@FXML
	private void handleStart() {		
		new Thread(() -> {
			getWinner();
			
			}).start();
	}
	
	private void showWinner(String winner) {
		winnerLabel.textProperty().set(winner);
		winnerLabel.visibleProperty().set(true);
		winnerHeader.visibleProperty().set(true);
	}

	private static <T> void triggerUpdate(ListView<T> listView, T newValue, int i) {
        EventType<? extends ListView.EditEvent<T>> type = ListView.editCommitEvent();
        Event event = new ListView.EditEvent<>(listView, type, newValue, i);
        listView.fireEvent(event);
        listView.scrollTo(i);
    }

	private void getWinner(){
		while (availableStudents.size() > 1){
			String studentToRemove = getStudentToRemove(availableStudents);
			int numberOfClasses = availableStudents.get(studentToRemove);
			if (numberOfClasses > 0){
				availableStudents.put(studentToRemove, --numberOfClasses);
			}
			else{
				availableStudents.remove(studentToRemove);
				System.out.println("Removing student: " + studentToRemove);
				Platform.runLater(() -> {
					list.add(studentToRemove);
					triggerUpdate(studentList, studentToRemove, list.size() - 1);
				});
				
				try {
					Thread.sleep(EXITEMENT_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		
		String winner = availableStudents.keys().nextElement();
		FileHelper.writeWinnerToFile(winnersFileName, winner);
		Platform.runLater(() -> {
			showWinner(winner);
		});
	}
	
	private String getStudentToRemove(Hashtable<String, Integer> students) {
		Random rand = new Random();
		ArrayList<String> studentNames = Collections.list(students.keys());
		String student = studentNames.get(rand.nextInt(students.size()));		
		return student;
	}
}