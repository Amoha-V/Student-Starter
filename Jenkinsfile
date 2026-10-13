
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout / Workspace') {
            steps {
                echo 'Building the local Course Registration project'
                bat 'git status --short'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn -B clean test'
            }
            post {
                always {
                    junit 'course-registration-app/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package -DskipTests'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'course-registration-app/target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Build, tests, and packaging completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the console output and test reports.'
        }
    }
}
