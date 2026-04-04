package ru . mipt . bit . platformer . graphics_objects ;


import com . badlogic . gdx . graphics . Texture ;

import com . badlogic . gdx . graphics . g2d . Batch ;

import com . badlogic . gdx . graphics . g2d . TextureRegion ;

import com . badlogic . gdx . math . Rectangle ;

import ru . mipt . bit . platformer . logic_objects . MoveModel ;

import ru . mipt . bit . platformer . util . TileMovement ;


import static ru . mipt . bit . platformer . util . GdxGameUtils . createBoundingRectangle ;

import static ru . mipt . bit . platformer . util . GdxGameUtils . drawTextureRegionUnscaled ;


public class GraphicsForMovable implements GraphicsInterface ,  GraphicsMovementInterface  { 
    private MoveModel moveModel ;

    private Texture texture ;

    private TextureRegion textureRegion ;

    private Rectangle rectangle ;


    public GraphicsForMovable ( Texture texture ,  MoveModel moveModel )   { 
        this . texture  =  texture ;

        this . textureRegion  =  new TextureRegion ( texture )  ;

        this . rectangle  =  createBoundingRectangle ( textureRegion )  ;

        this . moveModel  =  moveModel ;

     } 
     @ Override
    public void movementDrow ( TileMovement tileMovement )   { 
        movementRender ( tileMovement ,  moveModel ,  rectangle )  ;

     } 
     @ Override
    public Rectangle getRectangle (  )   { 
        return rectangle ;

     } 

     @ Override
    public void draw ( Batch batch )   { 
        drawTextureRegionUnscaled ( batch ,  textureRegion ,  rectangle ,  moveModel . getRotation (  )  )  ;

     } 
     @ Override
    public void dispose (  )   { 
        texture . dispose (  )  ;

     } 

    private void movementRender ( TileMovement tileMovement ,  MoveModel node ,  Rectangle rectangle )   { 
        tileMovement . moveRectangleBetweenTileCenters ( 
                rectangle , 
                node . getCoordinates (  )  , 
                node . getDestination (  )  , 
                node . getProgress (  ) 
         )  ;

     } 
 } 
