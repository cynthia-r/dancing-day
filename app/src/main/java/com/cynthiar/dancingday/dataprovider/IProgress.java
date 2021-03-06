package com.cynthiar.dancingday.dataprovider;

/**
 * Created by CynthiaR on 3/4/2017.
 */
public interface IProgress {
    int ERROR = -1;
    int CONNECT_SUCCESS = 0;
    int GET_INPUT_STREAM_SUCCESS = 1;
    int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
    int PROCESS_INPUT_STREAM_SUCCESS = 3;
    int NO_NETWORK_CONNECTION = 4;
    int getProgressCode();
}
