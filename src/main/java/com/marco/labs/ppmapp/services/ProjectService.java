package com.marco.labs.ppmapp.services;

 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marco.labs.ppmapp.domain.Backlog;
import com.marco.labs.ppmapp.domain.Project;
import com.marco.labs.ppmapp.domain.User;
import com.marco.labs.ppmapp.exceptions.ProjectIdException;
import com.marco.labs.ppmapp.exceptions.ProjectNotFoundException;
import com.marco.labs.ppmapp.repositories.BacklogRepository;
import com.marco.labs.ppmapp.repositories.ProjectRepository;
import com.marco.labs.ppmapp.repositories.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project, String username) {
		//project.getId != null
		//find by DB id -> null
		
		if(project.getId() != null) {

			List<Project> projects =  projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			//Project existingProject =
			if(!projects.isEmpty() && !projects.get(0).getUser().getUsername().equals(username)) {
				throw new ProjectNotFoundException("Project not found in your account");
			}else if(projects.isEmpty()){ 
				throw new ProjectNotFoundException("Project with ID: '"+ project.getProjectIdentifier() + "' cannot be updated because it doesnt exists");
			}
		}
		
		
		try {
			User user = userRepository.findByUsername(username);
			
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			return projectRepository.save(project);
		}catch(Exception e) {
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}
	}
	
 
	public Project findProjectByIdentifier(String projectIdentifier, String username ) {
		//Only want to return the project if the user looking for it is the owner
		List<Project> projects = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		
		if(projects.isEmpty()) {
			throw new ProjectIdException("Project IDENTIFIER '" + projectIdentifier.toUpperCase() + "' does not exists");
		}
		Project project = projects.get(0);//project.projectIdentifier est UNIQUE donc projects.size == 1
		if(!project.getProjectLeader().equals(username)){  
			throw new ProjectNotFoundException("Project not found in your account");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects(String username){
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByIdentifier(String projectId, String username) {
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
	
	
	
	
	
	
	
	
	
	
}
