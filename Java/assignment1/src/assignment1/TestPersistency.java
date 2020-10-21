package assignment1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.jupiter.api.Test;


class TestPersistency {
	static File f = new File("testFile.bin");
	static List<Student> list1 = new ArrayList<>();
	static List<Student> list2 = new ArrayList<>();


	
	@Test
	void testSaveFetch() throws IOException, ClassNotFoundException {
		list1.add(new Student("1", "Joe", "Bloggs", LocalDate.now(), 
				new Course(159272, "Paradigms"), new Address("Yemen", "Yemen Road", "1234", 15)));
		StudentStorage.save(list1, f);
		list2 = (List<Student>) StudentStorage.fetch(f);
		assertEquals(list1,list2);
	}
	
	@Test
	void testIntegrity() throws IOException, ClassNotFoundException {
		
		Student s1 = new Student("1", "Joe", "Bloggs", LocalDate.now(), 
				new Course(159272, "Paradigms"), new Address("Yemen", "Yemen Road", "1234", 15));
		list1.add(s1);
		Course c = course(new Course(159272, "Paradigms"));
	
		Student s2 = new Student("2", "Bob", "Smith", LocalDate.now(), 
				c, new Address("Town", "Town Street", "4321", 1));

		list1.add(s2);
		StudentStorage.save(list1, f);
		list2 = (List<Student>) StudentStorage.fetch(f);
		
		
		//Testing equals before save
		assertTrue(s1.getCourse()==s2.getCourse());
		assertTrue(s1.getAddress()!=s2.getAddress());
		
		//Testing editing course
		s1.getCourse().setName("Programming Paradigms");
		assertEquals(s1.getCourse(),s2.getCourse());
		
		//Testing equals after fetch
		Student s3 = list2.get(0);
		Student s4 = list2.get(1);
		assertTrue(list2.get(0).getCourse()==list2.get(1).getCourse());
		
		//Testing referential integrity
		s3.getCourse().setName("Programming");
		assertEquals(s3.getCourse(),s4.getCourse());		
		
	}
	
	@Test
	void testFetchException() throws IOException {
		assertThrows(FileNotFoundException.class, () -> StudentStorage.fetch(new File("testWrongFile.bin")));
	}
	
	@AfterClass
	public static void resetTestFile() throws IOException {
		//This resets the test file to be blank
		StudentStorage.save(new ArrayList<>(), f);
	}
	
	
	public Course course (Course c) {
		for(Student testc:list1) {
			if (c.equals(testc.getCourse())) {
				c = testc.getCourse();
			}
		}
		return c;
	}
}

