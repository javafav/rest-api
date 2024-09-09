package com.unit.conversion.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unit.conversion.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User  findByUserName(String userName);
}
