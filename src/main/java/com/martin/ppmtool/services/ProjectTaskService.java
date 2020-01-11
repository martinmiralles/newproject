package com.martin.ppmtool.services;

import com.martin.ppmtool.domain.Backlog;
import com.martin.ppmtool.domain.Project;
import com.martin.ppmtool.domain.ProjectTask;
import com.martin.ppmtool.exceptions.ProjectNotFoundException;
import com.martin.ppmtool.repositories.BacklogRepository;
import com.martin.ppmtool.repositories.ProjectRepository;
import com.martin.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            //All ProjectTasks to be added to a specific project, and not be null (that it exists)
            //Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

            //A Backlog can only exist if the Project exists
            //Set the Backlog to a ProjectTask
            projectTask.setBacklog(backlog);

            //we want our project sequence to be sequential, ex. IDPRO-1 IDPRO-2
            Integer BacklogSequence = backlog.getPTSequence();

            //Update Backlog Sequence
            BacklogSequence++;

            //Sequence is then set
            backlog.setPTSequence((BacklogSequence));

            //Add Sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority, when priority is null
            if(projectTask.getPriority()==null||projectTask.getPriority()==0){//In the future, we need the projectTask.getPriority()==0 to handle the form
                projectTask.setPriority(3);
            }

            //INITIAL status, when status is null
            if(projectTask.getStatus()==""||projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

//        Project project = projectRepository.findByProjectIdentifier(id);
//
//        if(project==null){
//            throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist");
//        }

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String projectTask_id, String username){

        //make sure we are searching on an EXISTING Backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        //make sure that our Project Task exists

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTask_id);

        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task '" + projectTask_id + "' not found.");
        }

        //make sure that the Backlog/Project ID in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){

            throw new ProjectNotFoundException("Project Task '" + projectTask_id + "' does not exist in project '" + backlog_id + "'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String projectTask_id, String username){

        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectTask_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String projectTask_id, String username){

        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectTask_id, username);

        projectTaskRepository.delete(projectTask);
    }
}
