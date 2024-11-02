pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'SonarQube'
        MAVEN_SETTINGS = '/usr/share/maven/conf/settings.xml'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building the project with Maven...'
                // Using settings.xml for Maven configuration
                sh "mvn install -DskipTests --settings ${MAVEN_SETTINGS}"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                script {
                    withSonarQubeEnv(SONARQUBE_SERVER) {
                        // Use explicit project key and potentially other properties as needed
                        sh 'mvn sonar:sonar -Dsonar.projectKey=backend_devops -Dsonar.login=${env.SONAR_TOKEN}'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t bilelkort:latest ."
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            // Optional: Add cleanup steps, e.g., removing temporary files
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for details.'
        }
    }
}
