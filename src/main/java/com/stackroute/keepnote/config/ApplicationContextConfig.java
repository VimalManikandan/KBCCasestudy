package com.stackroute.keepnote.config;

import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.model.User;




/*This class will contain the application-context for the application. 
 * Define the following annotations:
 * @Configuration - Annotating a class with the @Configuration indicates that the 
 *                  class can be used by the Spring IoC container as a source of 
 *                  bean definitions
 * @ComponentScan - this annotation is used to search for the Spring components amongst the application
 * @EnableWebMvc - Adding this annotation to an @Configuration class imports the Spring MVC 
 * 				   configuration from WebMvcConfigurationSupport 
 * @EnableTransactionManagement - Enables Spring's annotation-driven transaction management capability.
 *                  
 * 
 * */

/*@Configuration 
@ComponentScan(basePackages = "com.stackroute.keepnote")
@EnableWebMvc
@EnableTransactionManagement*/
public class ApplicationContextConfig {

	/*
	 * Define the bean for DataSource. In our application, we are using MySQL as the
	 * dataSource. To create the DataSource bean, we need to know: 1. Driver class
	 * name 2. Database URL 3. UserName 4. Password
	 * 
	 */

	
	/*
	 * @Autowired private EntityManagerFactory entityManagerFactory;
	 * 
	 * @Autowired private UserDAO userDAO;
	 * 
	 * @Bean public SessionFactory getSessionFactory() { if
	 * (entityManagerFactory.unwrap(SessionFactory.class) == null) { throw new
	 * NullPointerException("factory is not a hibernate factory"); } return
	 * entityManagerFactory.unwrap(SessionFactory.class); }
	 */

 
	
	
	/*
	 * @Bean public DataSource getDataSource() { DataSourceBuilder
	 * dataSourceBuilder= DataSourceBuilder.create();
	 * dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
	 * dataSourceBuilder.url(
	 * "jdbc:mysql://localhost:3306/usecase_db?useSSL=false&serverTimezone=UTC");
	 * dataSourceBuilder.username("root"); dataSourceBuilder.password("Kbc@2020");
	 * return dataSourceBuilder.build(); }
	 */
	 
	
	
	/*
	 * Use this configuration while submitting solution in hobbes.
	 * dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	 * dataSource.setUrl("jdbc:mysql://" + System.getenv("MYSQL_HOST") + ":3306/" +
	 * System.getenv("MYSQL_DATABASE")
	 * +"?verifyServerCertificate=false&useSSL=false&requireSSL=false");
	 * dataSource.setUsername(System.getenv("MYSQL_USER"));
	 * dataSource.setPassword(System.getenv("MYSQL_PASSWORD"));
	 */

	/*
	 * create a getter for Hibernate properties here we have to mention 1. show_sql
	 * 2. Dialect 3. hbm2ddl
	 */

	/*
	 * Define the bean for SessionFactory. Hibernate SessionFactory is the factory
	 * class through which we get sessions and perform database operations.
	 */

	/*
	 * Define the bean for Transaction Manager. HibernateTransactionManager handles
	 * transaction in Spring. The application that uses single hibernate session
	 * factory for database transaction has good choice to use
	 * HibernateTransactionManager. HibernateTransactionManager can work with plain
	 * JDBC too. HibernateTransactionManager allows bulk update and bulk insert and
	 * ensures data integrity.
	 */

}
