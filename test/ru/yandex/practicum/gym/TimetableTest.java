package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testAddNewTrainingSessionShouldAddSession() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(18, 30));

        timetable.addNewTrainingSession(session);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SUNDAY, new TimeOfDay(18, 30));
        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertEquals(session, sessions.get(0));
    }

    @Test
    void testAddNewTrainingSessionShouldNotAddDuplicate() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(18, 30));

        timetable.addNewTrainingSession(session);
        timetable.addNewTrainingSession(session);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.SUNDAY, new TimeOfDay(18, 30));
        assertEquals(1, sessions.size());
    }

    @Test
    void testAddNewTrainingSessionMultipleSessionsSameTimeDifferentGroupsWithDifferentCoaches() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Иван", "Дмитриевич");

        Group group1 = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group group2 = new Group("Шахматы", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0));
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySessions);
        assertEquals(1, mondaySessions.size());

        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertNull(tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySessions);
        assertEquals(1, mondaySessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertNotNull(thursdaySessions);
        assertEquals(2, thursdaySessions.size());

        List<TimeOfDay> thursdayTimes = new ArrayList<>(thursdaySessions.keySet());
        assertEquals(new TimeOfDay(13, 0), thursdayTimes.get(0));
        assertEquals(new TimeOfDay(20, 0), thursdayTimes.get(1));

        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertNull(tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondayTrainingSessionsOne = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        assertNotNull(mondayTrainingSessionsOne);
        assertEquals(1, mondayTrainingSessionsOne.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondayTrainingSessionsTwo = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        assertNull(mondayTrainingSessionsTwo);
    }

    @Test
    void testGetCountByCoachesWithMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Иван", "Дмитриевич");
        Coach coach3 = new Coach("Китаева", "Надежда", "Валентиновна");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        Group groupAdult3 = new Group("Шахматы", Age.ADULT, 90);
        TrainingSession tuesdayAdultTrainingSession = new TrainingSession(groupAdult3, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        Group groupAdult2 = new Group("Шахматы", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult2, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        Group groupAdult4 = new Group("Плавание", Age.ADULT, 90);
        TrainingSession wednesdayAdultTrainingSession = new TrainingSession(groupAdult4, coach3,
                DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(wednesdayAdultTrainingSession);
        timetable.addNewTrainingSession(tuesdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        /*Проверить сколько занятий в неделю ведёт каждый из тренеров и первыми возвращаются тренеры,
        которые проводят больше всего занятий*/
        List<CounterOfTrainings> counterOfTrainingsList = timetable.getCountByCoaches();
        assertNotNull(counterOfTrainingsList);
        assertEquals(4, counterOfTrainingsList.get(0).getNumberOfTrainingSessions());
        assertEquals(2, counterOfTrainingsList.get(1).getNumberOfTrainingSessions());
        assertEquals(1, counterOfTrainingsList.get(2).getNumberOfTrainingSessions());
    }

    @Test
    void testGetCountByCoachesOrderingWithEqualCounts() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Иван", "Дмитриевич");
        Coach coach3 = new Coach("Китаева", "Надежда", "Валентиновна");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        Group groupAdult3 = new Group("Шахматы", Age.ADULT, 90);
        TrainingSession tuesdayAdultTrainingSession = new TrainingSession(groupAdult3, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        Group groupAdult2 = new Group("Шахматы", Age.ADULT, 90);
        TrainingSession mondayAdultTrainingSession = new TrainingSession(groupAdult2, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        Group groupAdult4 = new Group("Плавание", Age.ADULT, 90);
        TrainingSession wednesdayAdultTrainingSession = new TrainingSession(groupAdult4, coach3,
                DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0));

        TrainingSession saturdayAdultTrainingSession = new TrainingSession(groupAdult4, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(12, 0));

        timetable.addNewTrainingSession(wednesdayAdultTrainingSession);
        timetable.addNewTrainingSession(tuesdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayAdultTrainingSession);
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(saturdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<CounterOfTrainings> counterOfTrainingsList = timetable.getCountByCoaches();
        assertNotNull(counterOfTrainingsList);
        assertEquals(4, counterOfTrainingsList.get(0).getNumberOfTrainingSessions());
        assertEquals(2, counterOfTrainingsList.get(1).getNumberOfTrainingSessions());
        assertEquals(2, counterOfTrainingsList.get(2).getNumberOfTrainingSessions());
    }

    @Test
    void testGetCountByCoachesWithEmptyTimetable() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
