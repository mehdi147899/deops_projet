pipeline {
    agent any
    
    environment {
        SONAR_SCANNER_HOME = tool 'SonarScanner'  // Ensure this matches the name configured in Jenkins
        NEXUS_URL = 'http://192.168.33.10:8081/repository/maven-releases-abder/'  // Updated Nexus repository URL
        NEXUS_CREDENTIALS_ID = 'nexus-creds'  // ID for Nexus credentials added in Jenkins
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
                        -Dsonar.host.url=http://192.168.33.10:9000 \
                        -Dsonar.login=squ_3c84ba8b6ecaeef2cb775ae5967ee1a48bbbf477"
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
                    sh 'curl http://192.168.33.10:8089/api/skier/all'
                }
            }
        }
        
        stage('Publish to Nexus') {
            steps {
                script {
                    // Publish the artifact to Nexus repository
                    withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS_ID}", usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh "mvn deploy:deploy-file \
                            -DgroupId=com.example \
                            -DartifactId=ski-station-app \
                            -Dversion=1.0.0 \
                            -Dpackaging=jar \
                            -Dfile=target/ski-station-app-1.0.0.jar \
                            -DrepositoryId=nexus \
                            -Durl=${NEXUS_URL} \
                            -Dnexus.username=$NEXUS_USERNAME \
                            -Dnexus.password=$NEXUS_PASSWORD"
                    }
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
