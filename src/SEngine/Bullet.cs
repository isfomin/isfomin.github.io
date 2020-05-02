using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    class Bullet : MovableFigure
    {
        private int _ttl = 120;

        public Bullet(int x, int y, string source)
            : base(x, y, source)
        {}

        public bool NotAlive()
        {
            if (_ttl < 0)
                return true;
            return false;
        }

        public void Update()
        {
            Move(CurrentDirection);
            _ttl--;
        }
    }
}