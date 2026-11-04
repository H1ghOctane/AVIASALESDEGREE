pipeline {
    agent any

    parameters {
        choice(name: 'TASK', choices: ['test', 'api', 'ui'],
                description: 'Что запускать: test — все тесты, api — только API, ui — только UI')
        choice(name: 'BROWSER', choices: ['chrome:157.0', 'chrome:156.0', 'firefox:140.0'],
                description: 'Браузер и версия в Selenoid (список — с selenoid.qa.guru)')
        choice(name: 'BROWSER_SIZE', choices: ['1920x1080', '1366x768', '1280x800'],
                description: 'Размер окна браузера')
        password(name: 'REMOTE_URL', defaultValue: '',
                description: 'Selenoid с логином и паролем: https://user:password@selenoid.qa.guru/wd/hub')
        password(name: 'TRAVELPAYOUTS_TOKEN', defaultValue: '',
                description: 'Токен Travelpayouts. Пусто — тесты цен будут пропущены')
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Run tests') {
            steps {
                // REMOTE_URL и TRAVELPAYOUTS_TOKEN не передаются через -D: Jenkins печатает команду в лог.
                // Gradle берёт их из переменных окружения (см. build.gradle)
                sh '''
                    chmod +x gradlew
                    BROWSER_NAME="${BROWSER%%:*}"
                    BROWSER_VERSION="${BROWSER##*:}"
                    ./gradlew clean "$TASK" \\
                        -Dbrowser="$BROWSER_NAME" \\
                        -DbrowserVersion="$BROWSER_VERSION" \\
                        -DbrowserSize="$BROWSER_SIZE"
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