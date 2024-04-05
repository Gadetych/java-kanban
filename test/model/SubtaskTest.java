package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubtaskTest {
    Subtask subtask1;
    Subtask subtask2;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void creatTasks() {
        epic1 = new Epic("title", "description");
        epic2 = new Epic("title", "description");
        subtask1 = new Subtask("title", "description", epic1.getId(), LocalDateTime.now(), Duration.ofMinutes(20));
        subtask2 = new Subtask("title", "description", epic2.getId(), LocalDateTime.now(), Duration.ofMinutes(20));
    }

    @Test
    void shouldBeEqualSubtaskId() {
        subtask1.setId(111);
        subtask2.setId(111);
        assertEquals(subtask1, subtask2, "Задачи не равны");
    }

    @Test
    void shouldNotBeEqualSubtasksWithDifferentIds() {
        subtask1.setId(111);
        subtask2.setId(110);
        assertNotEquals(subtask1, subtask2, "Задачи равны");
    }
}