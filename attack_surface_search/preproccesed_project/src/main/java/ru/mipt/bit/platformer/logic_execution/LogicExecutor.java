package ru.mipt.bit.platformer.logic_execution;

import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.actions.Action;
import ru.mipt.bit.platformer.game_management.ExecutingActionsQueue;
import ru.mipt.bit.platformer.level.Level;


@Component
public class LogicExecutor {
    private final Level level;

    public LogicExecutor(Level level) {
        this.level = level;
    }

    public void executeActions(ExecutingActionsQueue executingActionsQueue) {
        for (Action action : executingActionsQueue.getActions()) {
            action.execute(level);
        }
        executeActions(executingActionsQueue);
    }

}
