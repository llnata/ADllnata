package core.dao;

import core.HibernateUtil;
import core.model.Fichaje;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FichajeDAO {

    public void save(Fichaje f) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(f);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Fichaje> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Fichaje> lista;

        try {
            TypedQuery<Fichaje> query =
                    em.createQuery("FROM Fichaje", Fichaje.class);
            lista = query.getResultList();
        } finally {
            em.close();
        }

        return lista;
    }
}

