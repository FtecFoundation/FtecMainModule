package com.ftec.repositories.interfaces;

import com.ftec.entities.Statistic;
import com.ftec.repositories.implementations.StatisticDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import com.ftec.resources.TradeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class StatisticDAOImpl implements StatisticDAO{
    @PersistenceContext
    private EntityManager entityManager;
    private final Resources resources;

    @Autowired
    public StatisticDAOImpl(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void persist(Statistic stat) {
        entityManager.persist(stat);
    }

    @Override
    public void update(Statistic stat) {
        entityManager.persist(stat);
    }

    @Override
    public void saveStat(long userId, TradeTypes type, String pair, double commission, Stocks stock) {
        Statistic stat = new Statistic();
        stat.setCommission(commission);
        stat.setPair(pair);
        stat.setTxDate(new Date());
        stat.setType(type);
        stat.setUser(userId);
        stat.setStock(stock);
        entityManager.persist(stat);
    }

    @Override
    public List<Statistic> getLastStats(long userId, Date previousUpdate) {
        TypedQuery<Statistic> query = entityManager.createQuery("select stat from Statistic stat where stat.user=:userId and stat.txDate>:lastDate order by stat.txDate desc", Statistic.class);
        query.setParameter("userId", userId);
        query.setParameter("lastDate", previousUpdate);
        query.setMaxResults(resources.getPaginationRescordsPerPage());
        return query.getResultList();
    }
}
