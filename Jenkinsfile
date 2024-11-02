pipeline {
    agent any

    environment {
            SONARQUBE_SERVER = 'SonarQube'
            MAVEN_SETTINGS = '/usr/share/maven/conf/settings.xml'
            DOCKER_HUB_CREDENTIALS = credentials('dockerhub') // Use your Jenkins credentials ID
            DOCKER_IMAGE_NAME = 'bilelkort/devops'
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

                            sh 'mvn sonar:sonar -Dsonar.projectKey=projet'

                            }
                        }
                    }
                }

        stage('Deploy to Nexus') {
                    steps {

                    // Assurez-vous que vous êtes dans le bon répertoire
        script {

                            sh "mvn deploy -DskipTests -s ${MAVEN_SETTINGS} -DaltDeploymentRepository=deploymentRepo::default::http://192.168.33.10:8081/repository/maven-releases/"
                        }                                    }


                }

        stage('Build Docker Image') {
                    steps {

                            sh "docker build -t bilelkort:latest ."

                    }
                }


        stage('Push to Docker Hub') {
                    steps {
                        script {
                            // Login to Docker Hub
                            sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"

                            // Tag the image
                            sh "docker tag bilelkort:latest ${DOCKER_IMAGE_NAME}:latest"

                            // Push the image
                            sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                        }
                    }
                }






    }
}