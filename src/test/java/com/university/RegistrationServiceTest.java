
package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

        Student student = new Student("S101", "Anita");

        // Simulate 20 existing credits.
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(20, student.getTotalCredits());
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCourseIsFull() {

        Student student = new Student("S102", "Rahul");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        // Simulate a course that has reached its capacity.
        for (int i = 0; i < 30; i++) {
            course.incrementEnrollment();
        }

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
        assertEquals(0, student.getTotalCredits());
        assertEquals(30, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWhenOneSeatRemains() {

        Student student = new Student("S103", "Priya");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        // Simulate 29 existing enrollments.
        for (int i = 0; i < 29; i++) {
            course.incrementEnrollment();
        }

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(30, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectDuplicateRegistrationIgnoringCase() {

        Student student = new Student("S104", "Kiran");

        Course originalCourse = new Course(
                "CS101",
                "Programming",
                4,
                30,
                true,
                null
        );

        Course lowercaseCourse = new Course(
                "cs101",
                "Programming",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, originalCourse));

        boolean secondAttempt =
                service.register(student, lowercaseCourse);

        assertFalse(secondAttempt);
        assertTrue(student.isRegistered("CS101"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, originalCourse.getCurrentEnrollment());
        assertEquals(0, lowercaseCourse.getCurrentEnrollment());
    }

    @Test
    void shouldRejectDuplicateRegistrationWithMixedCase() {

        Student student = new Student("S105", "Meera");

        Course originalCourse = new Course(
                "CS101",
                "Programming",
                4,
                30,
                true,
                null
        );

        Course mixedCaseCourse = new Course(
                "Cs101",
                "Programming",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, originalCourse));

        boolean secondAttempt =
                service.register(student, mixedCaseCourse);

        assertFalse(secondAttempt);
        assertTrue(student.isRegistered("CS101"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, originalCourse.getCurrentEnrollment());
        assertEquals(0, mixedCaseCourse.getCurrentEnrollment());
    }
}
