package ru . mipt . bit . platformer . actions . impl_action ;


import com . badlogic . gdx . math . GridPoint2 ;

import ru . mipt . bit . platformer . actions . Action ;

import ru . mipt . bit . platformer . level . Level ;

import ru . mipt . bit . platformer . logic_objects . MoveModel ;

import ru . mipt . bit . platformer . logic_objects . properties . Direction ;

import ru . mipt . bit . platformer . logic_objects . tank . TankMoveModel ;

import ru . mipt . bit . platformer . logic_objects . tree . TreeMoveModel ;


import static com . badlogic . gdx . math . MathUtils . isEqual ;


public class MoveAction implements Action  { 
    private final Direction direction ;

    private final GridPoint2 destinationCoordinates ;

    private final MoveModel moveModel ;

    private boolean isFinished  =  false ;

    private boolean isStarted  =  false ;


    public MoveAction ( MoveModel tankMoveModel ,  Direction direction )   { 
        this . direction  =  direction ;

        this . moveModel  =  tankMoveModel ;

        this . destinationCoordinates  =  new GridPoint2 ( 
                tankMoveModel . getCoordinates (  )  . x  +  direction . getVector (  )  . x , 
                tankMoveModel . getCoordinates (  )  . y  +  direction . getVector (  )  . y
         )  ;

     } 

     @ Override
    public boolean isFinished (  )   { 
        return isFinished ;

     } 

     @ Override
    public void execute ( Level level )   { 
        if  ( moveModel . isMoving (  )   &&   ! isStarted )   { 
            finished (  )  ;

            return ;

         } 
        if  (  ! moveModel . isMoving (  )  )   { 
            moveModel . setDirection ( direction )  ;

            moveModel . setMovingStatus ( true )  ;

            isStarted  =  true ;

         } 

        executeTankMovement ( level )  ;

        moveModel . setRotation ( moveModel . getDirection (  )  . getRotation (  )  )  ;

        if  ( isEqual ( moveModel . getProgress (  )  ,  1f )  )   { 
            finishMoveActionIfPossible (  )  ;

         } 
     } 

    private void finished (  )   { 
        isFinished  =  true ;

     } 

    private GridPoint2 getDestinationCoordinates (  )   { 
        return destinationCoordinates ;

     } 

    private void executeTankMovement ( Level level )   { 
        if  (  ! movementIsPossible ( level )  )   { 
            moveModel . setProgress ( 0f )  ;

            moveModel . setMovingStatus ( false )  ;

            finished (  )  ;

         } 
     } 

    private boolean movementIsPossible ( Level level )   { 
        GridPoint2 newCoordinates  =  getDestinationCoordinates (  )  . cpy (  )  ;

        if  ( newCoordinates . x  <  level . getLeftBound (  )   || 
                newCoordinates . x  >  level . getRightBound (  )   || 
                newCoordinates . y  <  level . getLowBound (  )   || 
                newCoordinates . y  >  level . getUpBound (  )  )   { 
            return false ;

         } 
        for ( TreeMoveModel obstacle  :  level . getTrees (  )  . keySet (  )  )   { 
            if  ( obstacle . getCoordinates (  )  . equals ( newCoordinates )  )   { 
                return false ;

             } 
         } 
        for ( TankMoveModel otherTank  :  level . getTanks (  )  . keySet (  )  )   { 
            if  ( otherTank . getCoordinates (  )   ==  moveModel . getCoordinates (  )  )   { 
                continue ;

             } 
            if  ( otherTank . getCoordinates (  )  . equals ( newCoordinates )   ||  otherTank . getDestination (  )  . equals ( newCoordinates )  )   { 
                return false ;

             } 
         } 
        if  ( level . getPlayerTank (  )    !  =   moveModel )   { 
            if  ( level . getPlayerTank (  )  . getCoordinates (  )  . equals ( newCoordinates )   ||  level . getPlayerTank (  )  . getDestination (  )  . equals ( newCoordinates )  )   { 
                return false ;

             } 
         } 
        return true ;

     } 

    private void finishMoveActionIfPossible (  )   { 
        if  ( isEqual ( moveModel . getProgress (  )  ,  1f )  )   { 
            moveModel . finishMovement (  )  ;

            moveModel . setProgress ( 0f )  ;

            moveModel . setMovingStatus ( false )  ;

            finished (  )  ;

         } 
     } 
 } 
