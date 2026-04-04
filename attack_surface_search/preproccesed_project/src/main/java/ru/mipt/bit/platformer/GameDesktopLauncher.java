package ru . mipt . bit . platformer ;


import com . badlogic . gdx . ApplicationListener ;

import com . badlogic . gdx . backends . lwjgl3 . Lwjgl3Application ;

import com . badlogic . gdx . backends . lwjgl3 . Lwjgl3ApplicationConfiguration ;

import org . springframework . context . ApplicationContext ;

import org . springframework . context . annotation . AnnotationConfigApplicationContext ;


public class GameDesktopLauncher implements ApplicationListener  { 
    private Game game ;

     @ Override
    public void create (  )   { 
        ApplicationContext context  =  new AnnotationConfigApplicationContext ( GameConfiguration . class )  ;

        this . game  =   ( Game )  context . getBean ( "game" )  ;

     } 
     @ Override
    public void render (  )   { 
        game . renderCurrentResultByTick (  )  ;

     } 
     @ Override
    public void resize ( int width ,  int height )   { 
     } 
     @ Override
    public void pause (  )   { 
     } 
     @ Override
    public void resume (  )   { 
     } 
     @ Override
    public void dispose (  )   { 
        game . stop (  )  ;

     } 
    public static void main ( String[] args )   { 
        Lwjgl3ApplicationConfiguration config  =  new Lwjgl3ApplicationConfiguration (  )  ;

        config . setWindowedMode ( 1280 ,  1024 )  ;

        new Lwjgl3Application ( new GameDesktopLauncher (  )  ,  config )  ;

     } 
 } 
