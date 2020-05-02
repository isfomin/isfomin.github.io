using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    public class PointsCollection
    {
        private List<PointShape> _points;

        public PointsCollection()
        {
            _points = new List<PointShape>();
        }

        /// <summary>
        /// Добавляет точку в коллекцию
        /// </summary>
        /// <param name="point">Новая точка</param>
        public void AddPoint(PointShape point)
        {
            _points.Add(point);
        }

        /// <summary>
        /// Добавляет несколько точек в коллекцию
        /// </summary>
        /// <param name="points">Новые точки</param>
        public void AddRangePoints(PointShape[] points)
        {
            _points.AddRange(points);
        }

        /// <summary>
        /// Возвращает точку по индексу
        /// </summary>
        /// <param name="index">Индекс точки</param>
        /// <returns>Найденая точка</returns>
        public PointShape GetPoint(int index)
        {
            return _points[index];
        }

        /// <summary>
        /// Проверяет принадлежит ли точка коллекции
        /// </summary>
        /// <param name="X">Координата по оси X</param>
        /// <param name="Y">Координата по оси Y</param>
        /// <returns>True принадлежит, False не принадлежит</returns>
        public bool HasPoint(int X, int Y)
        {
            IEnumerable<PointShape> queryTruePoints =
                from p in _points
                where p.X == X && p.Y == Y
                select p;

            if (queryTruePoints.Count() > 0) {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Возвращает список всех точек коллекции
        /// </summary>
        /// <returns>Массив точек</returns>
        public PointShape[] AllPoints()
        {
            return _points.ToArray();
        }

        /// <summary>
        /// Удаляет указанную точку
        /// </summary>
        /// <param name="point">Точка которую нужно удалить</param>
        public void RemovePoint(PointShape point)
        {
            _points.Remove(point);
        }
    }
}
