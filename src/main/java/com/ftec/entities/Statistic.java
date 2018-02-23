package com.ftec.entities;

import com.ftec.resources.Stocks;
import com.ftec.resources.TradeTypes;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date txDate;

    private TradeTypes type;

    private String pair;

    private double commission;
    private Stocks stock;

    private long user;

    public Stocks getStock() {
        return stock;
    }

    public void setStock(Stocks stock) {
        this.stock = stock;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTxDate() {
        return txDate;
    }

    public void setTxDate(Date sell_date) {
        this.txDate = sell_date;
    }

    public TradeTypes getType() {
        return type;
    }

    public void setType(TradeTypes type) {
        this.type = type;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double comission) {
        this.commission = comission;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", sell_date=" + txDate +
                ", type=" + type +
                ", pair='" + pair + '\'' +
                ", commission=" + commission +
                ", stock=" + stock +
                ", user=" + user +
                '}';
    }
}