pipeline {
    agent any

    parameters {
        choice(name: 'TASK', choices: ['test', 'api', 'ui'], description: 'Gradle-задача: test — все тесты, api — API, ui — UI')
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Браузер')
        string(name: 'BROWSER_VERSION', defaultValue: '128.0', description: 'Версия браузера')
        string(name: 'BROWSER_SIZE', defaultValue: '1920x1080', description: 'Размер окна браузера')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless для браузера на самом агенте Jenkins (без Selenoid)')
        password(name: 'REMOTE_URL', defaultValue: '', description: 'Адрес Selenoid с логином и паролем: https://user:pass@host/wd/hub. Пусто — браузер на агенте')
        string(name: 'VIDEO_STORAGE', defaultValue: '', description: 'Адрес папки с видео Selenoid: https://host/video/. Пусто — без видео')
        booleanParam(name: 'NOTIFY', defaultValue: false, description: 'Отправить результат в Telegram')
    }

    environment {
        NOTIFICATIONS_JAR = '../allure-notifications-5.0.8.jar'
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run tests') {
            steps {
                // Токен лежит в Jenkins Credentials (Secret text, id = travelpayouts-token).
                // Jenkins кладёт его в переменную TP_TOKEN только на время этого блока и маскирует в логе.
                withCredentials([string(credentialsId: 'travelpayouts-token', variable: 'TP_TOKEN')]) {
                    sh '''
                        chmod +x gradlew
                        ./gradlew clean "$TASK" \
                            -Dbrowser="$BROWSER" \
                            -DbrowserVersion="$BROWSER_VERSION" \
                            -DbrowserSize="$BROWSER_SIZE" \
                            -Dheadless="$HEADLESS" \
                            -DremoteUrl="$REMOTE_URL" \
                            -DvideoStorage="$VIDEO_STORAGE" \
                            -Dtravelpayouts.token="$TP_TOKEN"
                    '''
                }
            }
        }
    }

    post {
        always {
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]

            script {
                if (params.NOTIFY) {
                    withCredentials([
                            string(credentialsId: 'telegram-bot-token', variable: 'TG_TOKEN'),
                            string(credentialsId: 'telegram-chat-id', variable: 'TG_CHAT')
                    ]) {
                        // В репозитории лежит шаблон без секретов, рабочая копия собирается в build/ и в git не попадает
                        sh '''
                            sed -e "s|{{TELEGRAM_TOKEN}}|$TG_TOKEN|" \
                                -e "s|{{TELEGRAM_CHAT}}|$TG_CHAT|" \
                                -e "s|{{REPORT_LINK}}|${BUILD_URL}allure|" \
                                -e "s|{{TASK}}|$TASK|" \
                                notifications/telegram.json > build/notifications.json
                            java -DconfigFile=build/notifications.json -jar "$NOTIFICATIONS_JAR"
                        '''
                    }
                }
            }
        }
    }
}
