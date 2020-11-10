package com.stackroute.keepnote.test.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.stackroute.keepnote.config.ApplicationContextConfig;
import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.CategoryDAOImpl;
import com.stackroute.keepnote.exception.CategoryAlreadyExistException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;

@RunWith(SpringRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class CategoryDAOImplTest {

	@Autowired
	private EntityManager entityManager;
	//private SessionFactory sessionFactory;
	
	private CategoryDAO categoryDAO;
	private Category category;

	@Before
	public void setUp() throws Exception {
		categoryDAO = new CategoryDAOImpl(entityManager);
		//category = new Category(1, "Testing", "All about testing spring application", null, "Jhon123", null);
		category=new Category(1, "Testing", "All about testing spring application", "Jhon123", new Date(), null);
	}

	@After
	public void tearDown() throws Exception {
		Query query = entityManager.unwrap(Session.class).createQuery("DELETE from Category");
		query.executeUpdate();
	}

	@Test
	@Rollback(true)
	public void testCreateCategorySuccess() throws CategoryNotFoundException, CategoryAlreadyExistException {
		categoryDAO.createCategory(category);
		Category savedCategory = categoryDAO.getCategoryById(category.getCategoryId());
		assertEquals(category, savedCategory);

	}

	@Test(expected= CategoryNotFoundException.class)
	@Rollback(true)
	public void testCreateCategoryFailure() throws CategoryNotFoundException, CategoryAlreadyExistException {
		categoryDAO.createCategory(category);
		Category savedCategory = categoryDAO.getCategoryById(2);
		assertNotEquals(category, savedCategory);

	}

	@Test
	public void testDeleteCategorySuccess() throws CategoryAlreadyExistException, CategoryNotFoundException {
		categoryDAO.createCategory(category);
		boolean status = categoryDAO.deleteCategory(category.getCategoryId());
		assertEquals(true, status);
	}

	@Test(expected= CategoryNotFoundException.class)
	public void testDeleteCategoryFailure() throws CategoryNotFoundException, CategoryAlreadyExistException {
		categoryDAO.createCategory(category);
		@SuppressWarnings("unused")
		Category savedCategory = categoryDAO.getCategoryById(2);
		boolean status = categoryDAO.deleteCategory(2);
		assertEquals(false, status);
	}

	@Test
	public void testUpdateCategory() throws CategoryNotFoundException, CategoryAlreadyExistException {
		categoryDAO.createCategory(category);
		Category savedCategory = categoryDAO.getCategoryById(category.getCategoryId());
		savedCategory.setCategoryDescription("Testing DAO layer in spring MVC");
		boolean status = categoryDAO.updateCategory(savedCategory);
		assertEquals(true, status);
		savedCategory = categoryDAO.getCategoryById(savedCategory.getCategoryId());
		assertEquals("Testing DAO layer in spring MVC", savedCategory.getCategoryDescription());
	}

	@Test
	public void testGetCategoryById() throws CategoryNotFoundException, CategoryAlreadyExistException {
		categoryDAO.createCategory(category);
		Category savedCategory = categoryDAO.getCategoryById(category.getCategoryId());
		assertEquals(category, savedCategory);
	}

	@Test
	public void testGetAllCategoryByUserId() throws CategoryAlreadyExistException, UserNotFoundException {
		categoryDAO.createCategory(category);
		category = new Category(2, "Testing", "All about testing spring application", "Jhon123", new Date(), null);
		categoryDAO.createCategory(category);
		category = new Category(3, "Testing", "All about testing spring application", "Jhon123", new Date(), null);
		categoryDAO.createCategory(category);

		List<Category> allCategories = categoryDAO.getAllCategoryByUserId("Jhon123");
		assertEquals(3, allCategories.size());
	}

}
