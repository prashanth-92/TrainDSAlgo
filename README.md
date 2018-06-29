# TrainDSAlgo
Game.scala
package bootstrap

import core.ComponentEngine
import core.PlayerEngine
import model.DifficultyLevel
import model.LadderPosition
import model.Player
import model.SnakePosition
import util.Helper
import core.PromptEngine

object Game {
	def main(args: Array[String]): Unit = {
	  println("start")
	  val (snakePos: List[SnakePosition], ladderPos: List[LadderPosition]) = ComponentEngine.generate(DifficultyLevel.EASY)
	  val players: List[Player] = PlayerEngine.createPlayers()
	  println("List of Players")
	  players.foreach(println)
	  var continueGame: Boolean = true
	  var activePlayer: Int = PlayerEngine.getFirstPlayer(players)
	  while(continueGame){
	    val player: Player = players(activePlayer)
	    println("Current Player: "+player.name+". Press 1 to roll the dice")
	    val input = PromptEngine.prompt() 
	    if(input.equalsIgnoreCase("1")){
	      play(player, snakePos, ladderPos)
	      activePlayer = PlayerEngine.getNextPlayer(players, activePlayer)
	    }
	    else if(input.equalsIgnoreCase("quit") && PromptEngine.quitPrompt().equalsIgnoreCase("Y")){
  	    continueGame = false
	    }
	  }
	  println("------snakes")
	  println(snakePos)
	  println("------ladders")
	  println(ladderPos)
	  println("------players")
	  print(players)
	}
	def play(activePlayer: Player, snakePos: List[SnakePosition], ladderPos: List[LadderPosition]): Unit = {
	  val pos: Int = Helper.rollDice()
	  println("Dice:"+pos)
	  activePlayer.move(pos,snakePos,ladderPos)
	  println(activePlayer.position)
	}
}

ComponentEngine.scala
package core

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Set

import model.DifficultyLevel
import model.DifficultyLevel.DifficultyLevel
import model.LadderPosition
import model.SnakePosition
import util.LadderHelper
import util.SnakeHelper
import model.Point

object ComponentEngine {
  def generate(level: DifficultyLevel): (List[SnakePosition], List[LadderPosition]) = {
	  (generateSnakes(level), generateLadders(level))
	}
	def generateSnakes(level: DifficultyLevel): List[SnakePosition] ={
	  val snakesToGenerate = SnakeHelper.getNumberofSnakes(level)
	  var snakes= ListBuffer[SnakePosition]()
	  for(i <- 0 until snakesToGenerate){
	    snakes+= new SnakePosition(PointGenerator.getPoint(), PointGenerator.getPoint())
	  }
	  snakes.toList
	}
	def generateLadders(level:DifficultyLevel): List[LadderPosition] = {
	  val laddersToGenerate = LadderHelper.getNumberofLadders(level)
	  var ladders= ListBuffer[LadderPosition]()
	  for(i <- 0 until laddersToGenerate){
	    ladders+= new LadderPosition(PointGenerator.getPoint(), PointGenerator.getPoint())
	  }
	  ladders.toList
	}
}

PlayerEngine.scala
package core

import model.Player
import scala.collection.mutable.ListBuffer
import model.Point

object PlayerEngine {
  def createPlayers(): List[Player] = {
    var players = ListBuffer[Player]()
    players += createPlayer(new Point(0,0), "Test1")
    players += createPlayer(new Point(0,0), "Test2")
    players.toList
  }
  def createPlayer(position: Point, name: String): Player = {
    var player = new Player(position, name)
    player
  }
  def getFirstPlayer(players:List[Player]): Int = {
    0
  }
  def getNextPlayer(players: List[Player], currentPlayerIndex: Int):Int = {
    if((currentPlayerIndex+1)>(players.length-1))
      return 0
    currentPlayerIndex+1
  }
}
PointGenerator.scala
package core

import util.Helper
import model.Point
import util.Constants

object PointGenerator {
  def getPoint(): Point = {
    val x = Helper.getRandomNumber(Constants.COLUMNS)
    val y = Helper.getRandomNumber(Constants.ROWS)
    new Point(x,y)
  }
}

PromptEngine.scala
package core

import scala.io.StdIn

object PromptEngine {
  def quitPrompt():String = {
	  println("You have decided to quit the game, no progress will be saved. Do you want to continue?")
	  prompt()
	}
	def prompt(): String = {
	  readLine()
	}
}

Component.scala
package model

object Component extends Enumeration{
  type Component = Value
  val SNAKE, LADDER = Value
}

DifficultyLevel.scala
package model

object DifficultyLevel extends Enumeration{
  type DifficultyLevel = Value
  val EASY, MEDIUM, HARD = Value
}

LadderPosition.scala
package model

class LadderPosition(val start:Point, val end: Point) {
   override def toString(): String = {
    "start:" + start + ".end:"+end
  }
}

Player.scala
package model

import util.Constants

class Player(var position: Point, val name: String) {
	override def toString(): String = {
			"position:"  + position.toString() + "name:" + name
	}
	def move(places: Int, snakePos: List[SnakePosition], ladderPos: List[LadderPosition]): Unit = {
			if((position.x + places) <= Constants.COLUMNS){
				position.x += places
			}
			else{
				position.y += (position.x + places) / Constants.COLUMNS
						position.x = (position.x + places) - Constants.COLUMNS 
			}
			handleSnake(snakePos)
			handleLadder(ladderPos)
	}
	private def handleSnake(snakePos: List[SnakePosition]): Unit = {
			val filteredSnakes = snakePos.filter(equals)
					if(!filteredSnakes.isEmpty){
						position.move(filteredSnakes(0).end)
					}
			/*snakePos.foreach(spos=> {
      if(equals(spos.start, position)){
        println("-----snake")
        println(spos)
        position.move(spos.end)
      }
    })*/
	}
	private def handleLadder(ladderPos: List[LadderPosition]): Unit = {
			val filteredLadders = ladderPos.filter(equals)
					if(!filteredLadders.isEmpty){
						position.move(filteredLadders(0).end)
					}

			/*ladderPos.foreach(lpos => {
      if(equals(lpos.start, position)){
        println("-----ladder")
        println(lpos)
        position.move(lpos.end)
      }
    })*/
	}
	private def equals(p1: Point, p2: Point): Boolean = {
			(p1.x == p2.x && p1.y == p2.y)
	}
}

Point.scala
package model

import util.Constants
import util.Helper

class Point(var x: Int, var y: Int) {
  override def toString(): String = {
    "x:"  + x.toString() + "y:" + y.toString()
  }
 /* override def equals(another: Point): Boolean = {
    this.x == another.x && this.y == another.y
  }*/
  def move(position: Point): Unit = {
    println("moved")
    x = position.x
    y = position.y
  }
}

SnakePosition.scala
package model

class SnakePosition(val start:Point, val end: Point) {
  override def toString(): String = {
    "start:" + start + ".end:"+end
  }
 /* override def equals(another:SnakePosition): Boolean = {
    (another.start.equals(this.start) && another.end.equals(this.end))
  }*/
}

Constants.scala
package util

import model.Component
import model.DifficultyLevel

object Constants {
  val level = Map(DifficultyLevel.HARD -> 
                    Map(Component.SNAKE->18, Component.LADDER ->5),
                  DifficultyLevel.MEDIUM -> 
                    Map(Component.SNAKE->12, Component.LADDER ->11),
                  DifficultyLevel.EASY -> 
                    Map(Component.SNAKE->110, Component.LADDER ->13)
                  )
 val ROWS = 10
 val COLUMNS = 10
 
}

Helper.scala
package util

import scala.util.Random

object Helper {
  val r = Random
  def getRandomNumber(max: Int): Int = {
			r.nextInt(max)
	}
  def rollDice(): Int = {
	 getRandomNumber(6) + 1
	}
}

LadderHelper.scala
package util

import model.DifficultyLevel.DifficultyLevel
import model.Component

object LadderHelper {
  
	def getNumberofLadders(level: DifficultyLevel): Int = {
		Helper.getRandomNumber(Constants.level.get(level).get(Component.LADDER))
	}
}

SnakeHelper.scala
package util

import model.DifficultyLevel.DifficultyLevel
import model.Component

object SnakeHelper {
  def getNumberofSnakes(level: DifficultyLevel): Int = {
	  Helper.getRandomNumber(Constants.level.get(level).get(Component.SNAKE))
	}
}
