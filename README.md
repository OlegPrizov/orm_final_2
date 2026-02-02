# Итоговый проект по дисциплине ORM-фреймворки для Java.

- Просьба проверять не раньше 4 февраля, я сделал всё основное, но могут быть небольшие доделки.
- Система управления онлайн-курсами. Работа выполнена самостоятельно

## Технологии
- Java 17+
- Spring Boot 3.x
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven

## Как запустить проект?
1. Скачайте проект к себе на ПК ```git clone git@github.com:OlegPrizov/orm_final_2.git```
2. Перейдите в него ```cd orm_final_2/```
3. Лучше запускать проект из IntelliJ IDEA с установленным и включенным плагином lombok
4. В файле src/main/resources/application.yml настройте верный username, password и url в блоке datasource
5. Запустите PostgreSQL, создайте БД learning_platform
6. Запустите проект из корня с помощью ```mvn spring-boot:run```