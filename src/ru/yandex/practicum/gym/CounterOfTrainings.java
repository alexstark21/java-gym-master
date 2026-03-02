package ru.yandex.practicum.gym;

import java.util.Objects;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int numberOfTrainingSessions;

    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getNumberOfTrainingSessions() {
        return numberOfTrainingSessions;
    }

    public void incrementNumberOfTrainingSessions() {
        numberOfTrainingSessions++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CounterOfTrainings counter = (CounterOfTrainings) o;
        return numberOfTrainingSessions == counter.numberOfTrainingSessions && Objects.equals(coach, counter.coach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coach, numberOfTrainingSessions);
    }


    @Override
    public int compareTo(CounterOfTrainings o) {
        return Integer.compare(o.numberOfTrainingSessions, this.numberOfTrainingSessions);
    }
}
