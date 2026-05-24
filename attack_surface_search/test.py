import math

# Ввод данных
radius_A = float(input())
length_B = float(input())

# Вычисление диаметра A
diameter_A = 2 * radius_A

# Вычисление радиуса B из длины окружности (используем math.pi)
radius_B = length_B / (2 * math.pi)

# Сравнение радиусов с точностью до 2 знаков после запятой
if abs(radius_A - radius_B) < 0.001:
    status = 'равна'
elif radius_A < radius_B:
    status = 'внутри'
else:
    status = 'снаружи'

# Вывод результата с использованием f-строки
print(f'Окружность с центром в точке O диаметра {diameter_A} сантиметров находится {status} окружности с центром в точке O длиной {length_B} сантиметров.')