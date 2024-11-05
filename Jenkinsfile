pipeline {
    agent any
    
    environment {
        SONAR_SCANNER_HOME = tool 'SonarScanner'  // Ensure this matches the name configured in Jenkins
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Run Maven build
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {  // Matches the SonarQube server name configured in Jenkins
                    sh "${SONAR_SCANNER_HOME}/bin/sonar-scanner \
                        -Dsonar.projectKey=ski_station_project \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=squ_89b1c545e04fe6e31dcd1d0853907159ea2bf8ae"
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
                    // Run tests or health checks
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
