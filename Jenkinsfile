pipeline {

    agent {
        label 'agent007'
    }

    stages {

        stage('Build') {

                steps {
                    sh 'mvn clean compile -Dskiptests'
                }

        }

        stage('Test') {
                steps {
                    sh 'mvn test'
                }
        }

        stage('Deploy') {
            steps {

            sh 'mvn clean package -Dskiptests'
            }
        }

    }


}