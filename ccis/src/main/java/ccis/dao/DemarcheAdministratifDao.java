package ccis.dao;

import ccis.models.DemarcheAdministratif;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DemarcheAdministratifDao {

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void update(DemarcheAdministratif demarche) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(demarche);
            tx.commit();
        }
    }

    public DemarcheAdministratif findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(DemarcheAdministratif.class, id);
        }
    }

    public List<DemarcheAdministratif> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from demarche_administratif", DemarcheAdministratif.class).list();
        }
    }

    public void delete(DemarcheAdministratif demarche) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(demarche);
            tx.commit();
        }
    }

    public void deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            DemarcheAdministratif demarche = session.get(DemarcheAdministratif.class, id);
            if (demarche != null) {
                session.delete(demarche);
            }
            tx.commit();
        }
    }
    public long count() {
        try (Session session = sessionFactory.openSession()) {
            return (long) session.createQuery("select count(*) from demarche_administratif").uniqueResult();
        }
    }
}
