package com.marco.labs.ppmapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marco.labs.ppmapp.domain.ProjectTask;


@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long>{
	
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
	

	//    ProjectTask findByProjectSequence(String sequence);
	List<ProjectTask> findByProjectSequence(String projectsequence);
}
