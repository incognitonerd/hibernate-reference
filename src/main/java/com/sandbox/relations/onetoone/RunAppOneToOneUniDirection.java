package com.sandbox.relations.onetoone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.sandbox.models.onetomanyunidirectional.Course;
import com.sandbox.models.onetomanyunidirectional.Review;
import com.sandbox.models.onetooneunidirectional.Instructor;
import com.sandbox.models.onetooneunidirectional.InstructorDetail;
import com.sandbox.utils.Constants;

/*
 * One to One UniDirectional
 * Instructor -> Instructor_Detail
 * Can't delete instructor_detail directly. Must delete instructor to delete instructor_detail
 */
@SpringBootApplication
@ComponentScan(value = "com.sandbox")
public class RunAppOneToOneUniDirection {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunAppOneToOneUniDirection.class);

	public static void main(String[] args) {
		// checkDBConnection();
		//saveInstructorAndInstuctorDetail();
		
		Map.Entry<Instructor,InstructorDetail> entry = getInstructorAndInstructorDetail().entrySet().iterator().next();
		if(entry.getKey() != null &&  entry.getValue() != null)
			System.out.println(entry.getKey().getfName() + "\tDetails - " + entry.getValue().toString());
		//deleteInstructorDetailsNotAllowed();
		//deleteInstructor();
	}

	public static void saveInstructorAndInstuctorDetail() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			Instructor instructor = new Instructor("waves", "braids", "fades@dreads");
			InstructorDetail instructorDetail = new InstructorDetail("barber", "shop");
			instructor.setInstructorDetail(instructorDetail);
			entry.getValue().save(instructor);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static Map<Instructor, InstructorDetail> getInstructorAndInstructorDetail() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		Map<Instructor, InstructorDetail> map = new HashMap<Instructor, InstructorDetail>();

		try {
			int instructorIf = 9;
			entry.getValue().beginTransaction();
			Instructor instructor = entry.getValue().get(Instructor.class, instructorIf);
			if (instructor != null) {
				map.put(instructor, instructor.getInstructorDetail());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return map;
	}

	public static void deleteInstructorDetailsNotAllowed() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			entry.getValue().beginTransaction();
			int instructorDetailId = 5;
			InstructorDetail instructorDetail = entry.getValue().get(InstructorDetail.class, instructorDetailId);
			if (instructorDetail != null) {
				entry.getValue().delete(instructorDetail);
				entry.getValue().flush();
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
			entry.getValue().beginTransaction();
			int instructorDetailId = 10;
			Instructor instructor = entry.getValue().get(Instructor.class, instructorDetailId);
			if (instructor != null) {
				entry.getValue().delete(instructor);
				entry.getValue().flush();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static void checkDBConnection() {
		String url = "jdbc:mysql://localhost:3306/hb-01-one-to-one?useSSL=false&autoReconnect=true&serverTimezone=UTC";
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
		SessionFactory sf = new Configuration().configure(Constants.ONE_TO_ONE).addAnnotatedClass(Instructor.class)
				.addAnnotatedClass(InstructorDetail.class).buildSessionFactory();
		Session s = sf.openSession();
		Map<SessionFactory, Session> tmp = new HashMap<SessionFactory, Session>();
		tmp.put(sf, s);

		return tmp;
	}
}
