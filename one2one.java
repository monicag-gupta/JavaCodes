//Student.java

package com.hibernate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "STUDENT")
public class Student {

	@Id
	@GeneratedValue
	private int student_id;

	private String student_name;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id")
	private StudentDetail studentDetail;

	public StudentDetail getStudentDetail() {
		return studentDetail;
	}

	public void setStudentDetail(StudentDetail studentDetail) {
		this.studentDetail = studentDetail;
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

}



//StudentDetail.java
package com.hibernate;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(name= "STUDENTDETAIL")
public class StudentDetail {
	
	@Id @GeneratedValue(generator = "newGenerator") //name of the primary key generator
	@GenericGenerator(name = "newGenerator", strategy = "foreign",parameters = { @Parameter(value = "student", name = "property") })
	private int student_id;
	
	private String student_mobile_number;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id")
	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public String getStudent_mobile_number() {
		return student_mobile_number;
	}
	public void setStudent_mobile_number(String student_mobile_number) {
		this.student_mobile_number = student_mobile_number;
	}

}




//Main.java

package com.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class Main {

	public static void main(String[] args) {

		Student student = new Student();
		student.setStudent_name("Gontu1");

		StudentDetail studentDetail = new StudentDetail();
		studentDetail.setStudent_mobile_number("99XX1XXX77");
		studentDetail.setStudent(student);
		
		student.setStudentDetail(studentDetail);

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		//please note I am not saving studentDetail object but still it will be saved in database
		//that's the magic of one to one mapping 
		session.save(student);

		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
	}

	
}


