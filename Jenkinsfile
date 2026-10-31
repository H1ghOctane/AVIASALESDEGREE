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

        stage('Setup musl-ChromeDriver & ICU') {
            steps {
                script {
                    sh '''
                        echo "=== Скачивание musl-совместимого ChromeDriver и ICU для Alpine ==="
                        mkdir -p drivers

                        # 1. Скачиваем chromedriver из Alpine (musl-версия)
                        wget -q -O /tmp/chromedriver.apk "https://dl-cdn.alpinelinux.org/alpine/v3.21/community/x86_64/chromium-chromedriver-136.0.7103.113-r0.apk"

                        # 2. Скачиваем библиотеки ICU (они нужны для запуска chromedriver)
                        wget -q -O /tmp/icu-libs.apk "https://dl-cdn.alpinelinux.org/alpine/v3.21/main/x86_64/icu-libs-74.2-r1.apk"

                        # Распаковываем chromedriver
                        mkdir -p /tmp/chromedriver_extracted
                        tar -xzf /tmp/chromedriver.apk -C /tmp/chromedriver_extracted
                        cp /tmp/chromedriver_extracted/usr/bin/chromedriver drivers/
                        chmod +x drivers/chromedriver

                        # Распаковываем ICU-библиотеки в папку drivers/usr/lib
                        mkdir -p drivers/usr
                        tar -xzf /tmp/icu-libs.apk -C drivers/

                        echo "=== Проверка: запускаем chromedriver с указанием LD_LIBRARY_PATH ==="
                        export LD_LIBRARY_PATH=$WORKSPACE/drivers/usr/lib:$LD_LIBRARY_PATH
                        ./drivers/chromedriver --version

                        echo "=== Драйвер и библиотеки готовы ==="
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

                    // Указываем путь к musl-драйверу
                    testCommand += "-Dwebdriver.chrome.driver=$WORKSPACE/drivers/chromedriver "

                    // Указываем браузер Chromium
                    testCommand += "-Dchrome.binary=/usr/bin/chromium-browser "

                    // Отключаем Selenium Manager
                    testCommand += "-Dselenium.manager.enabled=false "

                    if (params.TEST_SUITE == 'ui') {
                        testCommand += "-Dgroups=ui"
                    } else if (params.TEST_SUITE == 'api') {
                        testCommand += "-Dgroups=api"
                    }

                    // Запускаем тесты с указанием LD_LIBRARY_PATH для ICU
                    sh """
                        export LD_LIBRARY_PATH=$WORKSPACE/drivers/usr/lib:\$LD_LIBRARY_PATH
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