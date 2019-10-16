package com.sandbox.relations.onetomany;

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
import org.springframework.context.annotation.ComponentScan;

import com.sandbox.models.onetomanybidirectional.Course;
import com.sandbox.models.onetomanybidirectional.Instructor;
import com.sandbox.models.onetomanybidirectional.InstructorDetail;
import com.sandbox.utils.Constants;

/*
 * One to Many BiDirectional
 * Instructor <--> Course
 * Must delete the courses an instructor teaches before deleting the instructor
 */
@SpringBootApplication
@ComponentScan(value = "com.sandbox")
public class RunAppOneToManyBiDirection {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunAppOneToManyBiDirection.class);

	public static void main(String[] args) {
		// checkDBConnection();
		// saveInstructorAndInstructorDetail();
		// saveCourse();
		Map<Instructor, List<Course>> tmp = getInstructorAndCourses(12);
		for (Map.Entry<Instructor, List<Course>> entry : tmp.entrySet()) {
			System.out.println(entry.getKey().getId() + " " + entry.getKey().getfName() + " "
					+ entry.getKey().getlName() + " teaches:");
			for (Course c : entry.getValue()) {
				System.out.print(c.getTitle() + ", ");
			}
		}
		//deleteCourse();
		//deleteInstructor();
	}

	public static void saveInstructorAndInstructorDetail() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			Instructor instructor = new Instructor("shoe", "shirt", "hat@socks");
			InstructorDetail instructorDetail = new InstructorDetail("clothes", "ropas");
			instructor.setInstructorDetail(instructorDetail);
			entry.getValue().save(instructor);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void saveCourse() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			entry.getValue().beginTransaction();
			Instructor instructor = entry.getValue().get(Instructor.class, 13);
			if(instructor != null) {
				Course[] courses = { new Course("carpet"), new Course("marble") };
				for (Course c : courses) {
					instructor.addCourse(c);
					entry.getValue().save(c);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static Map<Instructor, List<Course>> getInstructorAndCourses(int instructorId) {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		Map<Instructor, List<Course>> map = new HashMap<Instructor, List<Course>>();
		try {
			Instructor instructor = entry.getValue().get(Instructor.class, instructorId);
			if (instructor != null) {
				System.out.println(instructor.getCourses());
				List<Course> c = instructor.getCourses();
				map.put(instructor, c);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return map;
	}

	public static void deleteCourse() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			Course course = entry.getValue().get(Course.class, 41);
			if (course != null) {
				entry.getValue().delete(course);
				entry.getValue().beginTransaction().commit();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void deleteInstructor() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			Instructor instructor = entry.getValue().get(Instructor.class, 10);
			if (instructor != null) {
				entry.getValue().delete(instructor);
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
		String url = "jdbc:mysql://localhost:3306/hb-03-one-to-many?useSSL=false&autoReconnect=true&serverTimezone=UTC";
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
		SessionFactory sf = new Configuration().configure(Constants.ONE_TO_MANY).addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).buildSessionFactory();
		Session s = sf.openSession();

		Map<SessionFactory, Session> tmp = new HashMap<SessionFactory, Session>();
		tmp.put(sf, s);

		return tmp;
	}
}
