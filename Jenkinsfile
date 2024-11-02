pipeline {
    agent any

    environment {
            SONARQUBE_SERVER = 'SonarQube'
            MAVEN_SETTINGS = '/usr/share/maven/conf/settings.xml'
        }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('MVN build') {
                        steps {
                            echo 'MVN ...'
                            sh 'mvn install -DskipTests'
                        }
                    }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        // Use the token for authentication
                        sh '''
                            mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=devops \
                            -Dsonar.login=${env.SONARQUBE_TOKEN}
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
                    steps {

                            sh "docker build -t bilelkort:latest ."

                    }
                }









    }
}