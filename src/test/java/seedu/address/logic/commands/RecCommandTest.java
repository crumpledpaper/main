package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.RecCommand.MESSAGE_COMPLETED;
import static seedu.address.logic.commands.RecCommand.MESSAGE_REC;
import static seedu.address.logic.commands.RecCommand.MESSAGE_UE_LEFT;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.CommandHistory;
import seedu.address.model.GradTrak;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.util.SampleCourse;
import seedu.address.storage.moduleinfostorage.ModuleInfoManager;
import seedu.address.testutil.TypicalModuleTaken;

/**
 * Contains integration tests (interaction with the Model) for {@code RecCommand}.
 */
public class RecCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory;
    private final Course algoCourse = SampleCourse.COMPUTER_SCIENCE_ALGORITHMS;
    private final Course aiCourse = SampleCourse.COMPUTER_SCIENCE_AI;
    private final Course seCourse = SampleCourse.COMPUTER_SCIENCE_SOFTWARE_ENG;
    private final RecCommand command = new RecCommand();

    @Before
    public void setUp() {
        ModuleInfoManager moduleInfoManager = new ModuleInfoManager();
        Optional<ModuleInfoList> list;
        try {
            list = moduleInfoManager.readModuleInfoFile();
        } catch (DataConversionException dce) {
            System.err.println("Error reading json");
            return;
        }
        assert (list.isPresent());

        CourseList courseList = new CourseList();
        courseList.addCourse(algoCourse);
        courseList.addCourse(aiCourse);
        courseList.addCourse(seCourse);

        model = new ModelManager(new GradTrak(), new UserPrefs(),
                list.get(), courseList, new UserInfo());
        expectedModel = new ModelManager(new GradTrak(), new UserPrefs(),
                list.get(), courseList, new UserInfo());
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_emptyGradtrak() {
        String expectedMessage = String.format(MESSAGE_REC, 166);

        model.setCourse(algoCourse.getCourseName());
        expectedModel.setCourse(algoCourse.getCourseName());
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);

        model.setCourse(aiCourse.getCourseName());
        expectedModel.setCourse(aiCourse.getCourseName());
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);

        model.setCourse(seCourse.getCourseName());
        expectedModel.setCourse(seCourse.getCourseName());
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_completedGradtrak() {
        String expectedMessage = MESSAGE_COMPLETED;

        GradTrak algoGt = new GradTrak();
        algoGt.setModulesTaken(TypicalModuleTaken.getFullAlgoList());
        GradTrak aiGt = new GradTrak();
        aiGt.setModulesTaken(TypicalModuleTaken.getFullAiList());
        GradTrak seGt = new GradTrak();
        seGt.setModulesTaken(TypicalModuleTaken.getFullSeList());

        model.setCourse(algoCourse.getCourseName());
        model.setGradTrak(algoGt);
        expectedModel.setCourse(algoCourse.getCourseName());
        expectedModel.setGradTrak(algoGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());

        model.setCourse(aiCourse.getCourseName());
        model.setGradTrak(aiGt);
        expectedModel.setCourse(aiCourse.getCourseName());
        expectedModel.setGradTrak(aiGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());

        model.setCourse(seCourse.getCourseName());
        model.setGradTrak(seGt);
        expectedModel.setCourse(seCourse.getCourseName());
        expectedModel.setGradTrak(seGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());
    }

    @Test
    public void execute_ueRemainingGradtrak() {
        String expectedMessage = MESSAGE_UE_LEFT;

        List<ModuleTaken> algoList = TypicalModuleTaken.getFullAlgoList();
        algoList.removeAll(TypicalModuleTaken.getUeList());
        List<ModuleTaken> aiList = TypicalModuleTaken.getFullAiList();
        aiList.removeAll(TypicalModuleTaken.getUeList());
        List<ModuleTaken> seList = TypicalModuleTaken.getFullSeList();
        seList.removeAll(TypicalModuleTaken.getUeList());

        GradTrak algoGt = new GradTrak();
        algoGt.setModulesTaken(algoList);
        GradTrak aiGt = new GradTrak();
        aiGt.setModulesTaken(aiList);
        GradTrak seGt = new GradTrak();
        seGt.setModulesTaken(seList);

        model.setCourse(algoCourse.getCourseName());
        model.setGradTrak(algoGt);
        expectedModel.setCourse(algoCourse.getCourseName());
        expectedModel.setGradTrak(algoGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());

        model.setCourse(aiCourse.getCourseName());
        model.setGradTrak(aiGt);
        expectedModel.setCourse(aiCourse.getCourseName());
        expectedModel.setGradTrak(aiGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());

        model.setCourse(seCourse.getCourseName());
        model.setGradTrak(seGt);
        expectedModel.setCourse(seCourse.getCourseName());
        expectedModel.setGradTrak(seGt);
        expectedModel.updateRecModuleList();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getRecModuleListSorted());
    }
}
