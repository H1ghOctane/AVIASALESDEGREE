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

        stage('Setup musl-ChromeDriver') {
            steps {
                script {
                    sh '''
                        echo "=== Скачивание musl-совместимого ChromeDriver для Alpine ==="
                        mkdir -p drivers

                        # Скачиваем пакет chromium-chromedriver из репозитория Alpine
                        wget -q -O /tmp/chromedriver.apk "https://dl-cdn.alpinelinux.org/alpine/v3.21/community/x86_64/chromium-chromedriver-136.0.7103.113-r0.apk"

                        # Распаковываем .apk (это tar.gz) через tar (доступен в Alpine)
                        mkdir -p /tmp/chromedriver_extracted
                        tar -xzf /tmp/chromedriver.apk -C /tmp/chromedriver_extracted

                        # Копируем бинарник из usr/bin/chromedriver в нашу папку drivers
                        cp /tmp/chromedriver_extracted/usr/bin/chromedriver drivers/
                        chmod +x drivers/chromedriver

                        echo "=== Проверка: запускаем chromedriver --version ==="
                        ./drivers/chromedriver --version

                        echo "=== Драйвер готов: $WORKSPACE/drivers/chromedriver ==="
                    '''
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    sh './gradlew --stop || true'

                    def testCommand = "./gradlew clean test "

                    testCommand += "-Dselenide.browser=chrome "
                    testCommand += "-Dselenide.headless=true "
                    testCommand += "-Dselenide.browserSize=1920x1080 "

                    // Указываем путь к распакованному musl-драйверу
                    testCommand += "-Dwebdriver.chrome.driver=$WORKSPACE/drivers/chromedriver "

                    // Указываем браузер Chromium (он уже установлен в системе)
                    testCommand += "-Dchrome.binary=/usr/bin/chromium-browser "

                    // Отключаем Selenium Manager, чтобы он не пытался скачать glibc-версию
                    testCommand += "-Dselenium.manager.enabled=false "

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