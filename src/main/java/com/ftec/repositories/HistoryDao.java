package com.ftec.repositories;


import com.ftec.entities.User;

public interface HistoryDao {
	void addHistoryToUser(User u, double amount, String reason);
	void addHistoryToUser(long user_id, double balance, double amount, String reason);
}
