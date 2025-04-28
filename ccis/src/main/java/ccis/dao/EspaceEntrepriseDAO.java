package ccis.dao;

import ccis.models.EspaceEntreprise;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EspaceEntrepriseDAO {
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public void save(EspaceEntreprise espaceEntreprise) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(espaceEntreprise);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public EspaceEntreprise getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(EspaceEntreprise.class, id);
        }
    }

    public List<EspaceEntreprise> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from espace_entreprise", EspaceEntreprise.class).list();
        }
    }

    public void update(EspaceEntreprise espaceEntreprise) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(espaceEntreprise);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(EspaceEntreprise espaceEntreprise) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(espaceEntreprise);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    public long count() {
        try (Session session = sessionFactory.openSession()) {
            return (long) session.createQuery("select count(*) from espace_entreprise").uniqueResult();
        }
    }
}
