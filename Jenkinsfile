pipeline {
    agent any

    environment {
        // Definir variables de entorno, por ejemplo:
        SONARQUBE_SERVER = 'SonarQube-Server'
        ARTIFACTORY_URL = 'http://artifactory.example.com/artifactory'
        ARTIFACTORY_REPO = 'libs-release-local'
        ARTIFACT_NAME = 'monolitica-app'
        VERSION = '1.0.0'
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Clonar el repositorio Git
                git branch: 'main', url: 'https://github.com/jblacker2022/monolitica.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Ejecutar Maven para compilar y empaquetar el artefacto
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Ejecutar las pruebas unitarias con Maven y generar reporte JaCoCo
                    sh 'mvn test'
                }
            }
        }

        stage('JaCoCo Report') {
            steps {
                script {
                    // Ejecutar JaCoCo para analizar la cobertura de pruebas
                    sh 'mvn jacoco:report'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Análisis de código con SonarQube
                    sh '''
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=monolitica-app \
                        -Dsonar.host.url=http://$SONARQUBE_SERVER \
                        -Dsonar.login=$SONARQUBE_TOKEN
                    '''
                }
            }
        }

        stage('Publish to Artifactory') {
            steps {
                script {
                    // Publicar el artefacto en Artifactory
                    sh '''
                        mvn deploy:deploy-file \
                        -Dfile=target/$ARTIFACT_NAME-$VERSION.jar \
                        -DgroupId=com.example \
                        -DartifactId=$ARTIFACT_NAME \
                        -Dversion=$VERSION \
                        -Dpackaging=jar \
                        -DrepositoryId=artifactory \
                        -Durl=$ARTIFACTORY_URL/$ARTIFACTORY_REPO
                    '''
                }
            }
        }
    }

    post {
        always {
            // Limpiar recursos al final
            cleanWs()
        }
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
}
