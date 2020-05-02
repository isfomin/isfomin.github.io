using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;

namespace SEngine
{
    public class GameTime
    {
        public float LoopTime { get; set; }
        public Stopwatch Timer { get; set; }

        public GameTime()
        {
            Timer = new Stopwatch();
            LoopTime = 0;
        }

        public void Start()
        {
            Timer.Start();
        }

        public void Stop()
        {
            Timer.Stop();
        }

        public void Reset()
        {
            Timer.Reset();
        }
    }
}
