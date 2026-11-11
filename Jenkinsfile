pipeline {

    agent any

    options {
        timestamps()
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout Source') {
            steps {
                echo 'Checking out Banking Application'
                checkout scm
            }
        }

        stage('Environment Check') {
            steps {
                bat 'java -version'
                bat 'gradlew.bat --version'
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling Java application'
                bat 'gradlew.bat clean classes'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Executing JUnit tests'
                bat 'gradlew.bat test'
            }
        }

        stage('Package') {
            steps {
                echo 'Generating Spring Boot JAR'
                bat 'gradlew.bat bootJar'
            }
        }
    }

    post {
        always {
            junit testResults: 'build/test-results/test/*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'build/libs/*.jar',
                             excludes: '**/*-plain.jar',
                             fingerprint: true,
                             allowEmptyArchive: true
        }

        success {
            echo 'Banking application CI pipeline completed successfully'
        }

        failure {
            echo 'Banking application CI pipeline failed'
        }
    }
}
