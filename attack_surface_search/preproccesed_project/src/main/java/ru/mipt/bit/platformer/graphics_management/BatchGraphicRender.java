package ru . mipt . bit . platformer . graphics_management ;


import com . badlogic . gdx . graphics . g2d . Batch ;

import org . springframework . stereotype . Component ;

import ru . mipt . bit . platformer . graphics_objects . GraphicsInterface ;

import ru . mipt . bit . platformer . level . Level ;


 @ Component
public class BatchGraphicRender  { 
    private final Batch batch ;

    private final Level level ;


    public BatchGraphicRender ( Batch batch ,  Level level )   { 
        this . batch  =  batch ;

        this . level  =  level ;

     } 

    public void drow (  )   { 
        batch . begin (  )  ;

        for  ( GraphicsInterface graphicsInterface  :  level . allGraphicsEntities (  )  )   { 
            graphicsInterface . draw ( batch )  ;

         } 
        batch . end (  )  ;

     } 

    public void dispose (  )   { 
        level . dispose (  )  ;

        batch . dispose (  )  ;

     } 
 } 
