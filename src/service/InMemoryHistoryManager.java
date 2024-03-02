package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history;
    private Node first;
    private Node last;

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    private void linkLast(Task task) {
        if (first == null) {
            first = new Node(null, task, null);
            last = first;
        } else {
            Node newNode = new Node(last, task, null);
            last.next = newNode;
            last = newNode;
        }
    }

    private List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node node = first;
        while (node != null) {
            list.add(node.task);
            node = node.next;
        }
        return list;
    }

    private void removeNode(Node node) {
        if (node != null) {
            Node perv = node.prev;
            Node next = node.next;
            if (perv != null) {
                perv.next = next;
            } else {
                first = next;
            }

            if (next != null) {
                next.prev = perv;
            } else {
                last = perv;
            }
        }
    }

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        removeNode(node);
        history.remove(id);
    }

}
