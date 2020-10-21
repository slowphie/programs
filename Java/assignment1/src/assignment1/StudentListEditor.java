package assignment1;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;



public class StudentListEditor extends Application {
	static List<Student> students;
	static File f = new File("studentsFile.bin");
	
	//Variables for details
	String idVar = "";
	String nameVar = "";
	String surnameVar = "";
	LocalDate dateVar = LocalDate.now();
	int courseNumVar = 0;
	String courseNameVar = "";
	String townVar = "";
	String streetVar = "";
	String postcodeVar = "";
	int houseNumVar = 0;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Student Management System");
		
		//MENU PANNEL
		Button display = new Button("Display");
		display.setMinWidth(100);
		Button add = new Button("Add");
		add.setMinWidth(100);
		Button delete = new Button("Delete");
		delete.setMinWidth(100);
		Button edit = new Button("Edit");
		edit.setMinWidth(100);
				
		GridPane menu = new GridPane();
		menu.setHgap(10);
		menu.setVgap(10);

		menu.add(display, 0, 1);
		menu.add(add, 1, 1);
		menu.add(delete, 0, 2);
		menu.add(edit, 1, 2);
		
		BorderPane b = new BorderPane();	
		ImageView img = new ImageView("banner.png");
		img.setFitHeight(105);
		img.setPreserveRatio(true);

		Insets inset = new Insets(10,10,10,10);
		
		b.setTop(img);
		BorderPane.setMargin(menu, inset);
		b.setCenter(menu);
		 
		Scene fieldScene = new Scene(b, 400, 400);
		primaryStage.setScene(fieldScene);
		primaryStage.show();
		
		
		Button backMenu = new Button("Back to menu");
		backMenu.setMaxWidth(Double.MAX_VALUE);
		Button backMenu2 = new Button("Back to menu");
		backMenu2.setMaxWidth(Double.MAX_VALUE);
		Button backMenu3 = new Button("Back to menu");
		backMenu3.setMaxWidth(Double.MAX_VALUE);
		Button backMenu4 = new Button("Back to menu");
		backMenu4.setMaxWidth(Double.MAX_VALUE);
		
		//FIELDS SCENE
		TextField id = new TextField();
		TextField name = new TextField();
		TextField surname = new TextField();
		DatePicker dob = new DatePicker();
		TextField courseNum = new TextField();
		TextField courseName = new TextField();
		TextField town = new TextField();
		TextField street = new TextField();
		TextField postcode = new TextField();
		TextField houseNum = new TextField(); 
		
		Button enter = new Button("Enter");
		enter.setMaxWidth(Double.MAX_VALUE);
		
		primaryStage.setOnCloseRequest(e ->{
		    	try {
					StudentStorage.save(students, f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    	System.out.println("Closing...");
		  });

		display.setOnAction(e -> {
				
				BorderPane displayBorder = new BorderPane();
				Insets displayInset = new Insets(10,10,10,10);
				ImageView displayImg = new ImageView("banner.png");
				displayImg.setFitHeight(105);
				displayImg.setPreserveRatio(true);
				displayBorder.setTop(displayImg);
				
				Label displayTitle = new Label("Displaying:");
				displayTitle.setFont(new Font(20));
				ScrollPane scroll = new ScrollPane();
				GridPane displayGrid = new GridPane();
				displayGrid.add(displayTitle, 0, 0);

				for(int i = 0; i < students.size(); i++) {
					int pos = i*11;
					Student student = students.get(i);
					displayGrid.add(new Label("ID:  "), 0, pos+1);
					displayGrid.add(new Label(""+student.getId()), 1, pos+1);
					displayGrid.add(new Label("Name"), 0, pos+2);
					displayGrid.add(new Label(""+student.getFirst_name() + " "+students.get(i).getLast_name()), 1, pos+2);
					displayGrid.add(new Label("Date of Birth: "), 0, pos+3);
					displayGrid.add(new Label(""+student.getDateOfBirth()), 1, pos+3);
					displayGrid.add(new Label("--Address--"), 0, pos+4);
					displayGrid.add(new Label(""+student.getAddress().getHouseNum() + " " +student.getAddress().getStreet()), 0, pos+5);					
					displayGrid.add(new Label(""+student.getAddress().getTown()), 0, pos+6);					
					displayGrid.add(new Label(""+student.getAddress().getPostcode()), 0, pos+7);	
					displayGrid.add(new Label("--Course--"), 0, pos+8);
					displayGrid.add(new Label("Name: "), 0, pos+9);
					displayGrid.add(new Label(""+student.getCourse().getNumber()), 1, pos+9);
					displayGrid.add(new Label("Number: "), 0, pos+10);
					displayGrid.add(new Label(""+student.getCourse().getName()), 1, pos+10);
					displayGrid.add(new Label(" "), 0, pos+11);			
					
				}
				
				displayGrid.add(backMenu, 0, students.size()*11+1);	
				BorderPane.setMargin(displayGrid, displayInset);
				displayBorder.setCenter(displayGrid);
				scroll.setContent(displayBorder);
				Scene displayScene = new Scene(scroll, 400, 400);
				primaryStage.setScene(displayScene);
		});
		
		add.setOnAction(e ->{
			BorderPane addBorder = new BorderPane();
			Insets addInset = new Insets(10,10,10,10);
			ImageView addImg = new ImageView("banner.png");
			ScrollPane scroll = new ScrollPane();
			
			addImg.setFitHeight(105);
			addImg.setPreserveRatio(true);
			addBorder.setTop(addImg);
			GridPane fields = new GridPane();


			fields.add(new Label("ID: "), 0, 1);
			fields.add(id, 1, 1);
			fields.add(new Label("First Name: "), 0, 2);
			fields.add(name, 1, 2);
			fields.add(new Label("Last Name: "), 0, 3);
			fields.add(surname, 1, 3);
			fields.add(new Label("Date Of Birth: "), 0, 4);
			fields.add(dob, 1, 4);
			fields.add(new Label("---Address---"), 0, 5);
			fields.add(new Label("House number:  "), 0, 6);
			fields.add(houseNum, 1, 6);
			fields.add(new Label("Street:  "), 0, 7);
			fields.add(street,1,7);
			fields.add(new Label("Town:  "), 0, 8);
			fields.add(town,1,8);
			fields.add(new Label("Postcode:  "), 0, 9);
			fields.add(postcode,1,9);
			
			fields.add(new Label("---Course---"), 0, 10);
			fields.add(new Label("Course number: "), 0, 11);
			fields.add(courseNum, 1, 11);
			fields.add(new Label("Course name: "), 0, 12);
			fields.add(courseName, 1, 12);
				
				Label addTitle = new Label("Enter details:");
				addTitle.setFont(new Font(20));		
				fields.add(addTitle, 0, 0);
				fields.add(enter, 1, 13);
				fields.add(backMenu2, 1, 14);
				
				BorderPane.setMargin(fields, addInset);
				addBorder.setCenter(fields);
				scroll.setContent(addBorder);
				Scene addScene = new Scene(scroll);
				primaryStage.setScene(addScene);	

		});
		
		delete.setOnAction(e ->{
				BorderPane deleteBorder = new BorderPane();
				Insets deleteInset = new Insets(10,10,10,10);
				ImageView deleteImg = new ImageView("banner.png");
				GridPane searchGrid = new GridPane();	
				TextField searchID = new TextField();
				Button search = new Button("Delete");
				Label error = new Label();
				
				searchGrid.setHgap(10);
				searchGrid.setVgap(10);
				
				deleteImg.setFitHeight(105);
				deleteImg.setPreserveRatio(true);
				deleteBorder.setTop(deleteImg);
				
				searchGrid.add(new Label("Enter the ID of the student"), 0, 0);
				searchGrid.add(searchID, 0, 1);
				searchGrid.add(search, 1, 1);
				searchGrid.add(backMenu3, 0, 2);
				searchGrid.add(error, 0, 3);
				
				BorderPane.setMargin(searchGrid, deleteInset);
				deleteBorder.setCenter(searchGrid);
				Scene searchScene = new Scene(deleteBorder,  400, 400);
				primaryStage.setScene(searchScene);
				
				search.setOnAction(e1->{
					String idSearch = searchID.getText();
					boolean found = false;
					int i = 0;
					while (!found) {
						if (i<students.size()) {
							if (students.get(i).getId().equals(idSearch)) {
								System.out.println("Removing..." + students.get(i));
								
								students.remove(i);
								
								found = true;
							}
							i++;
						} else {
							error.setText("Student: " + idSearch + " not found");
							System.out.println("Not Found");
							found = true;
						}
					}								
			});
		});
		
		edit.setOnAction(e -> {
			BorderPane searchBorder = new BorderPane();
			Insets searchInset = new Insets(10,10,10,10);
			ImageView searchImg = new ImageView("banner.png");
			GridPane searchGrid = new GridPane();	
			TextField searchID = new TextField();
			Button search = new Button("Edit");
			Button update = new Button("Update");
			update.setMaxWidth(Double.MAX_VALUE);
			
			searchGrid.setHgap(10);
			searchGrid.setVgap(10);
			
			searchImg.setFitHeight(105);
			searchImg.setPreserveRatio(true);
			searchBorder.setTop(searchImg);
			
			Label error = new Label();
			
			searchGrid.add(new Label("Enter the ID of the student"), 0, 0);
			searchGrid.add(searchID, 0, 1);
			searchGrid.add(search, 1, 1);
			searchGrid.add(backMenu4, 0, 2);
			searchGrid.add(error, 0, 3);
			
			BorderPane.setMargin(searchGrid, searchInset);
			searchBorder.setCenter(searchGrid);		
			Scene searchScene = new Scene(searchBorder,  400, 400);
			primaryStage.setScene(searchScene);
			
			search.setOnAction(e1->{
				String idSearch = searchID.getText();
				boolean found = false;
				int i = 0;
				while (!found) {
					if (i<students.size()) {
						if (students.get(i).getId().equals(idSearch)) {
							Student editStudent = students.get(i);

							BorderPane editBorder = new BorderPane();
							Insets editInset = new Insets(10,10,10,10);
							ImageView editImg = new ImageView("banner.png");
							ScrollPane scroll = new ScrollPane();
							
							editImg.setFitHeight(105);
							editImg.setPreserveRatio(true);
							editBorder.setTop(editImg);
							GridPane fields = new GridPane();
							
							id.setText(editStudent.getId());
							name.setText(editStudent.getFirst_name());
							surname.setText(editStudent.getLast_name());
							dob.setValue(editStudent.getDateOfBirth());
							courseNum.setText(convertString(editStudent.getCourse().getNumber())); //edit for nmumberssssss
							courseName.setText(editStudent.getCourse().getName());
							town.setText(editStudent.getAddress().getTown());
							street.setText(editStudent.getAddress().getStreet());
							postcode.setText(editStudent.getAddress().getPostcode());
							houseNum.setText(convertString(editStudent.getAddress().getHouseNum())); //edit for numbers

							fields.add(new Label("ID: "), 0, 1);
							fields.add(id, 1, 1);
							fields.add(new Label("First Name: "), 0, 2);
							fields.add(name, 1, 2);
							fields.add(new Label("Last Name: "), 0, 3);
							fields.add(surname, 1, 3);
							fields.add(new Label("Date Of Birth: "), 0, 4);
							fields.add(dob, 1, 4);
							fields.add(new Label("---Address---"), 0, 5);
							fields.add(new Label("House number:  "), 0, 6);
							fields.add(houseNum, 1, 6);
							fields.add(new Label("Street:  "), 0, 7);
							fields.add(street,1,7);
							fields.add(new Label("Town:  "), 0, 8);
							fields.add(town,1,8);
							fields.add(new Label("Postcode:  "), 0, 9);
							fields.add(postcode,1,9);
							
							fields.add(new Label("---Course---"), 0, 10);
							fields.add(new Label("Course number: "), 0, 11);
							fields.add(courseNum, 1, 11);
							fields.add(new Label("Course name: "), 0, 12);
							fields.add(courseName, 1, 12);
								
							Label addTitle = new Label("Enter details:");
							addTitle.setFont(new Font(20));	
							fields.add(addTitle, 0, 0);
							fields.add(update, 1, 13);
							fields.add(backMenu2, 1, 14);	
							
							BorderPane.setMargin(fields, editInset);
							editBorder.setCenter(fields);
							scroll.setContent(editBorder);
							Scene addScene = new Scene(scroll);
							primaryStage.setScene(addScene);
							
							update.setOnAction(e2-> {
								String idVar = id.getText();
								String nameVar = name.getText();
								String surnameVar = surname.getText();
								LocalDate dateVar = dob.getValue();
								int courseNumVar = convertInt(courseNum.getText(), "Course Number"); 
								String courseNameVar = courseName.getText();
								String townVar = town.getText();
								String streetVar = street.getText();
								String postcodeVar = postcode.getText();
								int houseNumVar = convertInt(houseNum.getText(), "House Number");
								
								editStudent.setId(idVar);
								editStudent.setFirst_name(nameVar);
								editStudent.setLast_name(surnameVar);
								editStudent.setDateOfBirth(dateVar);
								editStudent.getCourse().setNumber(courseNumVar);
								editStudent.getCourse().setName(courseNameVar);
								editStudent.getAddress().setTown(townVar);
								editStudent.getAddress().setStreet(streetVar);
								editStudent.getAddress().setPostcode(postcodeVar);
								editStudent.getAddress().setHouseNum(houseNumVar);
	
								id.clear();
								name.clear();
								surname.clear();
								dob.setValue(null);
								courseNum.clear();
								courseName.clear();
								town.clear();
								street.clear();
								postcode.clear();
								houseNum.clear();
								primaryStage.setScene(fieldScene);
								
							});
							
							
							found = true;
						}
						i++;
					} else {
						error.setText("Student: " + idSearch + " not found");
						System.out.println("Not Found");
						found = true;
					}
				}								
		});

		});
		
		enter.setOnAction(e-> {
				System.out.println("Saving student...");
				String idVar = id.getText();
				String nameVar = name.getText();
				String surnameVar = surname.getText();
				LocalDate dateVar = dob.getValue();
				int courseNumVar = convertInt(courseNum.getText(), "Course Number");  //change when figure out number field
				String courseNameVar = courseName.getText();
				String townVar = town.getText();
				String streetVar = street.getText();
				String postcodeVar = postcode.getText();
				int houseNumVar = convertInt(houseNum.getText(), "House Number"); //change when figure out number field
				
				students.add(new Student(idVar, nameVar, surnameVar, dateVar, course(new Course(courseNumVar, courseNameVar)), new Address(townVar, streetVar, postcodeVar, houseNumVar)));
				
				id.clear();
				name.clear();
				surname.clear();
				dob.setValue(null);
				courseNum.clear();
				courseName.clear();
				town.clear();
				street.clear();
				postcode.clear();
				houseNum.clear();
				
							
		});
		
		
		backMenu.setOnAction(e -> primaryStage.setScene(fieldScene));	
		backMenu2.setOnAction(e -> {
			id.clear();
			name.clear();
			surname.clear();
			dob.setValue(null);
			courseNum.clear();
			courseName.clear();
			town.clear();
			street.clear();
			postcode.clear();
			houseNum.clear();
			primaryStage.setScene(fieldScene);
		});	
		backMenu3.setOnAction(e -> primaryStage.setScene(fieldScene));	
		backMenu4.setOnAction(e -> primaryStage.setScene(fieldScene));	


	}
	
	public int convertInt (String s, String fieldName) {
		try {
		    int i = Integer.parseInt(s);
		    return i;
		} catch (NumberFormatException e) {
			System.out.println(fieldName + " was not updated");
		    return 0;
		}
	}
	
	public String convertString (int i) {
	    String s = Integer.toString(i);
	    return s;
	}
	
	public Course course (Course c) {
		for(Student testc:students) {
			if (c.equals(testc.getCourse())) {
				c = testc.getCourse();
			}
		}
		return c;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if (f.exists()) {
			students = (List<Student>) StudentStorage.fetch(f);	
		}
		
		
		launch(args);
	}
}
