pipeline {
    agent any
    
    stages {
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
                    // Build Docker image for the application
                    sh 'docker build -t abderrahimallaniskier .'
                }
            }
        }
        
        stage('Start Services with Docker Compose') {
            steps {
                script {
                    // Start app and db services in detached mode
                    sh 'docker-compose up -d'
                }
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    // Run tests or health checks here
                    // Replace with your test commands as needed
                    sh 'curl http://localhost:8089/api/skier/all'
                }
            }
        }
    }
    
    post {
        always {
            script {
                // Stop and remove containers after the pipeline completes
                sh 'docker-compose down'
            }
        }
    }
}
