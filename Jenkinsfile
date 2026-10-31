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

        stage('Setup ChromeDriver') {
            steps {
                script {
                    sh '''
                        echo "=== Скачивание ChromeDriver ==="
                        mkdir -p drivers

                        # Скачиваем версию 149, так как именно она установлена в контейнере
                        wget -q -O /tmp/chromedriver.zip "https://storage.googleapis.com/chrome-for-testing-public/149.0.7827.155/linux64/chromedriver-linux64.zip"

                        # Распаковываем
                        cd /tmp
                        jar xf chromedriver.zip

                        # Копируем и даем права
                        cd $WORKSPACE
                        cp /tmp/chromedriver-linux64/chromedriver drivers/
                        chmod +x drivers/chromedriver

                        echo "=== Драйвер установлен: $WORKSPACE/drivers/chromedriver ==="
                    '''
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    // ВАЖНО: останавливаем старый Gradle Daemon, чтобы он подхватил новые пути
                    sh './gradlew --stop || true'

                    def testCommand = "./gradlew clean test "

                    testCommand += "-Dselenide.browser=chrome "
                    testCommand += "-Dselenide.headless=true "
                    testCommand += "-Dselenide.browserSize=1920x1080 "

                    // Передаем путь к драйверу
                    testCommand += "-Dwebdriver.chrome.driver=$WORKSPACE/drivers/chromedriver "

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