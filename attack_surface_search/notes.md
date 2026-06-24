Для тестирования необходимо получить jar-архив:
- `javac -d {имя папки, куда компилировать} {относительный путь до файлов java}/*.java`
- `jar cf {имя для jar архива}.jar {путь до папки с файлами .class}`

Локально тестовый jar-архив лежит по пути:
/Users/daniela/Desktop/maga_diplom/test_for_decompiling/bin
/Users/daniela/Desktop/maga_diplom/repo/attack_surface_search/src/main/java/org/example/
/Users/daniela/Desktop/maga_diplom/test/

Виды операций, момент взаимодействия с переменной data:
data =
data.
.(..., data, ...)
= data

препроцессинг: всё разделить пробелами: ( data , data_1 , ... )

могут быть одноимённые методы с разным количество аргументов
могут быть одинаковые методы с разным типом возвращаемых данных
может быть тернарный оператор
присваивание
вызов метода и дата в аргументе
вызов метода и как аргумент передаётся вызов метода с датой в аргументе
можно ли создавать классы в классах? - можно ли ориентироваться на файлы по имени?
могут быть запятые в ENUM между перечислением полей - важно ли это?

Метод это:
класс
название
количество аргументов
тип возвращаемых данных
??

как парсить метод:
я аргумент метода если я вхожу в последовательность вида ( ... , ... , ... , data , ... , ... , ... )
тогда я ищу в левом контексте прилегающую конструкцию вида text text или text . text или начало строки + text (когда вызов метода того же класса)

Интерфейсы:
можно возвращать интерфейсы
нельзя инициализировать переменную интерфейсом
следовательно нельзя передать в метод интерфейс, НО!
только если этот интерфейс был передан в аргумент функции, внутри которой вызывается этот метод
Следовательно по цепочке в какой-то изначальный момент в функцию всё-таки передавали инициализацию кокнретным классом

если две цепочки сводятся в один метод = в рассматриваемом методе в другой аргумент передан интерфейс, который возник из другой цепочки

https://habr.com/ru/companies/dsec/articles/702652/

Библиотека JavaParser распространяется под двойной лицензией (Dual License), что делает её очень гибкой для разработчиков:
LGPL (Lesser General Public License) — позволяет использовать библиотеку в закрытых коммерческих проектах. Основное требование: если вы вносите изменения в саму библиотеку JavaParser, вы должны опубликовать эти правки под той же лицензией.
Apache License 2.0 — максимально разрешительная лицензия. Она позволяет использовать, изменять и распространять код как в открытых, так и в коммерческих (закрытых) проектах без особых ограничений.

Нужно сравнивать типы аргументов тоже

CtExecutable
├── CtMethod          - обычные методы
├── CtConstructor     - конструкторы
├── CtLambda          - лямбда-выражения
└── CtAnonymousExecutable - статические/нестатические инициализаторы


CtType
├── CtClass          — классы (обычные, абстрактные, внутренние, анонимные) - есть конструкторы
├── CtInterface      — интерфейсы - нет
├── CtEnum           — перечисления (enum) - специальные конструкторы
└── CtAnnotationType — аннотации (@interface) - нет


CtExpression
├── CtInvocation                    — вызов метода (myMethod())
│   ├── CtExecutableReference       — ссылка на вызываемый метод
│   └── getArguments()              — аргументы вызова
│
├── CtConstructorCall               — вызов конструктора (new MyClass())
│   ├── CtExecutableReference       — ссылка на конструктор
│   └── getArguments()              — аргументы конструктора
│
├── CtLambda                        — лямбда-выражение (x -> x * 2)
│   ├── getExpression()             — тело лямбды
│   └── getParameters()             — параметры лямбды
│
├── CtExecutableReferenceExpression  — ссылка на метод (MyClass::myMethod)
│   └── getExecutable()             — ссылка на метод
│
├── CtTypeAccess                    — доступ к типу (MyClass.class)
│   └── getAccessedType()           — тип, к которому обращаются
│
├── CtLiteral                       — литерал (42, "hello")
│   └── getValue()                  — значение литерала
│
├── CtVariableRead                  — чтение переменной (myVar)
│   └── getVariable()               — ссылка на переменную
│
├── CtVariableWrite                 — запись переменной (myVar = value)
│   ├── getVariable()               — ссылка на переменную
│   └── getAssigned()               — присваиваемое значение
│
├── CtFieldRead                     — чтение поля (obj.field)
│   └── getVariable()               — ссылка на поле
│
├── CtFieldWrite                    — запись поля (obj.field = value)
│   ├── getVariable()               — ссылка на поле
│   └── getAssigned()               — присваиваемое значение
│
├── CtArrayRead                     — чтение элемента массива (arr[0])
│   └── getIndexExpression()        — индекс
│
├── CtArrayWrite                    — запись в массив (arr[0] = value)
│   ├── getIndexExpression()        — индекс
│   └── getAssigned()               — присваиваемое значение
│
├── CtBinaryOperator                — бинарная операция (a + b)
│   ├── getLeftHandOperand()        — левый операнд
│   └── getRightHandOperand()       — правый операнд
│
├── CtUnaryOperator                 — унарная операция (!flag, -value)
│   └── getOperand()                — операнд
│
├── CtConditional                   — тернарный оператор (x > 0 ? a : b)
│   ├── getCondition()              — условие
│   ├── getThenExpression()         — выражение для true
│   └── getElseExpression()         — выражение для false
│
├── CtNewArray                      — создание массива (new int[]{1, 2})
│   └── getElements()               — элементы массива
│
├── CtNewClass                      — создание анонимного класса
│   └── getAnonymousClass()         — тело анонимного класса
│
├── CtCast                          — приведение типа ((String) obj)
│   ├── getType()                   — тип приведения
│   └── getExpression()             — приводимое выражение
│
├── CtThisAccess                    — доступ к this
│   └── getTarget()                 — целевой тип (для this в анонимных классах)
│
├── CtSuperAccess                   — доступ к super
│   └── getTarget()                 — целевой тип
│
├── CtEnumValueRead                 — чтение значения enum (MyEnum.VALUE)
│   └── getVariable()               — ссылка на значение enum
│
├── CtAssert                        — assert выражение
│   ├── getAssertExpression()       — проверяемое выражение
│   └── getExpression()             — сообщение об ошибке
│
├── CtBreak                         — break оператор
│   └── getTargetLabel()            — метка (если есть)
│
├── CtContinue                      — continue оператор
│   └── getTargetLabel()            — метка (если есть)
│
├── CtReturn                        — return оператор
│   └── getReturnedExpression()     — возвращаемое выражение
│
├── CtThrow                         — throw оператор
│   └── getThrownExpression()       — выбрасываемое исключение
│
└── CtAnnotation                    — аннотация (@Override)
├── getAnnotationType()         — тип аннотации
└── getValues()                 — значения параметров аннотации

1) наладить читаемый вывод +-
2) интерфейсы: все реализации +
3) присваивание
4) поля класса (контекст)
5) интерфейсы, которые можно реализовать
6) собственные аннотации
7) подгрузка базовых либ -

Решить проблему с повторным вызовом одних и тех же методов в одном методе

добавить обработку методов сторонних либ

1. У интерфейса может быть реализация метода?

Да, может. Начиная с Java 8, у интерфейсов появились два типа методов с реализацией: static и default.

static методы: Это служебные методы, принадлежащие самому интерфейсу. Класс, который реализует интерфейс, не наследует и не может переопределить такой метод. Вызов возможен только через имя интерфейса: MyInterface.staticMethod().
default методы: Это методы экземпляра, которые имеют реализацию "по умолчанию". Класс, реализующий интерфейс, может их:

Унаследовать (если не переопределяет явно).
Переопределить (если нужна своя логика).
Вызвать у конкретного объекта: myObject.defaultMethod().


CtVariableRead не ограничивается только локальными переменными. Он представляет чтение любой переменной в Java:

Тип переменной	Пример	CtVariableRead?
Локальная переменная	int x = y;	✅ Да (y)
Параметр метода	void method(int param) { int x = param; }	✅ Да (param)
Поле класса	int x = this.field;	✅ Да (this.field)
Статическое поле	int x = MyClass.STATIC_FIELD;	✅ Да
Элемент массива	int x = arr[0];	❌ Нет (CtArrayRead)

Ситуация	Тип в Spoon
int x = localVar;	CtVariableRead (не CtFieldRead)
int x = param;	CtVariableRead (не CtFieldRead)
int x = field;	CtFieldRead
int x = this.field;	CtFieldRead
int x = obj.field;	CtFieldRead
int x = Class.STATIC_FIELD;	CtFieldRead


npm start  