package ru.mipt.bit.platformer.actions.impl_action;

import ru.mipt.bit.platformer.actions.Action;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.logic_objects.LivableModel;

public class SwitchHealthBarAction implements Action {
    private final LivableModel livableModel;
    private boolean isFinished = false;
    public SwitchHealthBarAction(LivableModel livableModel) {
        this.livableModel = livableModel;
        this.isFinished = false;
    }
    @Override
    public boolean isFinished() {
        return isFinished;
    }
    @Override
    public void execute(Level level) {
        livableModel.switchHealthBar();
        finished();
    }
    private void finished() {
        this.isFinished = true;
    }

}
