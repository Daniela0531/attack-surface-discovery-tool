package ru.mipt.bit.platformer.actions;

import ru.mipt.bit.platformer.level.Level;

public interface Action {
    boolean isFinished();
    void execute(Level level);
}
