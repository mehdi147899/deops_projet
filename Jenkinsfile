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

                            withSonarQubeEnv('SonarQube') {

                            sh 'mvn sonar:sonar -Dsonar.projectKey=backend_devops'

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