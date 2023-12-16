package com.ultimaspin.retrodev;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

public class LeagueDao {

    private final SessionFactory sessionFactory;

    public LeagueDao() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

            // These two lines here are needed because for some strange reason it seems to initialise something
            // Without it, later on trying to use the sessionFactory to create sessions results in an error
            // Don't believe me? Try removing it ;)
            Session session = sessionFactory.openSession();
            session.close();
        }
        finally {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }


    public List<League> getLeagues() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<League> cr = cb.createQuery(League.class);
            Root<League> root = cr.from(League.class);
            cr.select(root);

            return session.createQuery(cr).list();
        }
    }

    public static void main(String[] args) {
//        LeagueDao dao = new LeagueDao();
        new LeagueDao().getLeagues().forEach(it -> System.out.println(it.getLeagueName()));
//        foo();
    }

    private static void foo() {
        SessionFactory sessionFactory = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            e.printStackTrace();
        }
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<League> cr = cb.createQuery(League.class);
        Root<League> root = cr.from(League.class);
        cr.select(root);

        List<League> result = session.createQuery(cr).list();
        result.forEach(it -> System.out.println(it.getLeagueName()));
    }


}
