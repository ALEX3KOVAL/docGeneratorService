Реактивный сервис электронного документооборота (генерация файлов по заданным docx-шаблонам, которые загружаются при обращении к [file-service](https://github.com/ALEX3KOVAL/Java-Spring-File-Service)).
Сервис событийно-ориентированный, используется Kafka.
Также для отказоустойчивости используется паттерн "Retry" (реализация с использованием Project Reactor).
Для реализации паттерна "TransactionalOutBox" используются 2 дополнительных приложения:
1) модуль eventHandlingApp - приложение, которое обрабатывает события;
2) модуль eventProducerApp - приложение, которое пушит события в шину из БД, используется CDC (Debezium).
Также имеется переиспользуемые библиотеки для event-driven (созданные Github Packages):
1) [Контракты для event-driven](https://github.com/ALEX3KOVAL/eventingContract);
2) [Реализации для event-driven](https://github.com/ALEX3KOVAL/eventingImpl);
3) [Реализация фасада приложения продюсирования событий в шину (TransactionalOutBox)](https://github.com/ALEX3KOVAL/transactionalOutBox);
4) [Реализация фасада приложения для обработки событий](https://github.com/ALEX3KOVAL/kafkaEventer).

В eventProducerApp импортирована конфигурация TransactionalOutBoxConfiguration из пакета [transactionalOutBox](https://github.com/ALEX3KOVAL/transactionalOutBox). Данная конфигурация содержит ApplicationRunner, который запускает DebeziumEngine.
В eventHandlingApp имеется EventHandlingAppRunner (implements ApplicationRunner), который регистрирует слушатели событий.
