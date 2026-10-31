pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: ['all', 'ui', 'api'], description: 'Какие тесты запускать')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Браузер для UI-тестов')
        string(name: 'BROWSER_VERSION', defaultValue: '128.0', description: 'Версия браузера')
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

        stage('Start Selenoid') {
            steps {
                script {
                    // Создаем уникальную сеть для этого билда
                    def networkName = "selenoid-network-${env.BUILD_NUMBER}"
                    sh "docker network create ${networkName}"

                    // Запускаем Selenoid в этой сети
                    sh """
                        docker run -d --name selenoid \
                          --network ${networkName} \
                          -p 4444:4444 \
                          -v /var/run/docker.sock:/var/run/docker.sock \
                          -v ~/.aerokube/selenoid:/etc/selenoid \
                          aerokube/selenoid:latest-release
                    """

                    // Ждем, пока Selenoid поднимется
                    sh 'sleep 5'

                    // Сохраняем имя сети для следующих шагов
                    env.SELENOID_NETWORK = networkName
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'

                    def testCommand = "./gradlew clean test "

                    // Передаем параметры для удаленного запуска в Selenoid
                    testCommand += "-Dselenide.remote=http://selenoid:4444/wd/hub "
                    testCommand += "-Dselenide.browser=${params.BROWSER} "
                    testCommand += "-Dselenide.browserVersion=${params.BROWSER_VERSION} "
                    testCommand += "-Dselenide.headless=true "

                    // Передаем параметры для Owner
                    testCommand += "-Dweb.baseUrl=https://www.aviasales.ru/ "
                    testCommand += "-Dapi.baseUrl=https://api.travelpayouts.com "

                    if (params.TEST_SUITE == 'ui') {
                        testCommand += "-Dgroups=ui"
                    } else if (params.TEST_SUITE == 'api') {
                        testCommand += "-Dgroups=api"
                    }

                    // Запускаем тесты. Они будут выполняться в контейнере с Selenoid
                    sh testCommand
                }
            }
        }
    }

    post {
        always {
            // Останавливаем и удаляем контейнер Selenoid, чистим сеть
            script {
                sh 'docker stop selenoid || true'
                sh 'docker rm selenoid || true'
                sh "docker network rm ${env.SELENOID_NETWORK} || true"
            }
            // Публикуем отчет Allure
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
        }
    }
}