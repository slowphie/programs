package assignment1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCloningStudents {
	Student s = new Student("1234", "Joe", "Bloggs", LocalDate.now(), new Course(159272, "Paradigms"), new Address("Yemen", "Yemen Road", "1234", 15));
	Student clone;
	
	@BeforeEach
	void setup() throws CloneNotSupportedException {
		clone = s.clone();
	}
	
	@Test
	void testclone(){
		//Testing that two students have equal fields but different memory locations
		assertEquals(s, clone);
		assertTrue(s != clone);
	}
	
	@Test
	void testDeepCopyStudent() {
		//Testing that the change in name only affects one student
		assertTrue(s.getFirst_name()== clone.getFirst_name());
		clone.setFirst_name("John");
		assertTrue(s.getFirst_name()!=clone.getFirst_name());
	}
	
	@Test
	void testDeepCopyAddress() {
		//Testing that the change in address field only affects one student
		assertTrue(s.getAddress().getHouseNum()== clone.getAddress().getHouseNum());
		clone.getAddress().setHouseNum(16);
		assertTrue(s.getAddress().getHouseNum()!= clone.getAddress().getHouseNum());
	}
	
	@Test
	void testShallowCopyCourse() {
		//Testing that changing the course of one student will affect all with that same course
		assertTrue(s.getCourse().getName()== clone.getCourse().getName());
		clone.getCourse().setName("Programming Paradigms");
		assertTrue(s.getCourse().getName()== clone.getCourse().getName());
	}
	
	@Test
	void testCourse() {
		//Testing that changing the course of one student will affect all with that same course
		assertTrue(s.getCourse()== clone.getCourse());
		clone.getCourse().setName("Programming Paradigms");
		assertTrue(s.getCourse().getName()== clone.getCourse().getName());
	}
	

}
