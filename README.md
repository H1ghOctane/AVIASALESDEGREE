<h1 align="center">Автотесты для <a href="https://www.aviasales.ru">Aviasales</a></h1>

<p align="center">
  <a href="https://www.aviasales.ru"><img src="images/logo/aviasales.svg" width="320" alt="Aviasales"></a>
</p>

Дипломный проект QA.GURU. В репозитории UI-тесты сайта [aviasales.ru](https://www.aviasales.ru) и API-тесты двух сервисов: справочников и цен [Travelpayouts](https://support.travelpayouts.com/hc/ru/articles/203956163) (API Aviasales) и учебного API [Restful-booker](https://restful-booker.herokuapp.com/apidoc/index.html). Мобильные тесты лежат отдельно, в репозитории [MOBILEREST](https://github.com/H1ghOctane/MOBILEREST)

## Содержание

- [Стек](#стек)
- [Что проверяют тесты](#что-проверяют-тесты)
- [Структура проекта](#структура-проекта)
- [Запуск](#запуск)
- [Сборка в Jenkins](#сборка-в-jenkins)
- [Отчёт Allure](#отчёт-allure)
- [Уведомления в Telegram](#уведомления-в-telegram)
- [Видео прохождения теста](#видео-прохождения-теста)

## Стек

| Назначение | Инструмент | Версия |
| --- | --- | --- |
| Язык | Java | 17 |
| Сборка | Gradle | 8.13 |
| Тестовый фреймворк | JUnit 5 | 5.10.1 |
| UI | Selenide | 7.16.0 |
| API | REST Assured | 5.4.0 |
| Проверки | AssertJ | 3.24.2 |
| Модели | Lombok, Jackson | 1.18.30, 2.16.1 |
| Конфигурация | Owner | 1.0.12 |
| Тестовые данные | JavaFaker | 1.0.2 |
| Отчёты | Allure Report | 2.24.0 |
| Удалённые браузеры | Selenoid | |
| CI | Jenkins | |

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

## Сборка в [Jenkins](https://jenkins.qa.guru/job/degree_pravki/)

При запуске можно выбрать набор тестов, браузер, его версию и размер окна

<img src="images/logo/Jenkins.svg" alt="https://jenkins.qa.guru/view/qa-guru-students/job/degree_pravki/build?delay=0sec">

## [Отчёт Allure](https://jenkins.qa.guru/job/degree_pravki/lastCompletedBuild/allure/)

В отчёте у каждого шага есть вложения. У API-тестов это запрос и ответ, токены в них скрыты. У UI-тестов это скриншот, исходный код страницы, лог консоли браузера и видео из Selenoid

<img src="images/allure-overview.png" alt="Главная страница отчёта Allure">

<img src="images/allure-tests.png" alt="Список тестов в отчёте Allure">

## Уведомления в Telegram

После каждой сборки бот присылает в чат результат прогона и ссылку на отчёт

<img src="images/telegram.jpg" width="450" alt="Уведомление в Telegram">

## Видео прохождения теста

Видео из Selenoid, тест поиска в одну сторону
