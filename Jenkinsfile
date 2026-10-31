pipeline {
    agent any

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

        stage('Install Chrome') {
            steps {
                script {
                    // Устанавливаем Google Chrome и библиотеки для работы в headless-режиме
                    sh '''
                        echo "Установка Chrome..."
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

                    // Запускаем локально (без Selenoid), Selenide сам скачает драйвер
                    def testCommand = "./gradlew clean test "

                    // Передаем параметры для Headless-режима
                    testCommand += "-Dselenide.browser=chrome "
                    testCommand += "-Dselenide.headless=true "
                    testCommand += "-Dselenide.browserSize=1920x1080 "

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
        }
    }
}