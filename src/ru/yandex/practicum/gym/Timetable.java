package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.getOrDefault(day, new TreeMap<>());
        timetable.put(day, daySessions);

        List<TrainingSession> sessions = daySessions.getOrDefault(time, new ArrayList<>());
        daySessions.put(time, sessions);

        if (sessions.contains(trainingSession)) {
            return;
        }

        sessions.add(trainingSession);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.getOrDefault(dayOfWeek, new TreeMap<>());

        TreeMap<TimeOfDay, List<TrainingSession>> result = new TreeMap<>();

        for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySessions.entrySet()) {
            List<TrainingSession> sessionsCopy = new ArrayList<>(entry.getValue());
            result.put(entry.getKey(), sessionsCopy);
        }

        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return new ArrayList<>();
        }

        return daySessions.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, CounterOfTrainings> counterMap = new HashMap<>();

        for (Map.Entry<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            Map<TimeOfDay, List<TrainingSession>> daySessions = entry.getValue();
            for (Map.Entry<TimeOfDay, List<TrainingSession>> daySessionsEntry : daySessions.entrySet()) {
                for (TrainingSession trainingSession : daySessionsEntry.getValue()) {
                    Coach coach = trainingSession.getCoach();
                    CounterOfTrainings counter = counterMap.getOrDefault(coach, new CounterOfTrainings(coach));
                    counterMap.put(coach, counter);
                    counter.incrementNumberOfTrainingSessions();
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>(counterMap.values());
        Collections.sort(result);

        return result;
    }
}
