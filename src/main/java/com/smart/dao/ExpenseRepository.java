package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.smart.entities.Expense;

import jakarta.transaction.Transactional;


public interface ExpenseRepository extends JpaRepository<Expense, Integer>{
	
	@Modifying
	@Transactional
	@Query(value="delete from Expense e where e.eId = ?1")
	void deleteByIdCustom(Integer eId);

}
