package ru . mipt . bit . platformer . graphics_management;
import org . springframework . stereotype . Component;
import ru . mipt . bit . platformer . level . Level;
import ru . mipt . bit . platformer . util . TileMovement;
@ Component public class MovementGraphicRender
{
private final TileMovement tileMovement;
private final Level level;
public MovementGraphicRender ( TileMovement tileMovement , Level level )
{
this . tileMovement = tileMovement;
this . level = level;
}
public void drow ( )
{
level . movementDrow ( tileMovement );
}

}
