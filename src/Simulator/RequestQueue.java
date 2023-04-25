package Simulator;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
    private Queue<Point> requests;

    /**
     * Constructor for the RequestQueue class.
     * Initializes a new LinkedList to store the requests.
     */
    public RequestQueue() {
        requests = new LinkedList<>();
    }

    /**
     * Adds a new delivery request to the end of the queue.
     *
     * @param request The delivery request to be added.
     */
    public synchronized void addRequest(Point request) {
        requests.add(request);
    }

    /**
     * Retrieves the oldest delivery request from the queue without removing it.
     *
     * @return The oldest delivery request, or null if the queue is empty.
     */
    public synchronized Point getNextRequest() {
        return requests.peek();
    }

    /**
     * Removes the oldest delivery request from the queue.
     */
    public synchronized void removeRequest() {
        requests.poll();
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    /**
     * Returns the number of delivery requests in the queue.
     *
     * @return The number of delivery requests in the queue.
     */
    public synchronized int size() {
        return requests.size();
    }

    /**
     * Returns the queue of delivery requests.
     *
     * @return The queue of delivery requests.
     */
    public synchronized Queue<Point> getRequests() {
        return requests;
    }
}