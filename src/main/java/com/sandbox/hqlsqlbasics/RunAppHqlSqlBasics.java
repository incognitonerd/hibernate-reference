package com.sandbox.hqlsqlbasics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sandbox.models.Student;
import com.sandbox.utils.Constants;

@SpringBootApplication
@Component
@ComponentScan(value = "com.sandbox")
public class RunAppHqlSqlBasics {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunAppHqlSqlBasics.class);

	@Autowired
	private static Environment env;

	public static void main(String[] args) {
		checkDBConnection();
		// saveStudentDemo();
		// getStudentDemo();
		// updateStudentDemo();
		// deleteStudentDemo();
	}

	public static void saveStudentDemo() {
		saveStudent();
	}

	public static void getStudentDemo() {
		List<Student> lastNameStudents = getStudentByLastNameHqlMethod();
		if (lastNameStudents != null && !lastNameStudents.isEmpty()) {
			System.out.println("\n\n");
			for (Student stu : lastNameStudents) {
				System.out.println(stu);
			}
		}
		List<Student> allStudents = getAllStudentsHqlMethod();
		if (allStudents != null && !allStudents.isEmpty()) {
			System.out.println("\n\n");
			for (Student stu : allStudents) {
				System.out.println(stu);
			}
		}
	}

	public static void updateStudentDemo() {
		updateStudentSetterMethod();
		updateStudentSqlMethod();
	}

	public static void deleteStudentDemo() {
		deleteStudentSqlMethod();
	}

	public static void saveStudent() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			Student stu = new Student("xmen", "xmen", "xmen@xmen");
			entry.getValue().save(stu);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	private static List<Student> getStudentByLastNameHqlMethod() {
		List<Student> l = null;
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();
		try {
			entry.getValue().beginTransaction();
			l = (List<Student>) entry.getValue().createQuery("from Student s where s.lName='xmen'", Student.class)
					.getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return l;
	}

	public static List<Student> getAllStudentsHqlMethod() {
		List<Student> l = null;
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			entry.getValue().beginTransaction();

			l = entry.getValue().createQuery("from Student", Student.class).getResultList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
		return l;
	}

	private static void updateStudentSetterMethod() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		Student stu = null;
		try {
			entry.getValue().beginTransaction();
			int stuId = 9;
			stu = entry.getValue().get(Student.class, stuId);
			stu.setfName("Atlanta, Ga");
			entry.getValue().save(stu);
			entry.getValue().flush();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	private static void updateStudentSqlMethod() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			entry.getValue().beginTransaction();
			entry.getValue().createSQLQuery("update Student set email='redbull@redbull'").executeUpdate();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	private static void deleteStudentSqlMethod() {
		Map.Entry<SessionFactory, Session> entry = getSession().entrySet().iterator().next();

		try {
			entry.getValue().beginTransaction();
			entry.getValue().createQuery("delete from Student s where id = 7").executeUpdate();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			entry.getValue().close();
			entry.getKey().close();
		}
	}

	public static Map<SessionFactory, Session> getSession() {
		/*
		 * SessionFactory - reads application.properties and creates sessions Session -
		 * wraps JDBC connection. main object for interacting with the DB
		 */
		SessionFactory sf = new Configuration().configure(Constants.SQL_BASICS_CONFIG).addAnnotatedClass(Student.class)
				.buildSessionFactory();
		Session s = sf.openSession();

		Map<SessionFactory, Session> tmp = new HashMap<SessionFactory, Session>();
		tmp.put(sf, s);

		return tmp;
	}

	public static void checkDBConnection() {
		String url = "jdbc:mysql://localhost:3306/hb_student_tracker?useSSL=false&autoReconnect=true&serverTimezone=UTC";
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
}
