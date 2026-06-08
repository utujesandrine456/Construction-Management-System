package com.example.cms.service;

import com.example.cms.dto.request.TaskRequest;
import com.example.cms.dto.response.TaskResponse;
import com.example.cms.entity.Project;
import com.example.cms.entity.Stage;
import com.example.cms.entity.Task;
import com.example.cms.entity.User;
import com.example.cms.enums.Role;
import com.example.cms.enums.TaskStatus;
import com.example.cms.repository.StageRepository;
import com.example.cms.repository.TaskRepository;
import com.example.cms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StageRepository stageRepository;

    @InjectMocks
    private TaskService taskService;

    private User manager;
    private User worker;
    private Stage stage;

    @BeforeEach
    void setUp() {
        manager = User.builder().id(1L).fullName("Manager").role(Role.MANAGER).build();
        worker = User.builder().id(2L).fullName("Worker").role(Role.WORKER).build();
        Project project = Project.builder().id(1L).name("House").build();
        stage = Stage.builder().id(1L).name("Foundation").project(project).build();
    }

    @Test
    void createTask_Success() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Dig trenches");
        request.setAssignedWorkerId(2L);
        request.setStageId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(worker));
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));

        Task savedTask = Task.builder()
                .id(1L).title("Dig trenches").assignedWorker(worker)
                .assignedByManager(manager).stage(stage).status(TaskStatus.PENDING).build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request, 1L);

        assertNotNull(response);
        assertEquals("Dig trenches", response.getTitle());
        assertEquals("Worker", response.getAssignedWorkerName());
        assertEquals(TaskStatus.PENDING, response.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ThrowsException_IfWorkerNotWorkerRole() {
        User invalidWorker = User.builder().id(3L).role(Role.MANAGER).build();
        TaskRequest request = new TaskRequest();
        request.setAssignedWorkerId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(3L)).thenReturn(Optional.of(invalidWorker));

        assertThrows(RuntimeException.class, () -> taskService.createTask(request, 1L));
        verify(taskRepository, never()).save(any(Task.class));
    }
}
