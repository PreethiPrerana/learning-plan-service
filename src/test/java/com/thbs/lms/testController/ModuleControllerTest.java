package com.thbs.lms.testController;

import com.thbs.lms.controller.ModuleController;
import com.thbs.lms.model.Module;
import com.thbs.lms.service.ModuleService;
import com.thbs.lms.utility.DateRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class moduleControllerTest {

    @Mock
    private ModuleService moduleService;

    @InjectMocks
    private ModuleController moduleController;

    @Test
    void testCreatemodule() {
        Module module = new Module();

        when(moduleService.saveModule(any())).thenReturn(module);

        ResponseEntity<?> responseEntity = moduleController.createModule(module);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(module, responseEntity.getBody());

        verify(moduleService, times(1)).saveModule(any());
    }

    @Test
    void testCreatemodules() {
        List<Module> modules = new ArrayList<>();

        Module module1 = new Module();
        module1.setModuleId(1L); // Set an ID for testing

        Module module2 = new Module();
        module2.setModuleId(2L); // Set an ID for testing

        modules.add(module1);
        modules.add(module2);

        when(moduleService.saveAllModules(modules)).thenReturn(modules);

        ResponseEntity<?> responseEntity = moduleController.createModules(modules);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(modules, responseEntity.getBody());
    }

    @Test
    void testGetAllmodules() {
        List<Module> expectedPaths = new ArrayList<>();

        expectedPaths.add(new Module());

        when(moduleService.getAllModules()).thenReturn(expectedPaths);

        ResponseEntity<?> responseEntity = moduleController.getAllModules();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPaths, responseEntity.getBody());
    }

    @Test
    void testGetAllmodulesByLearningPlanId() {
        Long learningPlanId = 1L;

        List<Module> modules = new ArrayList<>();

        when(moduleService.getAllModulesByLearningPlanId(learningPlanId))
                .thenReturn(modules);

        ResponseEntity<?> responseEntity = moduleController
                .getAllModulesByLearningPlanId(learningPlanId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(modules, responseEntity.getBody());

        verify(moduleService, times(1)).getAllModulesByLearningPlanId(learningPlanId);
    }


    @Test
    void testGetAllLearningPlansByTrainer() {
        String trainer = "Test Trainer";

        List<Module> modules = new ArrayList<>();

        when(moduleService.getAllModulesByTrainer(trainer)).thenReturn(modules);

        ResponseEntity<?> responseEntity = moduleController.getAllModulesByTrainer(trainer);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(modules, responseEntity.getBody());

        verify(moduleService, times(1)).getAllModulesByTrainer(trainer);
    }

    @Test
    void testUpdateTrainer() {
        Long ModuleId = 1L;
        String newTrainer = "New Trainer";

        ResponseEntity<?> responseEntity = moduleController.updateTrainer(ModuleId, newTrainer);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainer updated successfully", responseEntity.getBody());

        verify(moduleService, times(1)).updateModuleTrainer(ModuleId, newTrainer);
    }

    @Test
    void testUpdateLearningPlanDates() {
        Long moduleID = 1L;

        DateRange dateRange = new DateRange();
        dateRange.setStartDate(new Date());
        dateRange.setEndDate(new Date());

        when(moduleService.updateModuleDates(moduleID, dateRange.getStartDate(),
                dateRange.getEndDate()))
                .thenReturn(Optional.of(new Module()));

        ResponseEntity<?> responseEntity = moduleController.updateDates(moduleID,
                dateRange);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Date Range updated successfully.", responseEntity.getBody());
        verify(moduleService, times(1)).updateModuleDates(moduleID,
                dateRange.getStartDate(),
                dateRange.getEndDate());
    }

    @Test
    void testDeletemodule() {
        doNothing().when(moduleService).deleteModule(1L);

        ResponseEntity<?> responseEntity = moduleController.deletemodule(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Module deleted successfully", responseEntity.getBody());
    }
}
