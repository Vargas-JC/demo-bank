pipeline {

    agent any

    environment {
        APP_NAME   = 'demo-bank'
        IMAGE_NAME = 'usuario/demo-bank'
    }

    stages {

        stage('Checkout') {
            steps {
                git(
                    branch: 'prod',
                    url: 'https://github.com/Vargas-JC/demo-bank.git'
                )
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
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
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        ./mvnw sonar:sonar \
                        -Dsonar.host.url=https://sonarcloud.io \
                        -Dsonar.organization=vargas-jc \
                        -Dsonar.projectKey=demo-bank \
                        -Dsonar.token=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Build Docker Image') {

            steps {

                sh """
                    docker build \
                    -t ${env.IMAGE_NAME}:${env.BUILD_NUMBER} .
                """

            }
        }

        stage('Push Docker Image') {

            steps {

                script {

                    docker.withRegistry(
                        'https://index.docker.io/v1/',
                        'dockerhub-creds'
                    ) {

                        sh """
                            docker push ${env.IMAGE_NAME}:${env.BUILD_NUMBER}
                        """

                    }

                }

            }

        }

        stage('Deploy') {

            steps {

                sh """
                    docker rm -f demo-bank || true

                    docker run -d \
                        --name demo-bank \
                        -p 8080:8080 \
                        ${env.IMAGE_NAME}:${env.BUILD_NUMBER}
                """

            }

        }

    }

    post {

        success {

            echo 'Deploy exitoso'

            emailext(
                subject: "SUCCESS - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Pipeline ejecutado correctamente.

Proyecto: ${env.APP_NAME}

Build: ${env.BUILD_NUMBER}

Docker Image:
${env.IMAGE_NAME}:${env.BUILD_NUMBER}

Jenkins:
${env.BUILD_URL}
"""
            )

        }

        failure {

            echo 'Pipeline falló'

            emailext(
                subject: "FAILED - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Pipeline FALLÓ.

Proyecto: ${env.APP_NAME}

Build: ${env.BUILD_NUMBER}

Revisar logs:
${env.BUILD_URL}
"""
            )

        }

        always {
            cleanWs()
        }

    }

}