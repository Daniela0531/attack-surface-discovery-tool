package ru.mipt.bit.platformer.game_input_management;

import com.badlogic.gdx.math.GridPoint2;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.actions.Action;
import ru.mipt.bit.platformer.button_commands.ButtonCommand;

import java.util.ArrayList;

@Component
public class CommandQueue {
//    private ArrayList<Command> receivedCommands;
    private ArrayList<Action> actions;

    public CommandQueue() {
//        this.receivedCommands = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public int moveActionAmount() {
        return actions.size();
    }
    public void addAction(Action moveAction) {
//        receivedCommands.add(command);
        actions.add(moveAction);
    }

//    public void add(Command command, Level level) {
////        receivedCommands.add(command);
//        if (command == Command.UP) {
//            Direction direction = new Direction(command);
//            MoveAction moveAction = new MoveAction(level.getPlayerTank(), direction);
//            actions.add(moveAction);
//        }
//        if (command == Command.LEFT) {
//            Direction direction = new Direction(command);
//            MoveAction moveAction = new MoveAction(level.getPlayerTank(), direction);
//            actions.add(moveAction);
//        }
//        if (command == Command.DOWN) {
//            Direction direction = new Direction(command);
//            MoveAction moveAction = new MoveAction(level.getPlayerTank(), direction);
//            actions.add(moveAction);
//        }
//        if (command == Command.RIGHT) {
//            Direction direction = new Direction(command);
//            MoveAction moveAction = new MoveAction(level.getPlayerTank(), direction);
//            actions.add(moveAction);
//        }
//        if (command == Command.SHOOT) {
//            Direction direction = new Direction(command);
//            MoveAction moveAction = new MoveAction(level.getPlayerTank(), direction);
//            actions.add(moveAction);
//        }
//    }

    public GridPoint2 process(ButtonCommand command) {
        if (command == ButtonCommand.UP) {
            return new GridPoint2(0, 1);
        }
        if (command == ButtonCommand.LEFT) {
            return new GridPoint2(-1, 0);
        }
        if (command == ButtonCommand.DOWN) {
            return new GridPoint2(0, -1);
        }
        if (command == ButtonCommand.RIGHT) {
            return new GridPoint2(1, 0);
        }
        return new GridPoint2(0, 0);
    }

//    public Command get() {
//        Command command = receivedCommands.get(0);
//        return command;
//    }

    public Action get() {
        return actions.get(0);
    }

//    public void popMoveAction() {
//        actions.remove(0);
//    }

    public void pop() {
        if (actions.isEmpty()) {
            return;
        }
        actions.remove(0);
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public int size() {
        return actions.size();
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void remove(Action action) {
        actions.remove(action);
    }

    public void clear() {
        actions.clear();
    }

    public void printQueue() {
        System.out.println("my queue:");
        for(Action action : actions) {
//            System.out.println("    " + action.getActionType() +
//                    ": coord " + action.getModel().getCoordinates() +
//                    "  direction" + action.getModel().getDirection().getVector());
        }
    }
}
