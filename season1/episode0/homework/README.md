## Домашнее задание №1

Требуется реализовать вычисление чисел Фибоначчи по индексу, например, 
```
F(1)=1
F(2)=1
F(3)=2
F(5)=5
```
и так далее. Поскольку числа Фибоначчи очень быстро растут (экспоненциально), то для вычиления значений, соответсвующим 
большим индексам нельзя использовать примитивные целостчисленные типы (long, int). Поэтому необходимо самостоятельно 
реализовать длинную арфиметику на массиве long[] (byte[]) так, что каждый следующий элемент массива "удлиняет" двоичное 
представление числа. Например, четыре числа byte (массив byte[] из четырёх элементов) 
```
00000001 00000001 00000001 00000001 
```
(здесь единичка установлена в младшем бите каждого байта) представляет число **16843009** 
(1 + 2<sup>8</sup> + 2<sup>16</sup> + 2<sup>24</sup>)  
Для решения задания требуется реализовать интерфейс 
[edu.technopolis.FibonacciAlgorithm](https://github.com/TimurTechnopolis/advanced_java_course_2017/blob/master/season1/episode0/homework/src/main/java/edu/technopolis/FibonacciAlgorithm.java)
Реализацию можно проверить на тестовом классе [edu.technopolis.Test](https://github.com/TimurTechnopolis/advanced_java_course_2017/blob/master/season1/episode0/homework/src/test/java/edu/technopolis/Test.java) 
