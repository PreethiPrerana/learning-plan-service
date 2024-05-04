package com.thbs.lms.testService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import com.thbs.lms.controller.ModuleController;
import com.thbs.lms.exception.DuplicateModuleException;
import com.thbs.lms.exception.InvalidModuleDataException;
import com.thbs.lms.exception.InvalidTrainerException;
import com.thbs.lms.exception.InvalidTypeException;
import com.thbs.lms.exception.ModuleNotFoundException;
import com.thbs.lms.model.Course;
import com.thbs.lms.model.LearningPlan;
import com.thbs.lms.model.Module;
import com.thbs.lms.repository.ModuleRepository;
import com.thbs.lms.service.ModuleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModuleController moduleController;

    @InjectMocks
    private ModuleService moduleService;

    private Module module, module2;
    private Course course;
    private LearningPlan learningPlan;

    @BeforeEach
    void setUp() {
        // Mocking a module object
        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Test Course");
    
        learningPlan = new LearningPlan();
        learningPlan.setLearningPlanId(1L);
    
        module = new Module();
        module.setModuleId(1L);
        module.setStartDate(new Date());
        module.setEndDate(new Date());
        module.setTrainer("Test Trainer"); // Corrected trainer value
        module.setCourse(course);
        module.setLearningPlan(learningPlan);
    
        module2 = new Module();
        module2.setModuleId(2L);
        module2.setStartDate(new Date());
        module2.setEndDate(new Date());
        module2.setTrainer("Test Trainer2");
        module2.setCourse(course);
        module2.setLearningPlan(learningPlan);
    }
    


    @Test
    void testSaveModule_Success() {
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        Module createdModule = moduleService.saveModule(module);

        assertNotNull(createdModule);
        assertEquals(module, createdModule);
    }

    @Test
    void testSaveModule_NullStartDate() {
        module.setStartDate(null);

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.saveModule(module);
        });
    }

    @Test
    void testSaveModule_NullEndDate() {
        module.setEndDate(null);

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.saveModule(module);
        });
    }

    @Test
    void testSaveModule_EmptyTrainer() {
        module.setTrainer("");

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.saveModule(module);
        });
    }

    @Test
    void testSaveModule_NullTrainer() {
        module.setTrainer(null);

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.saveModule(module);
        });
    }

    

    @Test
    void testSaveModule_NullCourse() {
        module.setCourse(null);

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.saveModule(module);
        });
    }


    @Test
    void testSaveModule_DuplicateEntry() {
        when(moduleRepository.findByLearningPlanLearningPlanIdAndCourse(anyLong(), any(Course.class))).thenReturn(Optional.of(module));

        assertThrows(DuplicateModuleException.class, () -> {
            moduleService.saveModule(module);
        });
    }

    @Test
    void testSaveAllModule_Success() {
        List<Module> modules = new ArrayList<>();
    
        modules.add(module);
        modules.add(module2);
    
        // Mocking the saveAll method to return the input list of modules
        when(moduleRepository.saveAll(modules))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Return the input list as it is
    
        // Calling the method under test
        List<Module> savedModules = moduleService.saveAllModules(modules);
    
        // Asserting that the number of saved modules matches the input list size
        assertEquals(2, savedModules.size());
    
        // Verifying that the saveAll method is called once with the input list
        verify(moduleRepository, times(1)).saveAll(modules);
    }
    

    
//    @Test
// void testSaveAllModule_ExceptionThrown() {
//     List<Module> modules = new ArrayList<>();

//     modules.add(module);
//     modules.add(module2);

//     // Mock the saveAll method to throw an exception when the second module is saved
//     when(moduleRepository.saveAll(anyList()))
//             .thenReturn(Arrays.asList(module))  // Return successfully saved modules
//             .thenThrow(new DuplicateModuleException("Duplicate module"));

//     // Ensure that a DuplicateModuleException is thrown and no modules are saved
//     assertThrows(DuplicateModuleException.class, () -> moduleService.saveAllModules(modules));
//     verify(moduleRepository, times(1)).saveAll(anyList()); // Verify only one attempt to save all modules
// }

    

    @Test
    void testSaveAllModule_EmptyList() {
        List<Module> modules = new ArrayList<>();

        List<Module> savedModule = moduleService.saveAllModules(modules);

        assertEquals(0, savedModule.size());
        verify(moduleRepository, never()).save(any(Module.class));
    }

    @Test
    void testGetAllModules_Success() {
        List<Module> expectedModule = new ArrayList<>();

        expectedModule.add(new Module());

        when(moduleRepository.findAll()).thenReturn(expectedModule);

        List<Module> actualModule = moduleService.getAllModules();

        assertEquals(expectedModule.size(), actualModule.size());
    }

    @Test
    void testGetAllModulesByLearningPlanId_Success() {
        when(moduleRepository.findByLearningPlanLearningPlanId(anyLong()))
                .thenReturn(Arrays.asList(module));

        List<Module> modules = moduleService.getAllModulesByLearningPlanId(1L);

        assertNotNull(modules);
        assertEquals(1, modules.size());
        assertEquals(module.getModuleId(), modules.get(0).getModuleId());
    }

    

    @Test
    void testGetAllModulesByTrainer_Success() {
        when(moduleRepository.findByTrainer(anyString())).thenReturn(Arrays.asList(module));

        List<Module> modules = moduleService.getAllModulesByTrainer("Test Trainer");

        assertNotNull(modules);
        assertEquals(1, modules.size());
        assertEquals(module.getModuleId(), modules.get(0).getModuleId());
    }

    @Test
    void testGetAllModulesByTrainer_NullTrainer() {
        assertThrows(InvalidTrainerException.class, () -> {
            moduleService.getAllModulesByTrainer(null);
        });
    }

    @Test
    void testGetAllModulesByTrainer_EmptyTrainer() {
        assertThrows(InvalidTrainerException.class, () -> {
            moduleService.getAllModulesByTrainer("");
        });
    }

    @Test
    void testUpdateModuleTrainer_Success() {
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.of(module));
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        String newTrainer = "New Trainer";
        moduleService.updateModuleTrainer(1L, newTrainer);

        assertEquals(newTrainer, module.getTrainer());
    }

    @Test
    void testUpdateModuleTrainer_NullTrainer() {
        assertThrows(InvalidTrainerException.class, () -> {
            moduleService.updateModuleTrainer(1L, null);
        });
    }

    @Test
    void testUpdateModuleTrainer_EmptyTrainer() {
        assertThrows(InvalidTrainerException.class, () -> {
            moduleService.updateModuleTrainer(1L, "");
        });
    }

    @Test
    void testUpdateModuleTrainer_moduleNotFound() {
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> {
            moduleService.updateModuleTrainer(2L, "New Trainer");
        });
    }

    @Test
    void testUpdateModuleDates_Success() {
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.of(module));
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 1000 * 60 * 60 * 24); // Adding one day

        Optional<Module> updatedModule = moduleService.updateModuleDates(1L, startDate,
                endDate);

        assertTrue(updatedModule.isPresent());
        assertEquals(startDate, updatedModule.get().getStartDate());
        assertEquals(endDate, updatedModule.get().getEndDate());
    }

    @Test
    void testUpdateModuleDates_NullStartDate() {
        Date date = new Date();
        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.updateModuleDates(1L, null, date);
        });
    }

    @Test
    void testUpdateModuleDates_NullEndDate() {
        Date date = new Date();
        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.updateModuleDates(1L, date, null);
        });
    }

    @Test
    void testUpdateModuleDates_EndDateBeforeStartDate() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 1000 * 60 * 60 * 24); // Subtracting one day

        assertThrows(InvalidModuleDataException.class, () -> {
            moduleService.updateModuleDates(1L, startDate, endDate);
        });
    }

    @Test
    void testUpdateModuleDates_moduleNotFound() {
        Date date = new Date();
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> {
            moduleService.updateModuleDates(2L, date, date);
        });
    }

    @Test
    void testDeleteModulesByLearningPlanId_Success() {
        List<Module> modules = new ArrayList<>();

        modules.add(module);
        modules.add(module2);

        when(moduleRepository.findByLearningPlanLearningPlanId(anyLong())).thenReturn(modules);

        when(moduleRepository.findById(anyLong())).thenReturn(Optional.of(module));
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.of(module2));

        assertDoesNotThrow(() -> {
            moduleService.deleteModulesByLearningPlanId(1L);
        });
    }

    @Test
    void testDeleteModulesByLearningPlanId_EmptyList() {
        when(moduleRepository.findByLearningPlanLearningPlanId(anyLong())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> {
            moduleService.deleteModulesByLearningPlanId(2L);
        });
    }

    @Test
    void testDeleteModules_Success() {
        List<Module> modules = Arrays.asList(module);

        assertDoesNotThrow(() -> {
            moduleService.deleteModules(modules);
        });
    }

    @Test
    void testDeleteModules_EmptyList() {
        List<Module> modules = new ArrayList<>();

        assertDoesNotThrow(() -> {
            moduleService.deleteModules(modules);
        });
    }

    @Test
    void testDeleteModule_Success() {
        when(moduleRepository.findById(1L)).thenReturn(Optional.of(module));

        moduleService.deleteModule(1L);

        verify(moduleRepository, times(1)).delete(module);
    }

    @Test
    void testDeleteModule_moduleNotFound() {
        when(moduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> {
            moduleService.deleteModule(2L);
        });
    }
}
