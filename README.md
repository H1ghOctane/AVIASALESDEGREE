<h1 align="center">Автотесты для <a href="https://www.aviasales.ru">Aviasales</a></h1>

<p align="center">
  <a href="https://www.aviasales.ru"><img src="images/logo/photo_2026-11-05_16-43-23.png" alt="Aviasales"></a>
</p>

Дипломный проект QA.GURU. В репозитории UI-тесты сайта [aviasales.ru](https://www.aviasales.ru) и API-тесты [Travelpayouts](https://support.travelpayouts.com/hc/ru/articles/203956163), партнёрского API самого Aviasales: справочник стран и цены на билеты. Мобильные тесты лежат отдельно, в репозитории [MOBILEDEGREE](https://github.com/H1ghOctane/MOBILEDEGREE)

## Содержание

- [Технологии и инструменты](#технологии-и-инструменты)
- [Что проверяют тесты](#что-проверяют-тесты)
- [Структура проекта](#структура-проекта)
- [Запуск](#запуск)
- [Сборка в Jenkins](#сборка-в-jenkins)
- [Отчёт Allure](#отчёт-allure)
- [Интеграция AllureTestOps](#интеграция-alluretestops)
- [Интеграция с Jira](#интеграция-с-jira)
- [Уведомления в Telegram](#уведомления-в-telegram)
- [Видео прохождения теста](#видео-прохождения-теста)

<a id="tools"></a>
## Технологии и инструменты:
| Java | IntelliJ <br> IDEA | GitHub | JUnit 5 | Gradle | Selenide | REST <br> Assured | Selenoid | Allure <br> Report | Allure <br> TestOps | Jenkins | Jira | Telegram |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| <a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50" alt="Java"/></a> | <a href="https://www.jetbrains.com/idea/"><img src="images/logo/Idea.svg" width="50" height="50" alt="IntelliJ IDEA"/></a> | <a href="https://github.com/"><img src="images/logo/GitHub.svg" width="50" height="50" alt="GitHub"/></a> | <a href="https://junit.org/junit5/"><img src="images/logo/Junit5.svg" width="50" height="50" alt="JUnit 5"/></a> | <a href="https://gradle.org/"><img src="images/logo/Gradle.svg" width="50" height="50" alt="Gradle"/></a> | <a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50" alt="Selenide"/></a> | <a href="https://rest-assured.io/"><img src="images/logo/RestAssured.svg" width="50" height="50" alt="REST Assured"/></a> | <a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50" alt="Selenoid"/></a> | <a href="https://github.com/allure-framework"><img src="images/logo/Allure.svg" width="50" height="50" alt="Allure Report"/></a> | <a href="https://qameta.io/"><img src="images/logo/Allure_TO.svg" width="50" height="50" alt="Allure TestOps"/></a> | <a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50" alt="Jenkins"/></a> | <a href="https://www.atlassian.com/software/jira/"><img src="images/logo/Jira.svg" width="50" height="50" alt="Jira"/></a> | <a href="https://telegram.org/"><img src="images/logo/Telegram.svg" width="50" height="50" alt="Telegram"/></a> |

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

Навигация (`NavigationTest`): из шапки открывается раздел «Отели», и в подборке «Где жить и что смотреть» есть Москва, Санкт-Петербург, Сочи и Калининград.

Выдача (`SearchFiltersTest`): после фильтра «Прямые рейсы» в выдаче нет билетов с пересадками. Тест отключён: при запуске в Selenoid Aviasales показывает на выдаче капчу и билеты не загружаются

### API, Travelpayouts

- Справочник стран `/data/ru/countries.json`: список не пустой, у каждой страны двухбуквенный код, коды не повторяются, ответ соответствует JSON-схеме, в списке есть Россия, Германия и Франция с правильными кодами (проверяется одним запросом)
- Цены `/aviasales/v3/prices_for_dates`: билеты относятся к запрошенному направлению, с `direct=true` в ответе только прямые рейсы, параметр `limit` ограничивает число билетов. Для этих тестов нужен токен, без него они пропускаются
- Авторизация в API цен: с неверным токеном сервер отвечает 401. Этот тест работает и без настоящего токена

## Структура проекта

```
src/test/java
├── api
│   ├── clients    клиенты API: GeoApi, PricesApi
│   ├── config     конфигурация Owner
│   ├── helpers    Allure-фильтр с шаблонами запросов и ответов
│   ├── models     модели запросов и ответов
│   ├── specs      спецификации REST Assured
│   └── tests
└── ui
    ├── config     конфигурация Owner и настройки браузеров
    ├── data       города и даты для тестов
    ├── helpers    вложения в Allure
    ├── pages      страницы: главная, выдача, отели
    │   └── components   шапка, календарь, выбор пассажиров
    └── tests
```

Тесты обращаются к сервису только через клиенты (API) и страницы (UI). Проверки написаны на AssertJ. В API-тестах запрос и проверки разделены: сначала ответ сохраняется в переменную, потом проверки идут отдельными шагами Allure

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
| `browser` | браузер: `chrome` или `firefox` | `chrome` |
| `browserVersion` | версия браузера | последняя доступная |
| `browserSize` | размер окна | `1920x1080` |
| `remoteUrl` | адрес Selenoid | не задан, браузер запускается локально |
| `travelpayouts.token` | токен Travelpayouts | не задан, тесты цен пропускаются |

Токен Travelpayouts удобно положить в файл `~/aviasales.properties` строкой `travelpayouts.token=...`, тогда его не нужно передавать при каждом запуске. В Jenkins токен и адрес Selenoid передаются параметрами `TRAVELPAYOUTS_TOKEN` и `REMOTE_URL` и в лог сборки не попадают

## Сборка в [Jenkins](https://jenkins.qa.guru/view/qa-guru-students/job/AVIASALESDEGREE/)

При запуске можно выбрать набор тестов, браузер, его версию и размер окна

<img src="images/logo/allure-report2.png" alt="Главная страница отчёта Allure">


## Отчёт [Allure](https://jenkins.qa.guru/view/qa-guru-students/job/AVIASALESDEGREE/1/allure/)

В отчёте у каждого шага есть вложения. У API-тестов это запрос и ответ, токены в них скрыты. У UI-тестов это скриншот, исходный код страницы, лог консоли браузера и видео из Selenoid
<img src="images/logo/allure-report1.png" alt="Список тестов в отчёте Allure">

<img src="images/logo/tata.png" alt="тесты Allure">

<img src="images/logo/allure-report3.png" alt="Графики в отчёте Allure">

## Интеграция [AllureTestOps](https://allure.qa.guru/project/5421/dashboards/5709)

<img src="images/logo/testOps2.png" alt="Графики в отчёте Allure">

Автоматизированные и ручные тест-кейсы:
<img src="images/logo/testOpsTests.png" alt="Графики в отчёте Allure">

## Интеграция с [Jira](https://jira.qa.guru/browse/NEWAPP-9)

<img src="images/logo/imgJira.png" alt="Задача Jira">

## Уведомления в Telegram

После каждой сборки бот присылает в чат результат прогона и ссылку на отчёт

<img src="images/logo/img_telegram.png" alt="Отчет в телеграмм">


## Видео прохождения теста

Видео из Selenoid, ошибка при вводе одинаковых городов «Укажите разные города»

https://github.com/user-attachments/assets/c07271ff-e913-43c7-afe1-7fcf4f5a546a


