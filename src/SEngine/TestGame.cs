using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace SEngine
{
    class TestGame
    {
        int gameFieldWidth = 100;
        int gameFieldHeight = 25;
        int initCountBot = 1;
        int maxCountBot = 5;
        Field gameField;
        Tank tank;
        
        Layout mainLayout;
        Layout bulletsLayout;

        List<Tank> bots = new List<Tank>();
        GameTime gameTime = new GameTime();
        Random r = new Random();

        public bool GameIsRunning = false;
        public int TimeDelay = 20;
        public float TimeDrawDelay = 100;
        public static int CountKilled = 0;
        public static int CountShoot = 0;

        public void Start()
        {
            Init();
            GameIsRunning = true;

            while (GameIsRunning) {

                gameTime.Start();

                Update(gameTime);

                if (TimeDrawDelay > 100) {
                    Draw(gameTime);
                    TimeDrawDelay = 0;
                }

                Thread.Sleep(TimeDelay);

                gameTime.Stop();
                gameTime.LoopTime = gameTime.Timer.ElapsedMilliseconds;
                TimeDrawDelay += gameTime.LoopTime;
                gameTime.Reset();
            }
        }

        protected void Init()
        {
            Console.Title = "--SEngine--";
            Console.WindowHeight = gameFieldHeight + 5;
            Console.BufferHeight = gameFieldHeight + 5;
            Console.WindowWidth = gameFieldWidth + 10;
            Console.BufferWidth = gameFieldWidth + 10;
            Console.CursorVisible = false;

            mainLayout = new Layout(gameFieldWidth, gameFieldHeight);
            bulletsLayout = new Layout(gameFieldWidth, gameFieldHeight);

            gameField = new Field(mainLayout);
            gameField.AddLayout(bulletsLayout);

            //tank = new Tank(1, 1, "x---x|1---1|-1-1-|--*--|--1-x|---1-");
            //tank.IsOriginGun = true;
            tank = new Tank(1, 1, "-x-|1*1|1-1");
            tank.SetDirection(Direction.Right);
            tank.AttachControl(Control.DirectionControl);
            mainLayout.AddFigure(tank);

            for (int i = 0; i < initCountBot; i++) {
                Tank newTank = new Tank(r.Next(25, 70), r.Next(3, 20), "-x-|1*1|1-1", true);
                newTank.AttachControl(Control.BotControl);
                //newTank.Color = ConsoleColor.Yellow;
                mainLayout.AddFigure(newTank);
                bots.Add(newTank);
            }
        }

        private float timeCreateBot = 3000;
        private float timeBotTankShoot = 2000;
        private int level = 1;
        protected void Update(GameTime gameTime)
        {
            timeCreateBot += gameTime.LoopTime;
            timeBotTankShoot += gameTime.LoopTime;
            tank.TimeStep += gameTime.LoopTime;

            if (CountKilled >= 10 && level == 1) {
                tank.LoadNewModel("-x-|-x-|1*1|1-1");
                level = 2;
            } else if (CountKilled >= 20 && level == 2) {
                tank.LoadNewModel("--x--|--x--|11*11|11111|11-11");
                level = 3;
            } else if (CountKilled >= 30 && level == 3) {
                tank.LoadNewModel("--x--|--1--|-x1x-|11*11|11111|11-11");
                level = 4;
            } else if (CountKilled >= 40 && level == 4) {
                tank.LoadNewModel("x---x|-1-1-|--*--|--1-x|---1-");
                tank.IsOriginGun = true;
                level = 5;
            } else if (CountKilled >= 50 && level == 5) {
                tank.LoadNewModel("x---x|1---1|-1-1-|--*--|--1-x|---1-");
                tank.IsOriginGun = true;
                level = 6;
            }

            tank.Update(gameTime);

            foreach (Tank t in bots) {
                t.Update(gameTime);
            }

            if (KeyBoard.getState().IsKeyDown(Keys.LCONTROL) || KeyBoard.getState().IsKeyDown(Keys.SPACE)) {
                if (tank.TimeStep >= 600) {

                    tank.Shoot(tank.CurrentDirection);
                    CountShoot++;
                    bulletsLayout.RemoveAllFigures();

                    for (int i = 0; i < tank.getBullets().Length; i++) {
                        Bullet b = tank.getBullets()[i];
                        if (!b.NotAlive())
                            bulletsLayout.AddFigure(b);
                    }
                    Console.Beep(5000, 20);

                    tank.TimeStep = 0;
                }
            }

            Bullet[] bullets = tank.getBullets();
            for (int j = 0; j < bullets.Length; j++) {
                for (int i = 0; i < bots.Count; i++) {
                    Bullet b = bullets[j];
                    Tank t = bots[i];

                    if (t.HasPoint(b.X, b.Y)) {
                        if (t.Strike()) {
                            mainLayout.Remove(t);
                            bots.Remove(t);
                            i--;
                            CountKilled++;
                            Console.Beep(1000, 5);
                        } else {
                            Console.Beep(3000, 5);
                        }
                        bulletsLayout.Remove(b);
                        tank.RemoveBullet(b);
                    }
                }
            
            }

            /*if (KeyBoard.getState().IsKeyDown(Keys.SPACE)) {
                foreach (Tank t in bots)
                    t.DettachControl();
            }*/

            /*if (timeBotTankShoot > 5000) {
                foreach (Tank t in bots) {
                    t.Shoot(t.CurrentDirection);
                    foreach (Bullet b in t.getBullets()) {
                        bulletsLayout.AddFigure(b);
                    }
                }
                timeBotTankShoot = 0;
            }*/

            if (timeCreateBot > 2000 && bots.Count < maxCountBot) {
                Tank newTank = new Tank(r.Next(25, 70), r.Next(3, 20), "-x-|1*1|1-1", true);
                newTank.AttachControl(Control.BotControl);
                //newTank.Color = r.Next(0, 2) == 1 ? ConsoleColor.Red : ConsoleColor.Yellow;
                mainLayout.AddFigure(newTank);
                bots.Add(newTank);
                timeCreateBot = 0;
            }
        }

        protected void Draw(GameTime gameTime)
        {
            Console.Clear();
            gameField.Draw();
            Console.WriteLine("Score: {0}  Tank level: {1}  Count shoot: {2}", CountKilled, level, CountShoot);
            Console.WriteLine("Bots: {0}", bots.Count);
        }
    }
}
