package com.cemnura.dal;

import com.cemnura.db.HibernateUtil;
import com.cemnura.entity.MovieCharacter;
import com.cemnura.entity.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
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
                ;
               // tx.rollback();
            throw ex;
        }finally {
//            tx.commit();
        }

        return result;
    }

    public static List<MovieCharacter> getCharacterList()
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

    public static MovieCharacter getCharacterByName(String name)
    {
        Transaction tx;

        try (Session session = HibernateUtil.openSession()){

            tx = session.beginTransaction();

            String hql = " FROM MovieCharacter C WHERE C.name = :name";
            TypedQuery<MovieCharacter> query = session.createQuery(hql);
            query.setParameter("name", name);

            return query.getSingleResult();

        }catch (Exception ex){
            logger.warn(ex.getMessage());
            throw ex;
        }
    }

    public static void appendQuotes(String name, List<Quote> quotes)
    {
        MovieCharacter character = getCharacterByName(name);

        quotes.forEach(quote -> {
            character.addQuote(quote);
            quote.setHero(character);
        });


        Long result = -1L;
        Transaction tx = null;

        try (Session session = HibernateUtil.openSession()){

            tx = session.beginTransaction();

            session.merge(character);

        }catch (Exception ex){
            logger.warn(ex.getMessage());
            if (tx != null)
                // tx.rollback();
                throw ex;
        }finally {
//            tx.commit();
        }

    }
}
