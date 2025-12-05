package core.dao;

import core.HibernateUtil;
import core.model.Trabajador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TrabajadorDAO {

    public void save(Trabajador t) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(t);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Trabajador> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Trabajador> lista;

        try {
            TypedQuery<Trabajador> query = em.createQuery("FROM Trabajador", Trabajador.class);
            lista = query.getResultList();
        } finally {
            em.close();
        }

        return lista;
    }
}
