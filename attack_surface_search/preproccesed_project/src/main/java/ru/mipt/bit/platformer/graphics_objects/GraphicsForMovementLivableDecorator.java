package ru . mipt . bit . platformer . graphics_objects ;


import com . badlogic . gdx . graphics . Texture ;

import com . badlogic . gdx . graphics . g2d . Batch ;

import com . badlogic . gdx . math . Rectangle ;

import ru . mipt . bit . platformer . logic_objects . LivableModel ;

import ru . mipt . bit . platformer . logic_objects . MoveModel ;


public class GraphicsForMovementLivableDecorator implements GraphicsMovementInterface  { 
    private GraphicsMovementInterface graphicsMovementInterface ;

    private HealthBarDrower healthBarDecorator ;

    private LivableModel livableModel ;


    public GraphicsForMovementLivableDecorator ( Texture texture ,  HealthBarDrower healthBarDecorator ,  MoveModel moveModel )   { 
        this . graphicsMovementInterface  =  new GraphicsForMovable ( texture ,  moveModel )  ;

        this . healthBarDecorator  =  healthBarDecorator ;

        this . livableModel  =   ( LivableModel )  moveModel ;

     } 
     @ Override
    public Rectangle getRectangle (  )   { 
        return graphics . getRectangle (  )  ;

     } 

     @ Override
    public void draw ( Batch batch )   { 
        graphics . draw ( batch )  ;

        if  ( livableModel . isHealthBarRaise (  )  )   { 
            drowHealthBar ( batch ,  livableModel )  ;

         } 
     } 

    private void drowHealthBar ( Batch batch ,  LivableModel model )   { 
        healthBarDecorator . drawHealthBar ( batch ,  graphicsMovementInterface . getTextureRegion (  )  ,  model . getMaxHealth (  )  ,  model . getCurrentHealth (  )  )  ;

     } 
     @ Override
    public void dispose (  )   { 
        graphics . dispose (  )  ;

     } 
 } 
