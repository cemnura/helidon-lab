package com.cemnura.dal;

import com.cemnura.db.HibernateUtil;
import com.cemnura.entity.MovieCharacter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuoteDBAccess {

    private static final Logger logger = LogManager.getLogger(QuoteDBAccess.class);

    public static Long insertCharacter(MovieCharacter character)
    {
        Long result = -1L;
        Transaction tx = null;

        try (Session session = HibernateUtil.openSession()){

            tx = session.beginTransaction();

            result = (Long) session.save(character);

        }catch (Exception ex){
            logger.warn(ex.getMessage());
            if (tx != null)
                tx.rollback();
        }finally {
            //tx.commit();
        }

        return result;
    }

    public static List<MovieCharacter> getCharacterList(String name)
    {
        try (Session session = HibernateUtil.openSession()){

            String hql = " FROM MovieCharacter C";
            Query query = session.createQuery(hql);

            return query.getResultList();

        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }

        return Collections.emptyList();
    }

    public static Optional<MovieCharacter> getCharacterByName(String name)
    {
        Transaction tx;

        try (Session session = HibernateUtil.openSession()){

            tx = session.beginTransaction();

            String hql = " FROM MovieCharacter C WHERE C.name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("name", name);

            return Optional.of((MovieCharacter) query.getSingleResult());

        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }finally {
        }

        return Optional.empty();
    }

}
