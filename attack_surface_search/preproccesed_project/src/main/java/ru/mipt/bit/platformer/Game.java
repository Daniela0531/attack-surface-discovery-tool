package ru . mipt . bit . platformer ;


import com . badlogic . gdx . Gdx ;

import org . springframework . stereotype . Component ;

import ru . mipt . bit . platformer . game_input_management . ButtonHandler ;

import ru . mipt . bit . platformer . game_input_management . CommandQueue ;

import ru . mipt . bit . platformer . game_input_management . GeneratorActions ;

import ru . mipt . bit . platformer . game_management . ExecutingActionsQueue ;

import ru . mipt . bit . platformer . graphics_management . MainGraphicRender ;

import ru . mipt . bit . platformer . level . Level ;

import ru . mipt . bit . platformer . level_properties . GraphicProperties ;

import ru . mipt . bit . platformer . logic_execution . LogicExecutor ;


 @ Component
public class Game  { 
    private final ButtonHandler buttonHandler ;

    private final LogicExecutor logicExecuter ;

    private final CommandQueue commandQueueHandler ;

    private ExecutingActionsQueue executingActions ;

    private final GeneratorActions generatorActions ;

    private final Level level ;

    private MainGraphicRender graphicRender ;


    public Game ( ButtonHandler buttonHandler , 
                LogicExecutor logicExecuter , 
                CommandQueue commandQueueHandler , 
                GeneratorActions generatorActions , 
                Level level , 
                GraphicProperties graphicProperties )   { 
        this . buttonHandler  =  buttonHandler ;

        this . commandQueueHandler  =  commandQueueHandler ;

        this . logicExecuter  =  logicExecuter ;

        this . generatorActions  =  generatorActions ;

        this . level  =  level ;

        this . executingActions  =  new ExecutingActionsQueue (  )  ;

        this . graphicRender  =  new MainGraphicRender ( graphicProperties ,  level )  ;

     } 

    public void renderCurrentResultByTick (  )   { 
        float deltaTime  =  Gdx . graphics . getDeltaTime (  )  ;

        commandProcessing (  )  ;

        logicExecuter . executeActions ( executingActions )  ;


        level . update ( deltaTime )  ;

        graphicRender . render ( level )  ;

     } 

    private void commandProcessing (  )   { 
        getNewCommands (  )  ;

        executingActions . removeFinishedActions (  )  ;

        executingActions . catchingNewActions ( commandQueueHandler )  ;

     } 
    private void getNewCommands (  )   { 
        buttonHandler . readCommand (  )  ;

        generatorActions . getCommand (  )  ;

     } 

    public void stop (  )   { 
        graphicRender . dispose (  )  ;

     } 
 } 
