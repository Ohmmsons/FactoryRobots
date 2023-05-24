package simulator;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The RequestQueue class manages a queue of delivery requests.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 * @inv requests != null
 */
public class RequestQueue{
    private final Queue<Request> requests;

    /**
     * Constructor for the RequestQueue class.
     * Initializes a new LinkedList to store the requests.
     *
     * @pre None.
     * @post A new RequestQueue instance is created with an empty requests queue.
     */
    public RequestQueue() {
        requests = new LinkedList<>();
    }

    /**
     * Adds a new delivery request to the end of the queue.
     *
     * @param request The delivery request to be added.
     *
     * @pre request != null
     * @post The request is added to the end of the queue.
     */
    public synchronized void addRequest(Request request) {
        if (request == null) throw new IllegalArgumentException("Request cannot be null");
        requests.add(request);
    }

    /**
     * Retrieves the oldest delivery request from the queue without removing it.
     *
     * @return The oldest delivery request, or null if the queue is empty.
     *
     * @pre None.
     * @post The oldest delivery request in the queue is returned, or null if the queue is empty.
     */
    public synchronized Request getNextRequest() {
        return requests.peek();
    }

    /**
     * Removes the oldest delivery request from the queue.
     *
     * @pre None.
     * @post The oldest delivery request is removed from the queue.
     */
    public synchronized void removeRequest() {
        requests.poll();
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     *
     * @pre None.
     * @post The method returns true if the queue is empty, and false otherwise.
     */
    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    /**
     * Returns the number of delivery requests in the queue.
     *
     * @return The number of delivery requests in the queue.
     *
     * @pre None.
     * @post The number of delivery requests in the queue is returned.
     */
    public synchronized int size() {
        return requests.size();
    }

    /**
     * Returns the queue of delivery requests.
     *
     * @return The queue of delivery requests.
     *
     * @pre None.
     * @post The queue of delivery requests is returned.
     */
    public synchronized Queue<Request> getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("(");
        for(Request request: this.requests)
            str.append(request);
        str.append(")");
        return str.toString();
    }
}
