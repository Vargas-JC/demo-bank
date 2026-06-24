pipeline {

agent any

environment {
    APP_NAME = "demo-bank"
    IMAGE_NAME = "usuario/demo-bank"
    DOCKER_CREDS = credentials('dockerhub-creds')
}

stages {

    stage('Checkout') {
        steps {
            git branch: 'prod',
                url: 'https://github.com/Vargas-JC/demo-bank.git'
        }
    }

    stage('Build') {
        steps {
            sh './mvnw clean package'
        }
    }

    stage('Unit Tests') {
        steps {
            sh './mvnw test'
        }
    }

    stage('SonarQube Analysis') {
        environment {
            SONAR_TOKEN = credentials('sonar-token')
        }
        steps {
            withSonarQubeEnv('sonarqube') {
                sh '''
                ./mvnw sonar:sonar \
                -Dsonar.token=$SONAR_TOKEN
                '''
            }
        }
    }

    stage('Quality Gate') {
        steps {
            timeout(time: 5, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
            }
        }
    }

    stage('Build Docker Image') {
        steps {
            sh '''
            docker build \
            -t $IMAGE_NAME:${BUILD_NUMBER} .
            '''
        }
    }

    stage('Push Docker Image') {
        steps {
            script {
                docker.withRegistry(
                    'https://index.docker.io/v1/',
                    'dockerhub-creds'
                ) {
                    sh '''
                    docker push $IMAGE_NAME:${BUILD_NUMBER}
                    '''
                }
            }
        }
    }

    stage('Deploy') {
        steps {
            sh '''
            docker rm -f demo-bank || true
            docker run -d \
              --name demo-bank \
              -p 8080:8080 \
              $IMAGE_NAME:${BUILD_NUMBER}
            '''
        }
    }
}

post {

    success {
        echo "Deploy exitoso"

        emailext(
            subject: "SUCCESS - ${APP_NAME} Build #${BUILD_NUMBER}",
            body: """
Pipeline ejecutado correctamente.

Proyecto: ${APP_NAME}
Build: ${BUILD_NUMBER}
Estado: SUCCESS

Docker Image: ${IMAGE_NAME}:${BUILD_NUMBER}

Ver Jenkins:
${BUILD_URL}
"""
)
}
    failure {
        echo "Pipeline falló"

        emailext(
            subject: "FAILED - ${APP_NAME} Build #${BUILD_NUMBER}",
            body: """
Pipeline FALLÓ.

Proyecto: ${APP_NAME}
Build: ${BUILD_NUMBER}
Estado: FAILURE

Revisar logs:
${BUILD_URL}
"""
)
}
    always {
        cleanWs()
    }
}
}