package com.marco.labs.ppmapp.exceptions;

public class ProjectNotFoundExceptionResponse {
	
	private String ProjectNotFound;

	ProjectNotFoundExceptionResponse(String message){
		this.ProjectNotFound = message;
	}
	public String getProjectNotFound() {
		return ProjectNotFound;
	}

	public void setProjectNotFound(String projectNotFound) {
		ProjectNotFound = projectNotFound;
	}
	

}
