package assignment1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StudentStorage{
	 static void save(Collection<Student> student, File f) throws IOException {
		 	System.out.println("Saving...");

			try{
				ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(f));
				o.writeObject(student);
				o.close();
			} catch (IOException e){
				System.out.println(e);
			}
	 }
	 
	 static void save(Collection<Student> student, String file) throws IOException {
		 save(student, new File(file));
	 }
	 
	 @SuppressWarnings("unchecked")
	static Collection<Student> fetch(File f) throws IOException, FileNotFoundException, ClassNotFoundException {
		 	System.out.println("Fetching...");
			List<Student> readStudents = new ArrayList<>();
			ObjectInputStream o = new ObjectInputStream(new FileInputStream(f));

			readStudents = (List<Student>) o.readObject();
			
			List<Student> studentList = new ArrayList<>();
			for (Student s : readStudents) {
				Course c = course(s.getCourse(), studentList);
				s.setCourse(c);
				studentList.add(s);
				
			}

			o.close();
		
			return readStudents;		 
	 }
	 
	public static Course course (Course c, List<Student> list) {
		for(Student testc:list) {
			if (c.equals(testc.getCourse())) {
				c = testc.getCourse();
			}
		}
		return c;
	}
}	
