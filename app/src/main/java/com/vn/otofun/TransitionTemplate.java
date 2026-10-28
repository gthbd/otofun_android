package com.vn.otofun;

/**
 * Created by AnhVT on 7/9/15.
 */
public interface TransitionTemplate {
    void enterTransition();

    boolean exitTransition(Runnable endAction);
}
