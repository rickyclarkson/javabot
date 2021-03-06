package javabot.dao;

import javax.persistence.EntityManager;

import javabot.model.Persistent;

/**
 * Created Jul 6, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public interface BaseDao<T> {
    void save(Persistent persistent);

    void delete(Persistent persistent);

    void delete(Long id);

    EntityManager getEntityManager();
}
