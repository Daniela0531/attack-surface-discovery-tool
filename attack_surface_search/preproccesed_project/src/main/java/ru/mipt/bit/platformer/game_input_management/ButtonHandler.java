package ru . mipt . bit . platformer . game_input_management;
import com . badlogic . gdx . Gdx;
import com . badlogic . gdx . math . GridPoint2;
import org . springframework . stereotype . Component;
import ru . mipt . bit . platformer . actions . impl_action . MoveAction;
import ru . mipt . bit . platformer . actions . impl_action . ShootAction;
import ru . mipt . bit . platformer . actions . impl_action . SwitchHealthBarAction;
import ru . mipt . bit . platformer . button_commands . ButtonCommand;
import ru . mipt . bit . platformer . level . Level;
import ru . mipt . bit . platformer . logic_objects . bullet . BulletMoveModel;
import ru . mipt . bit . platformer . logic_objects . properties . Direction;
import static com . badlogic . gdx . Input . Keys . *;
@ Component public class ButtonHandler
{
private final Level level;
private final CommandQueue commandQueueHandler;
public ButtonHandler ( Level level , CommandQueue commandQueueHandler )
{
this . level = level;
this . commandQueueHandler = commandQueueHandler;
}
public void readCommand ( )
{
if ( level . isPlayerKilled ( ) )
{
return;
}
if ( Gdx . input . isKeyPressed ( UP ) || Gdx . input . isKeyPressed ( W ) )
{
Direction direction = new Direction ( ButtonCommand . UP );
MoveAction moveAction = new MoveAction ( level . getPlayerTank ( ) , direction );
commandQueueHandler . addAction ( moveAction );
}
if ( Gdx . input . isKeyPressed ( LEFT ) || Gdx . input . isKeyPressed ( A ) )
{
Direction direction = new Direction ( ButtonCommand . LEFT );
MoveAction moveAction = new MoveAction ( level . getPlayerTank ( ) , direction );
commandQueueHandler . addAction ( moveAction );
}
if ( Gdx . input . isKeyPressed ( DOWN ) || Gdx . input . isKeyPressed ( S ) )
{
Direction direction = new Direction ( ButtonCommand . DOWN );
MoveAction moveAction = new MoveAction ( level . getPlayerTank ( ) , direction );
commandQueueHandler . addAction ( moveAction );
}
if ( Gdx . input . isKeyPressed ( RIGHT ) || Gdx . input . isKeyPressed ( D ) )
{
Direction direction = new Direction ( ButtonCommand . RIGHT );
MoveAction moveAction = new MoveAction ( level . getPlayerTank ( ) , direction );
commandQueueHandler . addAction ( moveAction );
}
if ( Gdx . input . isKeyPressed ( SPACE ) )
{
GridPoint2 coord = level . getPlayerTank ( ) . getCoordinates ( ) . cpy ( );
Direction direction = new Direction ( level . getPlayerTank ( ) . getRotation ( ) );
GridPoint2 startBulletCoord = new GridPoint2 ( coord . x + direction . getVector ( ) . x , coord . y + direction . getVector ( ) . y );
BulletMoveModel bulletMoveModel = level . putBulletInLevel ( startBulletCoord , direction );
ShootAction shootAction = new ShootAction ( level . getPlayerTank ( ) , bulletMoveModel , direction );
commandQueueHandler . addAction ( shootAction );
}
if ( Gdx . input . isKeyPressed ( L ) )
{
System . out . println ( "L pressed" );
SwitchHealthBarAction switchHealthBarAction = new SwitchHealthBarAction ( level . getPlayerTank ( ) );
commandQueueHandler . addAction ( switchHealthBarAction );
}

}

}
