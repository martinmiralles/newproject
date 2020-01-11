package com.martin.ppmtool.services;

import com.martin.ppmtool.domain.Backlog;
import com.martin.ppmtool.domain.Project;
import com.martin.ppmtool.domain.User;
import com.martin.ppmtool.exceptions.ProjectIdException;
import com.martin.ppmtool.exceptions.ProjectNotFoundException;
import com.martin.ppmtool.repositories.BacklogRepository;
import com.martin.ppmtool.repositories.ProjectRepository;
import com.martin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username)
    {

        // New Project: project.getId == null
        // Existing Project: project.getId != null

        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            //checking if Project exists AND if the project belongs to the logged user
            if(existingProject != null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project does not belong to your account");
            } else if(existingProject==null){
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated because it doesn't exist");
            }
        }

        //LOGIC
        try
        {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null)
            {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null)
            {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, String username)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null)
        {
            throw new ProjectIdException("Project ID: '" + projectId + "' does not exist");
        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project does not belong to your account");
        }

        //If successful and a project if found
        return project;
    }

    public Iterable<Project> findAllProjects(String username)
    {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier (String projectId, String username)
    {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
