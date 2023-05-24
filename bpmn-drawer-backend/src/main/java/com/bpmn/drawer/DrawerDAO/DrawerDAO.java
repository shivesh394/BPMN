package com.bpmn.drawer.DrawerDAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.bpmn.drawer.entity.File;



	@EnableJpaRepositories
	@Repository
	public interface DrawerDAO extends JpaRepository<File, Integer>{
		
		
	}

