using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    enum Strength
    {
        High,
        Medium,
        Low
    }

    class Tank : MovableFigure
    {
        public delegate void ControlDelegate(MovableFigure figure, GameTime gameTime);

        public bool Killed { get; private set; }
        public bool IsBot { get; private set; }
        public bool IsOriginGun { get; set; }
        
        private Strength strength;
        private List<Bullet> _bullets;
        private ControlDelegate control;

        public Tank(int x, int y, string source, bool isBot = false)
            : base(x, y, source)
        {
            _bullets = new List<Bullet>();
            Killed = false;
            IsBot = isBot;
            if (isBot) {
                TimeStep = 1500;
                TimeMove = 500;
                IsOriginGun = false;
                UpdateColor();
            } else {
                TimeStep = 800;
                TimeMove = 100;
                IsOriginGun = false;
            }
            strength = Strength.High;
        }

        public bool Strike()
        {
            if (strength == Strength.High) {
                strength = Strength.Medium;
                UpdateColor();
                return false;
            } else if (strength == Strength.Medium) {
                strength = Strength.Low;
                UpdateColor();
                return false;
            } else if (strength == Strength.Low) {
                this.Killed = true;
            }
            return true;
        }

        private void UpdateColor()
        {
            if (strength == Strength.High) {
                Color = ConsoleColor.Green;
            } else if (strength == Strength.Medium) {
                Color = ConsoleColor.Yellow;
            } else if (strength == Strength.Low) {
                Color = ConsoleColor.Red;
            }
        }

        public void AttachControl(ControlDelegate newControl)
        {
            control = newControl;
        }

        public void DettachControl()
        {
            control = null;
        }

        public void Shoot(Direction direction)
        {
            PointShape[] pointsGun = FindGunPoints(IsOriginGun);

            foreach (PointShape p in pointsGun) {
                Bullet b = new Bullet(p.X, p.Y, "1");
                b.SetDirection(CurrentDirection);
                b.Color = ConsoleColor.DarkCyan;
                _bullets.Add(b);
            }
        }

        private float timeMoveBullet = 100;
        public void Update(GameTime gameTime)
        {
            timeMoveBullet += gameTime.LoopTime;

            if (control != null)
                control(this, gameTime);

            if (timeMoveBullet > 100) {
                if (getBullets().Length > 0) {
                    for (int i = 0; i < _bullets.Count; i++) {
                        if (_bullets[i].NotAlive()) {
                            _bullets.Remove(_bullets[i]);
                            i--;
                        } else {
                            _bullets[i].Update();
                        }
                    }
                }
                timeMoveBullet = 0;
            }
        }

        public Bullet[] getBullets()
        {
            return _bullets.ToArray();
        }

        public void RemoveBullet(Bullet b)
        {
            _bullets.Remove(b);
        }
    }
}
