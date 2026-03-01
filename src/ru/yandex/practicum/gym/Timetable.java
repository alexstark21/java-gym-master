package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(day);
        if (daySessions == null) {
            daySessions = new TreeMap<>();
            timetable.put(day, daySessions);
        }

        List<TrainingSession> sessions = daySessions.get(time);
        if (sessions == null) {
            sessions = new ArrayList<>();
            daySessions.put(time, sessions);
        }

        if (sessions.contains(trainingSession)) {
            return;
        }

        sessions.add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> counterMap = new HashMap<>();

        for (Map.Entry<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            Map<TimeOfDay, List<TrainingSession>> daySessions = entry.getValue();
            for (Map.Entry<TimeOfDay, List<TrainingSession>> daySessionsEntry : daySessions.entrySet()) {
                for (TrainingSession trainingSession : daySessionsEntry.getValue()) {
                    Coach coach = trainingSession.getCoach();
                    CounterOfTrainings counter = counterMap.get(coach);
                    if (counter == null) {
                        counter = new CounterOfTrainings(coach);
                        counterMap.put(coach, counter);
                    }
                    counter.incrementNumberOfTrainingSessions();
                }
            }
        }

        return new ArrayList<>(counterMap.values());
    }
}
