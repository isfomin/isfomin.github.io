using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    public class Shape
    {
        public ConsoleColor Color { get; set; }

        /// <summary>
        /// Точка относительно которой вращается фигура
        /// </summary>
        public PointShape OriginPoint { get; private set; }
        
        /// <summary>
        /// Координа X верхнего левого угла фигуры
        /// </summary>
        public int X { get; private set; }

        /// <summary>
        /// Координата Y верхнего левого угла фигуры
        /// </summary>
        public int Y { get; private set; }
        
        /// <summary>
        /// Ширина фигуры
        /// </summary>
        public int Width { get; private set; }

        /// <summary>
        /// Высота фигуры
        /// </summary>
        public int Height { get; private set; }

        /// <summary>
        /// Коллекция хранящая все точки фигуры
        /// </summary>
        private PointsCollection _pointsCollection;

        public Shape(int x, int y, string source)
        {
            this.X = x;
            this.Y = y;
            LoadNewModel(source);
            Color = ConsoleColor.Cyan;
        }

        /// <summary>
        /// Проверяет существует ли точка фигуры с задаными координатами
        /// </summary>
        /// <param name="X">Координата по оси X</param>
        /// <param name="Y">Координата по оси Y</param>
        /// <returns>True существует, False не существут</returns>
        public bool HasPoint(int X, int Y)
        {
            return _pointsCollection.HasPoint(X, Y);
        }

        /// <summary>
        /// Отражает фигуру по вертикали относительно точки origin
        /// </summary>
        public void ReverberateVertical()
        {
            //if (OriginPoint == null)
            //    OriginPoint = FindOriginPoint();

            foreach (PointShape p in AllPointsFigure()) {
                p.Y = OriginPoint.Y + (OriginPoint.Y - p.Y);
            }
        }

        /// <summary>
        /// Отражает фигуру по горизонтали относительно точки origin
        /// </summary>
        public void ReverberateHorizontal()
        {
            //if (OriginPoint == null)
            //    OriginPoint = FindOriginPoint();

            foreach (PointShape p in AllPointsFigure()) {
                p.X = OriginPoint.X + (OriginPoint.X - p.X);
            }
        }

        /// <summary>
        /// Отраженает фигуру по главной диагонали относительно точки origin
        /// </summary>
        public void Transposition()
        {
            //if (OriginPoint == null)
            //    OriginPoint = FindOriginPoint();

            foreach (PointShape p in AllPointsFigure()) {
                int newX = OriginPoint.X - (OriginPoint.Y - p.Y);
                int newY = OriginPoint.Y - (OriginPoint.X - p.X);
                p.X = newX;
                p.Y = newY;
            }
        }

        /// <summary>
        /// Получает все координаты фигуры
        /// </summary>
        /// <returns>Массив точек</returns>
        public PointShape[] AllPointsFigure()
        {
            return _pointsCollection.AllPoints();
        }

        /// <summary>
        /// Устанавливает положения фигуру по заданным координатам
        /// </summary>
        /// <param name="x">Координата по оси X</param>
        /// <param name="y">Координата по оси Y</param>
        public void SetPosition(int x, int y)
        {
            int dX = x - X;
            int dY = y - Y;
            X = x;
            Y = y;

            foreach (PointShape p in AllPointsFigure()) {
                p.X += dX;
                p.Y += dY;
            }
        }

        /// <summary>
        /// Смещает фигуру на заданное число
        /// </summary>
        /// <param name="offsetX">Смещение по оси X</param>
        /// <param name="offsetY">Смещение по оси Y</param>
        public void OffsetPosition(int offsetX, int offsetY)
        {
            SetPosition(X + offsetX, Y + offsetY);
        }

        /// <summary>
        /// Загружает новую модель фигуры
        /// </summary>
        /// <param name="source">Строка описывающая модель</param>
        public virtual void LoadNewModel(string source)
        {
            _pointsCollection = Load(source);
            OriginPoint = FindOriginPoint();
        }

        /// <summary>
        /// Создает коллекцию точек фигуры из строки
        /// </summary>
        /// <param name="source">Строка описывающая фигуру. Пример: "-x-|1*1|1-1"</param>
        /// <returns>Коллекция точек</returns>
        private PointsCollection Load(string source)
        {
            PointsCollection newCollection = new PointsCollection();
            int x = this.X;
            int y = this.Y;
            int maxX = 0;

            for (int i = 0; i < source.Length; i++) {
                switch (source[i]) {
                    case '1': {
                            newCollection.AddPoint(new PointShape(x, y));
                            x++;
                            if (maxX < x)
                                maxX = x;
                            break;
                        }
                    case '|': {
                            x = this.X;
                            y++;
                            break;
                        }
                    case '*': {
                            newCollection.AddPoint(new PointShape(x, y, true));
                            x++;
                            break;
                        }
                    case 'x': {
                            newCollection.AddPoint(new PointShape(x, y, false, true));
                            x++;
                            break;
                        }
                    default: {
                            x++;
                            break;
                        }
                }
            }

            this.Width = maxX - X;
            this.Height = y - Y + 1;

            return newCollection;
        }

        /// <summary>
        /// Ищет точку origin 
        /// </summary>
        /// <returns>Точка origin</returns>
        protected PointShape FindOriginPoint()
        {
            return FindPoints(p => p.IsOrigin, 1)[0];
        }

        /// <summary>
        /// Ищет точку из которой будут производится выстрелы
        /// </summary>
        /// <param name="originIsGun">Является ли точки origin точкой оружия</param>
        /// <returns>Точки оружий</returns>
        protected PointShape[] FindGunPoints(bool originIsGun = false)
        {
            return FindPoints(p => p.IsGun || (originIsGun && p.IsOrigin));
        }

        /// <summary>
        /// Ищет точки по заданному lambda-выражению
        /// </summary>
        /// <param name="func">Lambda-выражение</param>
        /// <param name="maxCount">Максимальное число найденых элементов (-1 = все)</param>
        /// <param name="indexDefault">Если в коллекции по условию не будет найдено ни одного элемента, то вернется элемент с заданым индексом. Если ни чего не нужно вовращать, введите отрицательное значение</param>
        /// <returns>Найденные точки</returns>
        protected PointShape[] FindPoints(Func<PointShape, bool> func, int maxCount = -1, int indexDefault = 0)
        {
            PointsCollection foundPointsCollection = new PointsCollection();

            int counter = 0;
            foreach (PointShape p in AllPointsFigure().Where(func)) {
                foundPointsCollection.AddPoint(p);
                counter++;
                if (maxCount > 0 && counter == maxCount)
                    return foundPointsCollection.AllPoints();
            }

            if (foundPointsCollection.AllPoints().Length == 0 && indexDefault >= 0) {
                foundPointsCollection.AddPoint(AllPointsFigure()[indexDefault]);
            }

            return foundPointsCollection.AllPoints();
        }
    }
}
