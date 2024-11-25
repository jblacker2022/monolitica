pipeline {
    agent any

    environment {
        // Definir variables de entorno
        SONARQUBE_SERVER = 'SonarQube-Server'
        ARTIFACTORY_URL = 'http://artifactory.example.com/artifactory'
        ARTIFACTORY_REPO = 'libs-release-local'
        ARTIFACT_NAME = 'monolitica-app'
        VERSION = '1.0.0'

        // SonarQube
        SONAR_URL = 'http://172.174.20.139:9000/'
        SONAR_TOKEN = 'squ_c0eb7d6f5f5ff29ea8dfb1b234cd339ffd64b765'
    }

    tools {
        maven 'Maven' // Asegúrate de configurar Maven en Jenkins
        jdk 'Java 11' // Configura Java 11 en Jenkins
    }

    stages {
        stage('Setup') {
            steps {
                echo 'Setting up permissions for mvnw...'
                sh 'chmod +x ./mvnw' // Ajusta los permisos del archivo mvnw
            }
        }

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
                echo 'Analyzing code quality with SonarQube...'
                withSonarQubeEnv('SonarQube') { // 'SonarQube' debe coincidir con el nombre configurado en Jenkins
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
                    timeout(time: 60, unit: 'MINUTES') {  // Incrementa el tiempo de espera
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
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
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
