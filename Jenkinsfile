pipeline {
    agent any

    parameters {
        choice(name: 'TASK', choices: ['test', 'api', 'ui'], description: 'Gradle-задача: test — все тесты, api — API, ui — UI')
        password(name: 'TRAVELPAYOUTS_TOKEN', defaultValue: '', description: 'Токен Travelpayouts. Пусто — тесты цен будут пропущены')
        string(name: 'BROWSER', defaultValue: 'chrome', description: 'Браузер')
        string(name: 'BROWSER_VERSION', defaultValue: '128.0', description: 'Версия браузера')
        string(name: 'BROWSER_SIZE', defaultValue: '1920x1080', description: 'Размер окна браузера')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless для браузера на самом агенте Jenkins (без Selenoid)')
        password(name: 'REMOTE_URL', defaultValue: '', description: 'Адрес Selenoid с логином и паролем: https://user:pass@host/wd/hub. Пусто — браузер на агенте')
        string(name: 'VIDEO_STORAGE', defaultValue: '', description: 'Адрес папки с видео Selenoid: https://host/video/. Пусто — без видео')
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Run tests') {
            steps {
                // Параметры типа password Jenkins передаёт в переменные окружения и маскирует в логе
                sh '''
                    chmod +x gradlew
                    ./gradlew clean "$TASK" \
                        -Dtravelpayouts.token="$TRAVELPAYOUTS_TOKEN" \
                        -Dbrowser="$BROWSER" \
                        -DbrowserVersion="$BROWSER_VERSION" \
                        -DbrowserSize="$BROWSER_SIZE" \
                        -Dheadless="$HEADLESS" \
                        -DremoteUrl="$REMOTE_URL" \
                        -DvideoStorage="$VIDEO_STORAGE"
                '''
            }
        }
    }

    post {
        always {
            allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
        }
    }
}