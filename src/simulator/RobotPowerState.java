package simulator;

/**
 * This enum represents the power state of a robot. A robot can be in one of the following states:
 * - ENROUTE: The robots is going to the delivery request pickup point.
 * - DELIVERING: The robot is delivering a package.
 * - CHARGING: The robot is currently charging and not able to accept new requests.
 * - STANDBY: The robot is not currently delivering or charging, but is waiting for a new request.
 * - RETURNING: The robot is returning to its charging station to recharge.
 *  @author Jude Adam
 *  @version 1.0.0 20/02/2023
 */
public enum RobotPowerState{ENROUTE,DELIVERING,CHARGING,STANDBY,RETURNING}
