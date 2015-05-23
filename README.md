# ICQ_Killer_server

Вторая лабораторная по курсу проектирования в МГТУ им. Баумана. 2015 год.
Серверная часть с логированием и шифрованием.
Клиент: ICQ_Killer. (написан на андроиде) или десктопный (написан на javascript).

Условные обозначения:
1) root = корневая папка проекта (по умолчанию ICQ_Killer_server).

Приложение разрабатывалось с использованием: 
 - ОС ubuntu >= 12.04
 - open-jdk-8 (уровень языка: Java 8)
 - Intellij IDEA >= 13 
 - maven-4.0.0 (для автоматизации сборки)

Для десктоп-клиента использовались библиотеки (лежат в root/src/main/static):
 - jquery-2.1.3
 - Bootstrap v3.3.2

Также для работы приложения необходимы дополнительные библиотеки для логирования и шифрования:
 - для логирования: https://github.com/nfetissow/Logger
 - для шифрования: https://github.com/nfetissow/ICQ_Killer_Crypto

Запуск сервера:

1) $ git clone https://github.com/xammi/ICQ_Killer_server
2) Открыть Intellij IDEA
3) Импорт проекта -> выбрать в появившемся диалоге root/pom.xml
4) Задать путь к JDK, если его нет в JAVA_HOME
5) Подождать пока откроется окно проекта и пройдет индексация
6) В верхнем меню: Run -> Edit Configurations -> + -> Main Class = Main -> Ok
7) В верхнем меню: File -> Project Structure -> Libraries -> + -> Logger -> + -> ICQ_Killer_server
8) Shift-F10

При правильном выполнении всех инструкций, в Messages появится запись, что сервер начал работать на порту по умолчанию (=8082).
Чтобы изменить порт, необходимо перейти в root/conf/config.xml и поменять в секции general опцию port.
В случае, если проект все-таки не запустился перепроверьте версии maven, jdk, и т. д.