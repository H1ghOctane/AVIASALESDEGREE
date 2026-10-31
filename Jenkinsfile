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

        // Стадию Install Chrome мы УДАЛИЛИ, так как браузер уже есть на агенте

        stage('Run Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    def testCommand = "./gradlew clean test "

                    // Указываем, что браузер - хромиум (или chrome)
                    testCommand += "-Dselenide.browser=chrome "
                    testCommand += "-Dselenide.headless=true "
                    testCommand += "-Dselenide.browserSize=1920x1080 "

                    if (params.TEST_SUITE == 'ui') {
                        testCommand += "-Dgroups=ui"
                    } else if (params.TEST_SUITE == 'api') {
                        testCommand += "-Dgroups=api"
                    }

                    // САМОЕ ВАЖНОЕ: меняем HOME на папку воркспейса, чтобы Selenium мог скачать драйвер
                    sh """
                        export HOME=\$WORKSPACE
                        $testCommand
                    """
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