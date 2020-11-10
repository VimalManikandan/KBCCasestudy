package com.stackroute.keepnote.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.CategoryAlreadyExistException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.ReminderAlreadyExistException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Reminder;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */


@Repository
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	
	private EntityManager entityManager;
	
	@Autowired
	public CategoryDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * Create a new category
	 */
	public Category createCategory(Category category) throws CategoryAlreadyExistException {
		Session session = entityManager.unwrap(Session.class);
		Category c1=null;
		if(getCategorycheck(category.getCategoryId()) == null){
			category.setCategoryCreationDate(new Date());
			Integer id=  (Integer) session.save(category);	
			c1=session.get(Category.class, id);
		}
		else {
			throw new CategoryAlreadyExistException("Category Already Exists");
		}
		return c1;

	}
	
	public Category getCategorycheck(int categoryId) {
		Session session = entityManager.unwrap(Session.class);
		Category category= session.get(Category.class, categoryId);
		return category;
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) throws CategoryNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete from Category where categoryId=:cId");
		query.setParameter("cId", categoryId);
		if (query.executeUpdate() > 0) {
			return true;
		} else
			throw new CategoryNotFoundException("Category Not Found");

	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) throws CategoryNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Category c1 = new Category();
		c1.setCategoryId(category.getCategoryId());
		c1.setCategoryCreatedBy(category.getCategoryCreatedBy());
		c1.setCategoryDescription(category.getCategoryDescription());
		c1.setCategoryCreationDate(category.getCategoryCreationDate());
		c1.setCategoryName(category.getCategoryName());
		try {
			session.merge(c1);			
		} catch (Exception e) {
			throw new CategoryNotFoundException("Category Not Found");		
		}

		return true;

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Category category=session.get(Category.class, categoryId);
		if(category==null) {
			throw new CategoryNotFoundException("Not Found");
		}
		else {
			return category;
		}

	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) throws UserNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("from Category c where c.categoryCreatedBy=:uId");
		query.setParameter("uId", userId);
		List<Category> cList= query.getResultList();
		if(cList!=null)
			return cList;
		else {
			throw new UserNotFoundException("User Not Found");
		}


	}

}
