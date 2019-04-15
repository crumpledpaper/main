package seedu.address.model.recmodule;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.model.util.SampleCourseRequirement.SCIENCE_REGEX;
import static seedu.address.testutil.TypicalModuleTaken.CS2101;
import static seedu.address.testutil.TypicalModuleTaken.CS2103T;
import static seedu.address.testutil.TypicalModuleTaken.GER1000;

import java.util.List;

import org.junit.Test;

import seedu.address.model.GradTrak;
import seedu.address.model.course.Course;
import seedu.address.model.util.SampleCourse;
import seedu.address.testutil.GradTrakBuilder;
import seedu.address.testutil.ModuleTakenBuilder;
import seedu.address.testutil.RecModuleBuilder;
import seedu.address.testutil.TypicalModuleTaken;

public class RecModulePredicateTest {

    private final Course algoCourse = SampleCourse.COMPUTER_SCIENCE_ALGORITHMS;
    private final Course aiCourse = SampleCourse.COMPUTER_SCIENCE_AI;
    private final Course seCourse = SampleCourse.COMPUTER_SCIENCE_SOFTWARE_ENG;
    private final GradTrak emptyGradTrak = new GradTrak();
    private final RecModuleBuilder rmb = new RecModuleBuilder();
    private final List<RecModule> allModules = rmb.getAllModules();

    @Test
    public void equals() {
        GradTrak gt1 = new GradTrakBuilder().withPerson(CS2103T).build();
        GradTrak gt2 = new GradTrakBuilder().withPerson(CS2103T).withPerson(CS2101).build();

        /* same course and gradtrak -> returns true */
        RecModulePredicate rmp1 = new RecModulePredicate(algoCourse, gt1);
        RecModulePredicate rmp2 = new RecModulePredicate(algoCourse, gt1);
        assertTrue(rmp1.equals(rmp2));

        /* different course -> returns false */
        rmp1 = new RecModulePredicate(algoCourse, emptyGradTrak);
        rmp2 = new RecModulePredicate(aiCourse, emptyGradTrak);
        assertFalse(rmp1.equals(rmp2));

        /* different gradtrak -> returns false */
        rmp1 = new RecModulePredicate(algoCourse, emptyGradTrak);
        rmp2 = new RecModulePredicate(algoCourse, gt1);
        assertFalse(rmp1.equals(rmp2));
        rmp1 = new RecModulePredicate(algoCourse, gt2);
        assertFalse(rmp1.equals(rmp2));
    }

    @Test
    public void test_ineligibleModule_returnsFalse() {
        /* module without prerequisites but already in GradTrak -> returns false */
        GradTrak gt = new GradTrakBuilder().withPerson(GER1000).build();
        RecModulePredicate rmp = new RecModulePredicate(algoCourse, gt);
        RecModule rm = rmb.create("GER1000");
        assertFalse(rmp.test(rm));

        /* module not in GradTrak but prerequisites not met -> returns false */
        rmp = new RecModulePredicate(algoCourse, emptyGradTrak);
        rm = rmb.create("CS3230");
        assertFalse(rmp.test(rm));
    }

    @Test
    public void test_completedRequirements_returnsFalse() {
        GradTrak algoGt = new GradTrak();
        algoGt.setModulesTaken(TypicalModuleTaken.getFullAlgoList());
        GradTrak aiGt = new GradTrak();
        aiGt.setModulesTaken(TypicalModuleTaken.getFullAiList());
        GradTrak seGt = new GradTrak();
        seGt.setModulesTaken(TypicalModuleTaken.getFullSeList());
        RecModulePredicate algoRmp = new RecModulePredicate(algoCourse, algoGt);
        RecModulePredicate aiRmp = new RecModulePredicate(aiCourse, aiGt);
        RecModulePredicate seRmp = new RecModulePredicate(seCourse, seGt);

        for (RecModule rm : allModules) {
            assertFalse(algoRmp.test(rm));
            assertFalse(aiRmp.test(rm));
            assertFalse(seRmp.test(rm));
        }
    }

    @Test
    public void test_eligibleModuleForEmptyGradtrak_returnsTrue() {
        RecModulePredicate algoRmp = new RecModulePredicate(algoCourse, emptyGradTrak);
        RecModulePredicate aiRmp = new RecModulePredicate(aiCourse, emptyGradTrak);
        RecModulePredicate seRmp = new RecModulePredicate(seCourse, emptyGradTrak);

        for (RecModule rm : rmb.getRecModulesForEmptyGradTrak()) {
            assertTrue(algoRmp.test(rm));
            assertTrue(aiRmp.test(rm));
            assertTrue(seRmp.test(rm));
        }
    }

    @Test
    public void test_ueModule_returnsFalse() {
        RecModulePredicate algoRmp = new RecModulePredicate(algoCourse, emptyGradTrak);
        RecModulePredicate aiRmp = new RecModulePredicate(aiCourse, emptyGradTrak);
        RecModulePredicate seRmp = new RecModulePredicate(seCourse, emptyGradTrak);

        for (RecModule rm : rmb.getUeModules()) {
            assertFalse(algoRmp.test(rm));
            assertFalse(aiRmp.test(rm));
            assertFalse(seRmp.test(rm));
        }
    }

    @Test
    public void test_geModule() {
        GradTrak gehGt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("GEH1001").build()).build();
        GradTrak geqGt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("GEQ1000").build()).build();
        GradTrak gerGt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("GER1000").build()).build();
        GradTrak gesGt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("GES1002").build()).build();
        GradTrak getGt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("GET1001").build()).build();
        RecModulePredicate gehRmp = new RecModulePredicate(algoCourse, gehGt);
        RecModulePredicate geqRmp = new RecModulePredicate(algoCourse, geqGt);
        RecModulePredicate gerRmp = new RecModulePredicate(algoCourse, gerGt);
        RecModulePredicate gesRmp = new RecModulePredicate(algoCourse, gesGt);
        RecModulePredicate getRmp = new RecModulePredicate(algoCourse, getGt);

        for (RecModule rm : allModules) {
            String code = rm.getCode().toString();
            if (code.contains("GEH")) {
                assertFalse(gehRmp.test(rm));
                assertTrue(geqRmp.test(rm));
                assertTrue(gerRmp.test(rm));
                assertTrue(gesRmp.test(rm));
                assertTrue(getRmp.test(rm));
            } else if (code.contains("GEQ")) {
                assertFalse(geqRmp.test(rm));
                assertTrue(gehRmp.test(rm));
                assertTrue(gerRmp.test(rm));
                assertTrue(gesRmp.test(rm));
                assertTrue(getRmp.test(rm));
            } else if (code.contains("GER")) {
                assertFalse(gerRmp.test(rm));
                assertTrue(geqRmp.test(rm));
                assertTrue(gehRmp.test(rm));
                assertTrue(gesRmp.test(rm));
                assertTrue(getRmp.test(rm));
            } else if (code.contains("GES")) {
                assertFalse(gesRmp.test(rm));
                assertTrue(geqRmp.test(rm));
                assertTrue(gehRmp.test(rm));
                assertTrue(gerRmp.test(rm));
                assertTrue(getRmp.test(rm));
            } else if (code.contains("GET") && !code.equals("GET1021")) {
                assertFalse(getRmp.test(rm));
                assertTrue(geqRmp.test(rm));
                assertTrue(gehRmp.test(rm));
                assertTrue(gerRmp.test(rm));
                assertTrue(gesRmp.test(rm));
            }
        }
    }

    @Test
    public void test_facModule() {
        GradTrak gt = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("PC1144").build()).build();

        RecModulePredicate rmp1 = new RecModulePredicate(algoCourse, gt);
        RecModulePredicate rmp2 = new RecModulePredicate(algoCourse, emptyGradTrak);

        for (RecModule rm : allModules) {
            if (rm.getCode().toString().matches(SCIENCE_REGEX)) {
                assertFalse(rmp1.test(rm));
                if (!rm.getCode().toString()
                        .matches("MA2213|MA2214|MA2104|MA2101|MA2108|MA2501|ST2132|ST2137")) {
                    assertTrue(rmp2.test(rm));
                }
            }
        }
    }

    @Test
    public void test_ieModule() {
        GradTrak gt1 = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CP3880").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2101").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS3103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS2103").build()).build();
        GradTrak gt2 = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS4010").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2101").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS3103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS2103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build()).build();
        GradTrak gt3 = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CP3200").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CP3202").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2101").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS3103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS2103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build()).build();
        GradTrak gt4 = new GradTrakBuilder()
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CP3200").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2101").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS3103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("IS2103").build())
                .withPerson(new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build()).build();

        RecModule cp3880 = rmb.create("CP3880");
        RecModule is4010 = rmb.create("IS4010");
        RecModule cp3200 = rmb.create("CP3200");
        RecModule cp3202 = rmb.create("CP3202");

        RecModulePredicate rmp = new RecModulePredicate(algoCourse, gt1);
        assertFalse(rmp.test(is4010));
        assertFalse(rmp.test(cp3200));
        assertFalse(rmp.test(cp3202));

        rmp = new RecModulePredicate(algoCourse, gt2);
        assertFalse(rmp.test(cp3880));
        assertFalse(rmp.test(cp3200));
        assertFalse(rmp.test(cp3202));

        rmp = new RecModulePredicate(algoCourse, gt3);
        assertFalse(rmp.test(cp3880));
        assertFalse(rmp.test(is4010));

        rmp = new RecModulePredicate(algoCourse, gt4);
        assertTrue(rmp.test(cp3880));
        assertTrue(rmp.test(is4010));
        assertTrue(rmp.test(cp3202));
    }
}
