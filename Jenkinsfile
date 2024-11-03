    pipeline {
        agent any


        stages {
            stage('Initialize and Fetch Code') {
                steps {
                    echo 'Pulling code from Git...'
                    git branch: 'skanderLAZREG-5DS6-G4',
                        url: 'https://github.com/mehdi147899/deops_projet.git'
                }
            }

            stage('Build and Test') {
                steps {
                    sh 'mvn clean package'
                }
            }

            stage('SonarQube Analysis') {
                steps {
                    withSonarQubeEnv('MySonarQube') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }

            stage('Deploy to Nexus') {
                steps {
                    sh 'mvn deploy'
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        def imageName = 'skanderLAZREG-5DS6-G4-gestionStationSki'
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            def app = docker.build("${imageName}:${env.BUILD_NUMBER}")
                            app.push()
                        }
                    }
                }
            }

            stage('Deploy Using Docker Compose') {
                steps {
                    sh 'docker-compose up -d'
                }
            }

            stage('Verify Monitoring Services') {
                steps {
                    script {
                        // Health checks to ensure Prometheus and Grafana are running
                        sh 'curl -f http://localhost:9090/-/healthy' // Prometheus health check
                        sh 'curl -f http://localhost:3000/api/health' // Grafana health check
                        echo 'Both Prometheus and Grafana are up and running.'
                    }
                }
            }

        }

        post {
            always {
                // Take down all services including Prometheus and Grafana to clean up resources
                sh 'docker-compose down'
                cleanWs()  // Cleans up the workspace after the pipeline execution completes
            }
            success {
                echo 'SUCCESS: Pipeline completed successfully.'
            }
            failure {
                echo 'FAILURE: Pipeline failed.'
            }
        }
    }