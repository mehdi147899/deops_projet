pipeline {
    agent any

    environment {
            SONARQUBE_SERVER = 'SonarQube'
            MAVEN_SETTINGS = '/usr/share/maven/conf/settings.xml'
            SONARQUBE_TOKEN = credentials('8753e65d-8711-4208-9eed-fbdb00aade7e')
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
                                // Using double quotes for variable substitution
                                sh """
                                    mvn clean verify sonar:sonar \
                                    -Dsonar.projectKey=devops \
                                    -Dsonar.login=${SONARQUBE_TOKEN}
                                """
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