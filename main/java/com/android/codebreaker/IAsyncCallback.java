package com.android.codebreaker;

public interface IAsyncCallback
{
    void onTaskCompleted(String result);
    void onTaskError();
}