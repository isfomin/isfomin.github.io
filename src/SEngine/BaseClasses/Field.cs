using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    public class Field
    {
        public int Width { get; private set; }
        public int Height { get; private set; }
        
        private List<Layout> _layouts;
        private PointsCollection _globalLayout;

        public Field(Layout l)
        {
            Width = l.Width;
            Height = l.Height;
            _globalLayout = new PointsCollection();
            _layouts = new List<Layout>();
            _layouts.Add(l);
        }

        /// <summary>
        /// Добавлить слой к игровому полю
        /// </summary>
        /// <param name="l">Слой</param>
        public void AddLayout(Layout l)
        {
            _layouts.Add(l);
        }

        /// <summary>
        /// Прорисовывает игровое поле
        /// </summary>
        public void Draw()
        {
            /*_globalLayout = new PointsCollection();

            string result = "";
            string lastLine = "";

            foreach (Layout l in _layouts) {
                l.SetState(ref _globalLayout);
            }

            for (int j = 0; j < Height; j++) {
                for (int i = 0; i < Width; i++) {
                    result += !_globalLayout.HasPoint(i, j) ? " " : "1";
                    if (i + 1 == Width)
                        result += "|";
                    if (j + 1 == Height)
                        lastLine += "-";
                }
                Console.WriteLine(result);
                result = "";
            }

            Console.WriteLine(lastLine);*/

            for (int l = 0; l < _layouts.Count; l++) {
                Shape[] figures = _layouts[l].getFigures();
                
                for (int f = 0; f < figures.Length; f++) {
                    PointShape[] points = figures[f].AllPointsFigure();

                    for (int p = 0; p < points.Length; p++) {
                        
                        if (points[p].X >= Width || points[p].Y >= Height || points[p].X < 0 || points[p].Y < 0)
                            continue;
                        
                        Console.SetCursorPosition(points[p].X, points[p].Y);
                        if (Console.BackgroundColor != figures[f].Color)
                            Console.BackgroundColor = figures[f].Color;
                        Console.Write(" ");
                        //Console.ForegroundColor = figures[f].Color;
                        //Console.Write("1");
                    }
                    Console.ResetColor();
                }
            }

            //Console.BackgroundColor = ConsoleColor.White;
            Console.SetCursorPosition(0, Height + 1);
        }
    }
}
