pipeline {
    agent any

    tools {
        // Укажите имя JDK в вашем Jenkins (например, 'jdk17' или 'java')
        jdk 'jdk17'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Выберите браузер для UI-тестов')
        string(name: 'BASE_URL', defaultValue: 'https://www.aviasales.ru/', description: 'URL для UI-тестов')
        string(name: 'API_BASE_URL', defaultValue: 'https://api.travelpayouts.com', description: 'URL для API-тестов')
        password(name: 'API_TOKEN', defaultValue: '', description: 'Токен для API Aviasales')
        choice(name: 'TEST_SUITE', choices: ['all', 'ui', 'api'], description: 'Какие тесты запускать')
    }

    options {
        // Ограничение на время выполнения (защита от зависаний)
        timeout(time: 1, unit: 'HOURS')
        // Сохранять только 10 последних сборок
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Добавить метку времени в консоль
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                // Получаем код из Git
                checkout scm
            }
        }

        stage('Preparation') {
            steps {
                script {
                    // Даем права на выполнение gradlew (важно для Linux-агентов)
                    sh 'chmod +x gradlew'
                    echo "Запуск тестов: ${params.TEST_SUITE} в браузере ${params.BROWSER}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Формируем команду в зависимости от выбранного набора тестов
                    def testCommand = "./gradlew clean test "

                    // Передаем параметры через -D (Selenide и Owner их подхватят)
                    testCommand += "-Dselenide.browser=${params.BROWSER} "
                    testCommand += "-Dweb.baseUrl=${params.BASE_URL} "
                    testCommand += "-Dapi.baseUrl=${params.API_BASE_URL} "
                    testCommand += "-Dapi.token=${params.API_TOKEN} "

                    if (params.TEST_SUITE == 'ui') {
                        testCommand += "-Dtags=ui"
                    } else if (params.TEST_SUITE == 'api') {
                        testCommand += "-Dtags=api"
                    }

                    // Запускаем тесты
                    sh testCommand
                }
            }
        }
    }

    post {
        always {
            // Генерация и публикация Allure-отчета (требует установленного плагина Allure в Jenkins)
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]

            // Очистка рабочего пространства (опционально)
            cleanWs()
        }
    }
}