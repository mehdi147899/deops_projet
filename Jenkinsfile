pipeline {
    agent any

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
    }
}