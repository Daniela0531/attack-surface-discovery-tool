package ru.mipt.bit.platformer.game_management;

import ru.mipt.bit.platformer.actions.Action;
import ru.mipt.bit.platformer.game_input_management.CommandQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExecutingActionsQueue {
    private Set<Action> executingActions;
    public ExecutingActionsQueue() {
        this.executingActions = new HashSet<>();
    }
    public void catchingNewActions(CommandQueue commandQueue) {
        if (commandQueue.isEmpty()) {
            return;
        }
        for (Action newAction : commandQueue.getActions()) {
            executingActions.add(newAction);
        }
        commandQueue.clear();
    }

    public void removeFinishedActions() {
        Collection<Action> allActions = new ArrayList<>(executingActions);
        for(Action action : allActions) {
            if (action.isFinished()) {
                executingActions.remove(action);
            }
        }
    }

    public Set<Action> getActions() {
        return executingActions;
    }
}
