package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void creatTasks() {
        epic1 = new Epic("title", "description");
        epic2 = new Epic("title", "description");
    }

    @Test
    void shouldBeEqualEpicId() {
        epic1.setId(111);
        epic2.setId(111);
        assertEquals(epic1, epic2, "Задачи не равны");
    }

    @Test
    void shouldNotBeEqualEpicsWithDifferentIds() {
        epic1.setId(111);
        epic2.setId(110);
        assertNotEquals(epic1, epic2, "Задачи равны");
    }

    @Test
    void shouldNotBeAddedEpicToTheEpic() {
        assertFalse(epic1.addSubtask(epic1), "Эпик не должен добавлять себя в подзадачи");
    }
}