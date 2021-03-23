package com.ssafy.yourwine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yourwine.model.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Integer>{

	Food findByFoodId(Integer foodId);
	
}
