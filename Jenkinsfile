pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: ['api', 'all'], description: 'Какие тесты запускать в CI (UI не запускаем из-за защиты Aviasales)')
    }

    options {
        timeout(time: 15, unit: 'MINUTES') // Ставим жесткий лимит
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run API Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    // Запускаем только API-тесты по тегу
                    def testCommand = "./gradlew clean test -Dgroups=api"

                    sh testCommand
                }
            }
        }
    }

    post {
        always {
            // Публикуем отчет Allure (даже если тесты упали)
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
        }
    }
}