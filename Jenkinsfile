    pipeline {
        agent any


        stages {
            stage('Initialize and Fetch Code') {
                steps {
                    git (
                    url: 'https://github.com/mehdi147899/deops_projet.git',
                    branch: 'skanderLAZREG-5DS6-G4',
                    credentialsId: 'github'
                )
                }
            }

            stage('Build and Test') {
                steps {
                    sh 'mvn install -DskipTests'
                }
            }

            stage('Run Unit Tests') {
                steps {
                    echo 'Running unit tests...'
                    sh 'mvn test'
                }
            }

            stage('SonarQube Analysis') {
                steps {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=jenkins -Dsonar.sources=src/main/java -Dsonar.host.url=http://192.168.1.100:9000 -Dsonar.token=sqa_2b3d94d205010f6ac883b8699dd1c75c33d85936'
                }
            }

            stage('Deploy to Nexus') {
                steps {
                    sh 'mvn deploy -DskipTests=true'
                }
            }

            stage('Docker Image') {
                steps {
                    script {
                        dockerImage = docker.build "skanderlazreg/gestionstationski:1.0.0"
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

            stage('Deploy Using Docker Compose') {
                steps {
                    sh 'docker compose up -d'
                }
            }

        }
    }