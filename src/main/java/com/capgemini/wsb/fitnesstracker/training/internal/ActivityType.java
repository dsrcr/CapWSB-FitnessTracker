package com.capgemini.wsb.fitnesstracker.training.internal;

import lombok.Getter;

/**
 * Enum representing the different types of activities that a user can perform during a training session.
 * Each activity type has an associated display name, which can be used for presentation or user interface purposes.
 * <p>
 * The available activity types are:
 * <ul>
 *     <li>{@link #RUNNING} - Represents a running activity</li>
 *     <li>{@link #CYCLING} - Represents a cycling activity</li>
 *     <li>{@link #WALKING} - Represents a walking activity</li>
 *     <li>{@link #SWIMMING} - Represents a swimming activity</li>
 *     <li>{@link #TENNIS} - Represents a tennis activity</li>
 * </ul>
 * </p>
 */
@Getter
public enum ActivityType {
    RUNNING("Running"),
    CYCLING("Cycling"),
    WALKING("Walking"),
    SWIMMING("Swimming"),
    TENNIS("Tennis");


    private final String displayName;

    /**
     * Constructor for {@link ActivityType} enum.
     * Initializes the display name for the activity type.
     *
     * @param displayName the display name of the activity type
     */
    ActivityType(String displayName) {
        this.displayName = displayName;
    }

}
