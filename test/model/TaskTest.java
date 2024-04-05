package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    Task task1;
    Task task2;

    @BeforeEach
    void creatTasks() {
        task1 = new Task("title", "description", "04.05.2024 14:30", 30);
        task2 = new Task("title", "description", "04.05.2024 14:30", 30);
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
        LocalDateTime startTimeExpected = LocalDateTime.of(2024, Month.MAY, 4, 14, 30);
        LocalDateTime startTimeActual = task1.getStartTime();
        LocalDateTime endTimeExpected = LocalDateTime.of(2024, Month.MAY, 4, 15, 0);
        LocalDateTime endTimeActual = task1.getEndTime();

        assertEquals(startTimeExpected, startTimeActual, "Время начала задачи неверное");
        assertEquals(endTimeExpected, endTimeActual, "Время окончания задачи неверное");
    }
}