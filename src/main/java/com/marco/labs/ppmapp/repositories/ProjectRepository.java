package com.marco.labs.ppmapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marco.labs.ppmapp.domain.Project;
import java.lang.String;
import java.util.List;
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{


	List<Project> findByProjectIdentifier(String projectidentifier);
	
	@Override
	Iterable<Project> findAll();

	Iterable<Project> findAllByProjectLeader(String username);
}
