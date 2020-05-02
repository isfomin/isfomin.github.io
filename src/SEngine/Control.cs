using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    static class Control
    {
        private static Random r = new Random();

        public static void DefaultControl(MovableFigure gameObject, GameTime gameTime)
        {
            gameObject.TimeMove += gameTime.LoopTime;

            if (gameObject.TimeMove > 90) {
                if (KeyBoard.getState().IsKeyDown(Keys.LEFT)) {
                    gameObject.Step(Direction.Left);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.RIGHT)) {
                    gameObject.Step(Direction.Right);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.UP)) {
                    gameObject.Step(Direction.Top);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.DOWN)) {
                    gameObject.Step(Direction.Bottom);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                }
            }
        }

        public static void DirectionControl(MovableFigure gameObject, GameTime gameTime)
        {
            gameObject.TimeMove += gameTime.LoopTime;

            if (gameObject.TimeMove > 90) {
                if (KeyBoard.getState().IsKeyDown(Keys.LEFT)) {
                    gameObject.Move(Direction.Left);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.RIGHT)) {
                    gameObject.Move(Direction.Right);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.UP)) {
                    gameObject.Move(Direction.Top);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                } else if (KeyBoard.getState().IsKeyDown(Keys.DOWN)) {
                    gameObject.Move(Direction.Bottom);
                    Console.Beep(32000, 1);
                    gameObject.TimeMove = 0;
                }
            }
        }

        public static void BotControl(MovableFigure gameObject, GameTime gameTime)
        {
            gameObject.TimeMove += gameTime.LoopTime;
            gameObject.TimeStep += gameTime.LoopTime;

            if (gameObject.TimeMove > 1500) {

                gameObject.NewDirection = (Direction)r.Next(0, 4);
                gameObject.TimeMove = 0;
            }

            if (gameObject.TimeStep > 500) {
                if (gameObject.X <= 0 && gameObject.CurrentDirection == Direction.Left ||
                    gameObject.X + gameObject.Width >= 100 && gameObject.CurrentDirection == Direction.Right ||
                    gameObject.Y <= 0 && gameObject.CurrentDirection == Direction.Top ||
                    gameObject.Y + gameObject.Height >= 25 && gameObject.CurrentDirection == Direction.Bottom) {

                    gameObject.NewDirection = (Direction)r.Next(0, 4);
                    while (gameObject.NewDirection == gameObject.CurrentDirection) {
                        gameObject.NewDirection = (Direction)r.Next(0, 4);
                    }
                }

                gameObject.Move(gameObject.NewDirection);
                gameObject.TimeStep = 0;
            }
        }
    }
}
