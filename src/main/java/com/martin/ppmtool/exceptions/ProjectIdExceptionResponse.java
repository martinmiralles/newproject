package com.martin.ppmtool.exceptions;

public class ProjectIdExceptionResponse {

    private String projectIdentifier;

    public ProjectIdExceptionResponse(String projectIdentifier)
    {
        this.projectIdentifier = projectIdentifier;
    }

    //GETTERS AND SETTERS
    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
