package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    Task task1;
    Task task2;

    @BeforeEach
    void creatTasks() {
        task1 = new Task("title", "description");
        task2 = new Task("title", "description");
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
}