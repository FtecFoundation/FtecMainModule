package com.ftec.repositories.implementations;

import com.ftec.entities.Statistic;
import com.ftec.entities.User;
import com.ftec.resources.Stocks;
import com.ftec.resources.TradeTypes;

import java.util.Date;
import java.util.List;

public interface StatisticDAO {
    void persist(Statistic stat);
    void update(Statistic stat);
    void saveStat(long userId, TradeTypes type, String pair, double commission, Stocks stock);
    List<Statistic> getLastStats(long userId, Date previousUpdate);
}
