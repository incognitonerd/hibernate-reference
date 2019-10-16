package com.sandbox.relations.manytomany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sandbox.models.manytomanybidirectional.Course;
import com.sandbox.models.manytomanybidirectional.Instructor;
import com.sandbox.models.manytomanybidirectional.InstructorDetail;
import com.sandbox.models.manytomanybidirectional.Review;
import com.sandbox.models.manytomanybidirectional.Student;
import com.sandbox.utils.Constants;

/*
 * Many to Many BiDirectional
 * Student <--> Course
 */
@SpringBootApplication
public class RunAppManyToManyBiDirection {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunAppManyToManyBiDirection.class);

	public static void main(String[] args) {
		// checkDBConnection();
		// saveStudentAndCourse();
		Map<Student, List<Course>> tmp = getStudentAndCourse(5);
		for (Map.Entry<Student, List<Course>> entry : tmp.entrySet()) {
			System.out.println(entry.getKey().getId() + " " + entry.getKey().getfName() + " "
					+ entry.getKey().getlName() + " takes:");
			for (Course c : entry.getValue()) {
				System.out.print(c.getTitle() + ", ");
			}
		}

		//addStudentToCourse();
		//deleteCourse(3);
		deleteStudent(15);
	}

	public static void saveStudentAndCourse() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			Course c = new Course("violence");
			Student s1 = new Student("spanish", "french", "english");
			Student s2 = new Student("africa", "europe", "antarctica");
			c.addStudent(s1);
			c.addStudent(s2);

			entry.getValue().beginTransaction();
			entry.getValue().save(s1);
			entry.getValue().save(s2);
			entry.getValue().save(c);
			entry.getValue().flush();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void addStudentToCourse() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			entry.getValue().beginTransaction();
			Student stu = entry.getValue().get(Student.class, 12);
			if (stu != null) {
				List<Course> l = entry.getValue().createQuery("from Course", Course.class).getResultList();
				if (!l.isEmpty()) {
					for (int i = 0; i < l.size(); i++) {
						if (!stu.getCourses().contains(l.get(i)))
							stu.getCourses().add(l.get(i));
					}
				}
				entry.getValue().save(stu);
				entry.getValue().flush();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static Map<Student, List<Course>> getStudentAndCourse(int studentId) {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		Map<Student, List<Course>> map = new HashMap<Student, List<Course>>();
		try {
			entry.getValue().beginTransaction();

			Student stu = entry.getValue().get(Student.class, studentId);
			if (stu != null) {
				System.out.println(stu.getCourses());
				List<Course> c = stu.getCourses();
				map.put(stu, c);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return map;
	}

	public static void deleteCourse(int courseId) {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			Course c = entry.getValue().get(Course.class, courseId);
			if (c != null) {
				entry.getValue().delete(c);
				entry.getValue().beginTransaction().commit();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void deleteStudent(int studentId) {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			Student stu = entry.getValue().get(Student.class, studentId);
			if (stu != null) {
				entry.getValue().delete(stu);
				entry.getValue().beginTransaction().commit();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void checkDBConnection() {
		String url = "jdbc:mysql://localhost:3306/hb-05-many-to-many?useSSL=false&autoReconnect=true&serverTimezone=UTC";
		String usr = "****************";
		String pw = "****************";
		
		try {
			System.out.println("Connecting to DB - " + url);
			Connection c = DriverManager.getConnection(url, usr, pw);
			System.out.println("Connection to Successful");			
			if(c !=null)
			c.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} 
	}

	public static Map<SessionFactory, Session> getSession() {
		/*
		 * SessionFactory - reads application.properties and creates sessions Session -
		 * wraps JDBC connection. main object for interacting with the DB
		 */
		SessionFactory sf = new Configuration().configure(Constants.MANY_TO_MANY).addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Review.class)
				.addAnnotatedClass(Course.class).addAnnotatedClass(Student.class).buildSessionFactory();
		Session s = sf.openSession();

		Map<SessionFactory, Session> tmp = new HashMap<SessionFactory, Session>();
		tmp.put(sf, s);

		return tmp;
	}
}
