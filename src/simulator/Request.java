package simulator;

/**
 * The Request class represents a delivery request with a start and end point.
 *
 * @author Jude Adam
 * @version 1.0.0 20/04/2023
 * @param start The start point of the delivery request.
 * @param end   The end point of the delivery request.
 * @inv start != null &amp;&amp; end != null
 */
public record Request(Point start, Point end) {
    /**
     * Constructs a Request instance.
     *
     * @param start The start point of the delivery request.
     * @param end   The end point of the delivery request.
     *
     * @pre start != null &amp;&amp; end != null
     * @post A Request instance is created with the given start and end points.
     */
    public Request {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end points cannot be null");
        }
    }

    @Override
    public String toString(){
        return "Start " + start + " End " + end;
    }
}
