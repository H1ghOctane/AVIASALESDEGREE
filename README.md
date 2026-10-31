🛫 Aviasales UI & API Tests

Репозиторий содержит тесты для веб-интерфейса и API Aviasales, а также конфигурацию Jenkins Pipeline для запуска в CI

Мобильные тесты вынесены в отдельный репозиторий: [MOBILEREST](https://github.com/H1ghOctane/MOBILEREST) (тестирование Wikipedia через Appium/BrowserStack)

✅ Стек
- Java 17
- Gradle
- JUnit 5
- Selenide
- REST Assured
- Owner
- Lombok
- AssertJ
- Allure Report
- Jenkins

📁 Структура

```text
src/test/java
├── api
│   ├── config      // ApiConfig (Owner)
│   ├── models      // Country (Lombok)
│   ├── specs       // RequestSpec, ResponseSpec
│   └── tests       // CountriesTest, CountrySchemaTest, CountrySearchTest
├── config          // WebConfig
├── ui
│   ├── pages       // MainPage
│   ├── tests       // ParameterizedTests
│   ├── CityEnum
│   └── TestBase
└── resources
    ├── config      // api.properties, web.properties
    └── schemas     // countries-schema.json


Локальный запуск UI-тестов
<img width="1280" height="652" alt="image" src="https://github.com/user-attachments/assets/f4bf319e-fc82-404c-88f4-2844f5f9ec2a" />

