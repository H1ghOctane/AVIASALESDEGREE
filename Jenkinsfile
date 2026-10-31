pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    parameters {
        choice(name: 'TEST_SUITE', choices: ['all', 'ui', 'api'], description: 'Какие тесты запускать')
    }

    options {
        timeout(time: 1, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Chrome Dependencies') {
            steps {
                script {
                    // Устанавливаем Google Chrome и библиотеки для работы в headless-режиме
                    // Работает для агентов на базе Ubuntu/Debian
                    sh '''
                        echo "Установка зависимостей для Chrome..."
                        sudo apt-get update
                        sudo apt-get install -y wget gnupg2 libnss3 libgconf-2-4 libatk1.0-0 libatk-bridge2.0-0 libcups2 libdrm2 libxkbcommon0 libxcomposite1 libxdamage1 libxfixes3 libxrandr2 libgbm1 libasound2

                        wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
                        sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
                        sudo apt-get update
                        sudo apt-get install -y google-chrome-stable
                    '''
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    // Формируем команду
                    def testCommand = "./gradlew clean test "

                    // Передаем параметры для Selenide
                    testCommand += "-Dselenide.browser=chrome "
                    testCommand += "-Dselenide.headless=true "
                    testCommand += "-Dselenide.browserSize=1920x1080 "

                    // Передаем параметры для Owner (если нужно переопределить URL)
                    testCommand += "-Dweb.baseUrl=https://www.aviasales.ru/ "
                    testCommand += "-Dapi.baseUrl=https://api.travelpayouts.com "

                    // Если токен хранится в Credentials Jenkins, раскомментируйте и настройте:
                    // withCredentials([string(credentialsId: 'aviasales-api-token', variable: 'API_TOKEN')]) {
                    //     testCommand += "-Dapi.token=\${API_TOKEN} "
                    // }

                    if (params.TEST_SUITE == 'ui') {
                        testCommand += "-Dgroups=ui"
                    } else if (params.TEST_SUITE == 'api') {
                        testCommand += "-Dgroups=api"
                    }

                    sh testCommand
                }
            }
        }
    }

    post {
        always {
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
            cleanWs()
        }
    }
}