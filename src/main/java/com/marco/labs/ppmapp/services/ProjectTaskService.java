package com.marco.labs.ppmapp.services;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;

import com.marco.labs.ppmapp.domain.Backlog;
import com.marco.labs.ppmapp.domain.Project;
import com.marco.labs.ppmapp.domain.ProjectTask;
import com.marco.labs.ppmapp.exceptions.ProjectNotFoundException;
import com.marco.labs.ppmapp.repositories.BacklogRepository;
import com.marco.labs.ppmapp.repositories.ProjectRepository;
import com.marco.labs.ppmapp.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		
			//ProjectTask to be added to a specific project, project != null, Backlog exists
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
			
			//set the Backlog to Project
			projectTask.setBacklog(backlog);
			//We want our projectSequence to be like this: IDPRO-1 IDPRO-2
			Integer BacklogSequence = backlog.getPTSequence();
			//Update the Backlog sequence
			BacklogSequence++;
			backlog.setPTSequence(BacklogSequence);
			//Add Sequence to ProjectTask
			projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			//Initial status when status is null
			if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}	
			//Initial priority when priority is null
			if(projectTask.getPriority() == null || projectTask.getPriority() == 0) { // In the future we need projectTask.getPriority() === 0 to handle the form
				projectTask.setPriority(3);
			}
			return projectTaskRepository.save(projectTask);
	}
	
	public Iterable<ProjectTask> findBacklogById(String id, String username){
		projectService.findProjectByIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findByProjectSequence(String backlog_id, String projectTask_id, String username) {
		//Make sure we are searching on the right Backlog
		projectService.findProjectByIdentifier(backlog_id, username);
		
		//make sur that the projectTask exists

		List<ProjectTask> projectTasks = projectTaskRepository.findByProjectSequence(projectTask_id);
		
		
		if(projectTasks.isEmpty()) {
			throw new ProjectNotFoundException("Project Task '" + projectTask_id +"' not found");
		}
		
		//make sure that the backlog/project id in the path corresponds to the right project 
		if(!projectTasks.get(0).getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '" + projectTask_id + "' does not exist in project: '" + backlog_id);
		}
		return projectTasks.get(0);
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlog_id, String projectTask_id, String username) {
		ProjectTask projectTask = findByProjectSequence(backlog_id, projectTask_id, username);
		
		projectTask = updatedProjectTask;
		return projectTaskRepository.save(projectTask);
	}


	public void deleteProjectTaskByProjectSequence(String backlog_id, String projectTask_id, String username){
        ProjectTask projectTask = findByProjectSequence(backlog_id, projectTask_id, username);

        Backlog backlog = projectTask.getBacklog();
        List<ProjectTask> projectTasks = backlog.getProjectTasks();
        projectTasks.remove(projectTask);
        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
	}
	//Update project task

    //find existing project task

    //replace it with updated task

	//save update
}
