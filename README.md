<h1 align="center">Автотесты для <a href="https://www.aviasales.ru">Aviasales</a></h1>

<p align="center">
  <a href="https://www.aviasales.ru"><img src="images/logo/photo_2026-11-05_16-43-23.png" alt="Aviasales"></a>
</p>

Дипломный проект QA.GURU. В репозитории UI-тесты сайта [aviasales.ru](https://www.aviasales.ru) и API-тесты двух сервисов: справочников и цен [Travelpayouts](https://support.travelpayouts.com/hc/ru/articles/203956163) (API Aviasales) и учебного API [Restful-booker](https://restful-booker.herokuapp.com/apidoc/index.html). Мобильные тесты лежат отдельно, в репозитории [MOBILEDEGREE](https://github.com/H1ghOctane/MOBILEDEGREE)

## Содержание

- [Технологии и инструменты](#технологии-и-инструменты)
- [Что проверяют тесты](#что-проверяют-тесты)
- [Структура проекта](#структура-проекта)
- [Запуск](#запуск)
- [Сборка в Jenkins](#сборка-в-jenkins)
- [Отчёт Allure](#отчёт-allure)
- [Уведомления в Telegram](#уведомления-в-telegram)
- [Видео прохождения теста](#видео-прохождения-теста)

<a id="tools"></a>
## Технологии и инструменты:

| Java                                                                                                      | IntelliJ  <br>  Idea                                                                                               | GitHub                                                                                                     | JUnit 5                                                                                                           | Gradle                                                                                                     | Selenide                                                                                                         | Selenoid                                                                                                                  | Allure <br> Report                                                                                                         |  Jenkins                                                                                                        |   Jira                                                                                                              | Telegram                                                                                                            |Allure <br> TestOps                                                                                                          
|:----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------:|
| <a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50"  alt="Java"/></a>  | <a href="https://www.jetbrains.com/idea/"><img src="images/logo/Idea.svg" width="50" height="50"  alt="IDEA"/></a> | <a href="https://github.com/"><img src="images/logo/GitHub.svg" width="50" height="50"  alt="Github"/></a> | <a href="https://junit.org/junit5/"><img src="images/logo/Junit5.svg" width="50" height="50"  alt="JUnit 5"/></a> | <a href="https://gradle.org/"><img src="images/logo/Gradle.svg" width="50" height="50"  alt="Gradle"/></a> | <a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50"  alt="Selenide"/></a> | <a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50"  alt="Selenoid"/></a> | <a href="https://github.com/allure-framework"><img src="images/logo/Allure.svg" width="50" height="50"  alt="Allure"/></a> |<a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50"  alt="Jenkins"/></a> | <a href="https://www.atlassian.com/software/jira/"><img src="images/logo/Jira.svg" width="50" height="50" alt="Java" title="Java"/></a> | <a href="https://web.telegram.org/"><img src="images\logo\Telegram.svg" width="50" height="50" alt="Telegram"/></a> |<a href="https://qameta.io/"><img src="images\logo\Allure_TO.svg" width="50" height="50" alt="Allure_TO"/></a> |

<a id="cases"></a>

## Что проверяют тесты

### UI, aviasales.ru

Поиск авиабилетов (`SearchFormTest`):
- поиск в одну сторону открывает выдачу, и в форме выдачи остаются введённые города
- поиск туда-обратно на двух пассажиров открывает выдачу с теми же городами
- кнопка смены городов меняет местами «Откуда» и «Куда»
- по началу названия («Каз», «Екат», «Кали») в подсказках появляется нужный город
- после выбора трёх взрослых в поле пассажиров написано «3 пассажира»

Валидация формы (`SearchValidationTest`):
- без города прибытия поиск не запускается, под полем «Куда» появляется ошибка «Укажите город прибытия»
- при одинаковых городах вылета и прилёта появляется ошибка «Укажите разные города»

Выдача (`SearchFiltersTest`): после фильтра «Прямые рейсы» в выдаче нет билетов с пересадками

Навигация (`NavigationTest`): из шапки открывается раздел «Отели», и в подборке «Где жить и что смотреть» есть Москва, Санкт-Петербург, Сочи и Калининград.

На выдаче Aviasales иногда показывает капчу. Тогда тест фильтра не может дождаться билетов

### API, Restful-booker

- `POST /auth`: с верным логином и паролем приходит токен, с неверным паролем токена нет, а в ответе `Bad credentials`
- `POST /booking`: бронирование создаётся, ответ совпадает с отправленными данными
- `GET /booking/{id}`: возвращает созданное бронирование, для несуществующего id отвечает 404
- `PUT /booking/{id}`: бронирование обновляется целиком
- `PATCH /booking/{id}`: меняется только переданное поле, остальные остаются прежними
- `DELETE /booking/{id}`: бронирование удаляется; без токена сервер отвечает 403 и бронирование остаётся

### API, Travelpayouts

- Справочник стран `/data/ru/countries.json`: список не пустой, у каждой страны двухбуквенный код, ответ соответствует JSON-схеме, в списке есть Россия, Германия и Франция с правильными кодами
- Цены `/aviasales/v3/prices_for_dates`: билеты относятся к запрошенному направлению, параметр `limit` ограничивает число билетов. Для этих тестов нужен токен; без него они пропускаются

## Структура проекта

```
src/test/java
├── api
│   ├── clients    клиенты API: BookerApi, GeoApi, PricesApi
│   ├── config     конфигурация Owner
│   ├── data       тестовые данные
│   ├── helpers    Allure-фильтр с шаблонами запросов и ответов
│   ├── models     модели запросов и ответов
│   ├── specs      спецификации REST Assured
│   └── tests
└── ui
    ├── config     конфигурация Owner
    ├── data       города и даты для тестов
    ├── helpers    вложения в Allure и ожидания
    ├── pages      страницы: главная, выдача, отели
    │   └── components   шапка, календарь, выбор пассажиров
    └── tests
```

Тесты обращаются к сервису только через клиенты (API) и страницы (UI). Проверки написаны на AssertJ

## Запуск

Все тесты:

```bash
./gradlew clean test
```

Отдельно API или UI:

```bash
./gradlew clean api
./gradlew clean ui
```

UI-тесты по умолчанию открывают Chrome на своём компьютере. Чтобы запустить их в Selenoid, передай адрес:

```bash
./gradlew clean ui -DremoteUrl=https://user:password@selenoid.qa.guru/wd/hub
```

Параметры запуска:

| Параметр | Что задаёт | По умолчанию |
| --- | --- | --- |
| `browser` | браузер | `chrome` |
| `browserVersion` | версия браузера | последняя доступная |
| `browserSize` | размер окна | `1920x1080` |
| `remoteUrl` | адрес Selenoid | не задан, браузер запускается локально |
| `travelpayouts.token` | токен Travelpayouts | не задан, тесты цен пропускаются |

Токен Travelpayouts удобно положить в файл `~/aviasales.properties` строкой `travelpayouts.token=...`, тогда его не нужно передавать при каждом запуске. В Jenkins токен и адрес Selenoid передаются параметрами `TRAVELPAYOUTS_TOKEN` и `REMOTE_URL` и в лог сборки не попадают

## Сборка в [Jenkins](https://jenkins.qa.guru/view/qa-guru-students/job/AVIASALESDEGREE/)

При запуске можно выбрать набор тестов, браузер, его версию и размер окна

<img src="images/logo/allure-report2.png" alt="Главная страница отчёта Allure">


## Отчет [Allure](https://jenkins.qa.guru/view/qa-guru-students/job/AVIASALESDEGREE/1/allure/)

В отчёте у каждого шага есть вложения. У API-тестов это запрос и ответ, токены в них скрыты. У UI-тестов это скриншот, исходный код страницы, лог консоли браузера и видео из Selenoid
<img src="images/logo/allure-report1.png" alt="Список тестов в отчёте Allure">

<img src="images/logo/allure-report3.png" alt="Графики в отчёте Allure">

## Уведомления в Telegram 

После каждой сборки бот присылает в чат результат прогона и ссылку на отчёт

<img src="images/logo/img_telegram.png" alt="Отчет в телеграмм">


## Видео прохождения UI теста

Видео из Selenoid, ошибка при вводе одинаковых городов «Укажите разные города»

https://github.com/user-attachments/assets/c07271ff-e913-43c7-afe1-7fcf4f5a546a


