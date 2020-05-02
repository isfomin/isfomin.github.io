using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    public class Layout
    {
        /// <summary>
        /// Ширина слоя
        /// </summary>
        public int Width { get; set; }
        
        /// <summary>
        /// Высота слоя
        /// </summary>
        public int Height { get; set; }

        /// <summary>
        /// Набор фигур на слое
        /// </summary>
        private List<Shape> _figures;

        public Layout(int width, int height)
        {
            Width = width;
            Height = height;
            _figures = new List<Shape>();
        }

        /// <summary>
        /// Добавляет фигуру на слой
        /// </summary>
        /// <param name="f">Фигура</param>
        public void AddFigure(Shape f)
        {
             if (!_figures.Contains(f))
                _figures.Add(f);
        }

        /// <summary>
        /// Удаляет заданную фигуру со слоя
        /// </summary>
        /// <param name="f">Удаляемая фигура</param>
        public void Remove(Shape f)
        {
            _figures.Remove(f);
        }

        /// <summary>
        /// Удаляет все фигуры
        /// </summary>
        public void RemoveAllFigures()
        {
            if (_figures.Count > 0) {
                _figures.RemoveRange(0, _figures.Count);
            }
        }

        /// <summary>
        /// Возвращает массив всех фигур слоя
        /// </summary>
        /// <returns>Массив фигур</returns>
        public Shape[] getFigures()
        {
            return _figures.ToArray();
        }

        /// <summary>
        /// (Метод устарел и не используется)
        /// Передает все фигуры текущего слоя в глобальный слой
        /// </summary>
        /// <param name="globalLayout">Глобальный слой</param>
        public void SetState(ref PointsCollection globalLayout)
        {
            foreach (Shape f in _figures) {
                globalLayout.AddRangePoints(f.AllPointsFigure());
            }
        }
    }
}
