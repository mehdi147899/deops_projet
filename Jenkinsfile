pipeline {
    agent any

    environment {
            SONARQUBE_SERVER = 'SonarQube'
            MAVEN_SETTINGS = '/usr/share/maven/conf/settings.xml'

            dockerImage = ""
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

//         stage('Build Docker Image') {
//                     steps {
//
//                             sh "docker build -t bilelkort:latest ."
//
//                     }
//                 }

        stage('Docker Image') {
                    steps {
                        script {
                            dockerImage = docker.build "bilelkort/devops:latest"
                        }
                    }
                }



        stage('Docker Hub') {
                    steps {
                        script {
                            docker.withRegistry( '', 'dockerhub' ) {
                                dockerImage.push()
                            }
                        }
                    }
                }






    }
}