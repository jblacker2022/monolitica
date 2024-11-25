pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'SonarQube-Server'
        ARTIFACTORY_URL = 'http://artifactory.example.com/artifactory'
        ARTIFACTORY_REPO = 'libs-release-local'
        ARTIFACT_NAME = 'monolitica-app'
        VERSION = '1.0.0'
        SONAR_URL = 'http://172.174.20.139:9000/'
        SONAR_TOKEN = 'squ_c0eb7d6f5f5ff29ea8dfb1b234cd339ffd64b765'
    }

    tools {
        maven 'Maven'
        jdk 'Java 11'
    }

    stages {
        stage('Setup') {
            steps {
                echo 'Verificando permisos de mvnw...'
                sh 'ls -l ./mvnw' // Verificar permisos del archivo mvnw
                echo 'Configurando permisos para mvnw...'
                sh 'chmod +x ./mvnw' // Asegura que el archivo sea completamente ejecutable
                sh 'ls -l ./mvnw' // Verificar nuevamente los permisos
            }
        }

        stage('Clone Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/jblacker2022/monolitica.git'
            }
        }

        stage('Fix Permissions') {
            steps {
                script {
                    // Verificar permisos del archivo mvnw y corregir si es necesario
                    sh 'ls -l ./mvnw' // Ver permisos actuales
                    sh 'chmod +x ./mvnw' // Asegurarse de que el archivo es ejecutable
                    sh 'ls -l ./mvnw' // Verificar los permisos después de corregir
                }
            }
        }

        stage('Test mvnw execution') {
            steps {
                sh './mvnw --version' // Verifica que el archivo mvnw sea ejecutable
            }
        }

        stage('Build') {
            steps {
                script {
                    // Ejecutar Maven para compilar y empaquetar el artefacto
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Testing (JUnit + Jacoco)') {
            steps {
                echo 'Running tests and generating coverage report...'
                sh './mvnw test'
                sh './mvnw jacoco:report'
            }
        }

        stage('Sonar Analysis') {
            steps {
                echo 'Analizando calidad del código con SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh """
                        ./mvnw sonar:sonar \
                        -Dsonar.projectKey=monolitica-app \
                        -Dsonar.host.url=${SONAR_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 60, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }

        // Etapa de publicación en Artifactory eliminada
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
