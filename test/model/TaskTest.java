package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    Task task1;
    Task task2;

    @BeforeEach
    void creatTasks() {
        task1 = new Task("title", "description", LocalDateTime.now(), Duration.ofMinutes(15));
        task2 = new Task("title", "description", LocalDateTime.now(), Duration.ofMinutes(15));
    }

    @Test
    void shouldBeEqualTaskId() {
        task1.setId(111);
        task2.setId(111);
        assertEquals(task1, task2, "Задачи не равны");
    }

    @Test
    void shouldNotBeEqualTasksWithDifferentIds() {
        task1.setId(111);
        task2.setId(110);
        assertNotEquals(task1, task2, "Задачи равны");
    }

    @Test
    void checkingTheTimeCalculations() {
        LocalDateTime startTimeExpected = LocalDateTime.now();
        LocalDateTime startTimeActual = task1.getStartTime();
        LocalDateTime endTimeExpected = LocalDateTime.now().plusMinutes(15);
        LocalDateTime endTimeActual = task1.getEndTime();

        assertEquals(startTimeExpected, startTimeActual, "Время начала задачи неверное");
        assertEquals(endTimeExpected, endTimeActual, "Время окончания задачи неверное");
    }
}