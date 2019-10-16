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

import com.sandbox.models.onetomanyunidirectional.Course;
import com.sandbox.models.onetomanyunidirectional.Instructor;
import com.sandbox.models.onetomanyunidirectional.InstructorDetail;
import com.sandbox.models.onetomanyunidirectional.Review;
import com.sandbox.utils.Constants;

/*
 * One to Many UniDirectional
 * Course -> Review
 * 
 * 1st delete review
 * 2nd delete course
 * 3rd delete instructor
 * 4th delete instructor_detail
 */
@SpringBootApplication
@ComponentScan(value = "com.sandbox")
public class RunAppOneToManyUniDirection {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunAppOneToManyUniDirection.class);

	public static void main(String[] args) {
		// checkDBConnection();
		//saveCourseAndReviews();
		Map<Course, List<Review>> tmp = getCourseAndReviews();
		for (Map.Entry<Course, List<Review>> entry : tmp.entrySet()) {
			System.out.println(entry.getKey().getId() + " " + entry.getKey().getTitle() + " Reviews:");
			for (Review r : entry.getValue()) {
				System.out.print(r.getComment() + ", ");
			}
		}
		//deleteReview();
		deleteCourse();
	}

	public static void saveCourseAndReviews() {
		Map.Entry<SessionFactory,Session> entry = getSession().entrySet().iterator().next();
		try {
			Instructor instructor = new Instructor("headace", "toothace", "back pain@stomachace");
			InstructorDetail instructorDetail = new InstructorDetail("tylenol", "goody");
			instructor.setInstructorDetail(instructorDetail);


			//Instructor instructor2 = entry.getValue().get(Instructor.class, 11);
			Course c = new Course("lamp");
			c.addReview(new Review("power"));
			c.addReview(new Review("water"));
			c.addReview(new Review("trash"));
			c.setInstructor(instructor);
			entry.getValue().beginTransaction();
			entry.getValue().save(instructor);
			entry.getValue().save(c);
			entry.getValue().flush();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static Map<Course, List<Review>> getCourseAndReviews() {
		Map.Entry<SessionFactory,Session> entry = getSession().entrySet().iterator().next();
		Map<Course, List<Review>> map = new HashMap<Course, List<Review>>();
		try {
			int courseId = 54;
			Course c = entry.getValue().get(Course.class, courseId);
			if (c != null) {
				System.out.println(c.getReviews());
				List<Review> r=c.getReviews();
				map.put(c, r);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return map;
	}

	public static void deleteReview() {
		Map.Entry<SessionFactory,Session> entry = getSession().entrySet().iterator().next();
		try {
			int reviewId = 25;
			Review r = entry.getValue().get(Review.class, reviewId);
			if (r != null) {
				entry.getValue().delete(r);
				entry.getValue().beginTransaction().commit();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void deleteCourse() {
		Map.Entry<SessionFactory,Session> entry = getSession().entrySet().iterator().next();
		try {
			int courseId = 49;
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
	
	public static Map<SessionFactory,Session> getSession() {
		/*
		 * SessionFactory - reads application.properties and creates sessions Session -
		 * wraps JDBC connection. main object for interacting with the DB
		 */
		SessionFactory sf = new Configuration().configure(Constants.ONE_TO_MANY)
				.addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class)
				.addAnnotatedClass(Course.class)
				.addAnnotatedClass(Review.class).buildSessionFactory();
		Session s = sf.openSession();
		
		Map<SessionFactory,Session>  tmp = new HashMap<SessionFactory,Session>();
		tmp.put(sf, s);
		
		return tmp;
	}
}
