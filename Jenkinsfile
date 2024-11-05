pipeline {
    agent any
    
    stages {
        stage('Clone Repository') {
            steps {
                // Clone the repository
                git 'https://github.com/mehdi147899/deops_projet.gi'
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Run Maven build
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image
                    sh 'docker build -t abderrahimallaniskier .'
                }
            }
        }
        
        stage('Run Docker Container') {
            steps {
                script {
                    // Run Docker container
                    sh 'docker run -d -p 8089:8089 abderrahimallaniskier'
                }
            }
        }
    }
}
