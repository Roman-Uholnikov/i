package org.wf.dp.dniprorada.base.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.base.model.Entity;

import java.util.Collection;
import java.util.List;

/**
 * User: goodg_000
 * Date: 05.05.2015
 * Time: 22:33
 */
public abstract class AbstractEntityDao<T extends Entity> implements EntityDao<T> {

   private final static int DEFAULT_DELETE_BATCH_SIZE = 1000;

   private Class<T> entityClass;

   protected AbstractEntityDao(Class<T> entityClass) {
      this.entityClass = entityClass;
   }

   private SessionFactory sessionFactory;

   @Required
   public SessionFactory getSessionFactory() {
      return sessionFactory;
   }

   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   protected Session getSession() {
      return sessionFactory.getCurrentSession();
   }

   @Override
   public List<T> getAll() {
      return DetachedCriteria.forClass(entityClass).getExecutableCriteria(getSession()).list();
   }

   @Override
   public void saveOrUpdate(T entity) {
      getSession().saveOrUpdate(entity);
   }

   @Override
   public T getById(Long identification) {
      return (T) getSession().get(entityClass, identification);
   }

   public Class<T> getEntityClass() {
      return entityClass;
   }

   @Override
   public void delete(T entity) {
      getSession().delete(entity);
   }

   @Override
   public void deleteAll(Collection<T> entities) {
      int i = 0;
      for (T entity : entities) {
         getSession().delete(entity);
         i++;

         if (i >= DEFAULT_DELETE_BATCH_SIZE) {
            getSession().flush();
            i = 0;
         }
      }
   }
}
