package com.example.alimjan.news.api;

import android.support.annotation.Nullable;

/**
 * A simple class that defines common response status of web request.
 */
public class NetworkState {


    /**
     * Enum that includes all possible response status of a web request.
     */
    public enum State {
        LOADING,
        SUCCESS,
        EMPTY,
        END,
        ERROR
    }

    /**
     * Predefined loading state instance for easy to use.
     */
    public static final NetworkState LOADING = new NetworkState(State.LOADING);
    /**
     * Predefined success state instance for easy to use.
     */
    public static final NetworkState SUCCESS = new NetworkState(State.SUCCESS);
    /**
     * Predefined empty state instance for easy to use.
     */
    @SuppressWarnings("unused")
    public static final NetworkState EMPTY = new NetworkState(State.END);
    /**
     * Predefined end state instance for easy to use.
     */
    @SuppressWarnings("unused")
    public static final NetworkState END = new NetworkState(State.END);

    // Current status
    private final State mState;
    // Error message
    private String mMessage;

    public NetworkState(State state) {
        this.mState = state;
    }

    public NetworkState(State state, String message) {
        this.mState = state;
        this.mMessage = message;
    }

    /**
     * Creates a {@link NetworkState} error state instance with message for easy to use.
     *
     * @param message State message.
     * @return {@link NetworkState} with status type error.
     */
    public static NetworkState error(String message) {
        return new NetworkState(State.ERROR, message);
    }

    /**
     * Returns current status.
     */
    public State getStatus() {
        return mState;
    }

    /**
     * Returns current state message, this is a nullable field not every state has message.
     */
    @Nullable
    public String getMessage() {
        return mMessage;
    }


    /**
     *
     */
    public void setMessage(String errorMsg) {
        this.mMessage = errorMsg;
    }
}
