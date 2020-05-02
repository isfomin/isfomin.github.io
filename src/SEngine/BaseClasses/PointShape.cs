using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    public class PointShape : Point
    {
        public bool IsOrigin { get; set; }
        public bool IsGun { get; set; }

        public PointShape(int X, int Y, bool isOrigin = false, bool isGun = false) 
            : base(X, Y)
        {
            this.IsOrigin = isOrigin;
            this.IsGun = isGun;
        }
    }
}
