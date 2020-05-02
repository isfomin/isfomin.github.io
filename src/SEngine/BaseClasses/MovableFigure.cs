using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SEngine
{
    class MovableFigure : Shape
    {
        /// <summary>
        /// Текущее направление фигуры
        /// </summary>
        public Direction CurrentDirection { get; private set; }
        
        /// <summary>
        /// Буфер для храненя следующего направления
        /// </summary>
        public Direction NewDirection { get; set; }
        
        // Временные переменные, в будущем необходимо удалить или перенести в другой класс.
        public float TimeStep { get; set; }
        public float TimeMove { get; set; }

        /// <summary>
        /// Направление устанавливаемое по умолчанию
        /// </summary>
        private Direction _defaultDirection;

        public MovableFigure(int x, int y, string source)
            : base(x, y, source)
        {
            _defaultDirection = Direction.Top;
        }

        /// <summary>
        /// Загружает новую модель фигуры и устанавливает текущее направление по умолчанию
        /// </summary>
        /// <param name="source">Строка описывающая модель</param>
        public override void LoadNewModel(string source)
        {
            CurrentDirection = _defaultDirection;
            base.LoadNewModel(source);
        }

        /// <summary>
        /// Устанавливает направление и при необходимости поворачивает фигуру
        /// </summary>
        /// <param name="direction">Направление</param>
        public void SetDirection(Direction direction)
        {
            if (CurrentDirection == direction)
                return;

            switch (direction) {
                case Direction.Top: {
                    if (CurrentDirection == Direction.Bottom) {
                        ReverberateVertical();
                    } else if (CurrentDirection == Direction.Left) {
                        TurnRight();
                    } else if (CurrentDirection == Direction.Right) {
                        TurnLeft();
                    }
                    break;
                }
                case Direction.Bottom: {
                    if (CurrentDirection == Direction.Top) {
                        ReverberateVertical();
                    } else if (CurrentDirection == Direction.Left) {
                        TurnLeft();
                    } else if (CurrentDirection == Direction.Right) {
                        TurnRight();
                    }
                    break;    
                }
                case Direction.Left: {
                    if (CurrentDirection == Direction.Top) {
                        TurnLeft();
                    } else if (CurrentDirection == Direction.Bottom) {
                        TurnRight();
                    } else if (CurrentDirection == Direction.Right) {
                        ReverberateHorizontal();
                    }
                    break;
                }
                case Direction.Right: {
                    if (CurrentDirection == Direction.Top) {
                        TurnRight();
                    } else if (CurrentDirection == Direction.Bottom) {
                        TurnLeft();
                    } else if (CurrentDirection == Direction.Left) {
                        ReverberateHorizontal();
                    }
                    break;    
                }
            }

            CurrentDirection = direction;
        }

        /// <summary>
        /// Перемещает фигуру в заданом направлении и на заданое растояние без поворота фигуры
        /// </summary>
        /// <param name="direction">Направление движения</param>
        /// <param name="spacing">Расстояние на которое нужно сместить фигуру</param>
        public void Step(Direction direction, int spacing = 1)
        {
            switch (direction) {
                case Direction.Top: {
                        OffsetPosition(0, -spacing);
                        break;
                    }
                case Direction.Bottom: {
                        OffsetPosition(0, spacing);
                        break;
                    }
                case Direction.Left: {
                        OffsetPosition(-spacing, 0);
                        break;
                    }
                case Direction.Right: {
                        OffsetPosition(spacing, 0);
                        break;
                    }
            }
        }

        /// <summary>
        /// Перемещает фигуру в текущем направлении
        /// </summary>
        /// <param name="spacing">Число на которое нужно переместить фигуру в текущем направлении</param>
        public void Move(byte spacing = 1)
        {
            Move(CurrentDirection, spacing);
        }

        /// <summary>
        /// Перемещает фигуру в указанном направлении, при необходимости поварачивает фигуру
        /// </summary>
        /// <param name="direction">Направление</param>
        /// <param name="spacing">Число на которое нужно переместить фигуру в заданом направлении</param>
        public void Move(Direction direction, byte spacing = 1)
        {
            switch (direction) {
                case Direction.Top: {
                        if (CurrentDirection == Direction.Top) {
                            Step(Direction.Top, spacing);
                        } else {
                            SetDirection(Direction.Top);
                        }
                        break;
                    }
                case Direction.Bottom: {
                        if (CurrentDirection == Direction.Bottom) {
                            Step(Direction.Bottom, spacing);
                        } else {
                            SetDirection(Direction.Bottom);
                        }
                        break;
                    }
                case Direction.Left: {
                        if (CurrentDirection == Direction.Left) {
                            Step(Direction.Left, spacing);
                        } else {
                            SetDirection(Direction.Left);
                        }
                        break;
                    }
                case Direction.Right: {
                        if (CurrentDirection == Direction.Right) {
                            Step(Direction.Right, spacing);
                        } else {
                            SetDirection(Direction.Right);
                        }
                        break;
                    }
            }
        }

        /// <summary>
        /// Поворот фигуры налево относительно текущего направления
        /// </summary>
        public void TurnLeft()
        {
            switch (CurrentDirection) {
                case Direction.Top: {
                        this.Transposition();
                        CurrentDirection = Direction.Left;
                        break;
                    }
                case Direction.Bottom: {
                        this.Transposition();
                        CurrentDirection = Direction.Right;
                        break;
                    }
                case Direction.Left: {
                        this.ReverberateHorizontal();
                        this.Transposition();
                        CurrentDirection = Direction.Bottom;
                        break;
                    }
                case Direction.Right: {
                        this.ReverberateHorizontal();
                        this.Transposition();
                        CurrentDirection = Direction.Top;
                        break;
                    }
            }
        }

        /// <summary>
        /// Поворот фигуры направо относительно текущего направления
        /// </summary>
        public void TurnRight()
        {
            switch (CurrentDirection) {
                case Direction.Top: {
                        this.Transposition();
                        this.ReverberateHorizontal();
                        CurrentDirection = Direction.Right;
                        break;
                    }
                case Direction.Bottom: {
                        this.Transposition();
                        this.ReverberateHorizontal();
                        CurrentDirection = Direction.Left;
                        break;
                    }
                case Direction.Left: {
                        this.Transposition();
                        CurrentDirection = Direction.Top;
                        break;
                    }
                case Direction.Right: {
                        this.Transposition();
                        CurrentDirection = Direction.Bottom;
                        break;
                    }
            }
        }
    }
}
