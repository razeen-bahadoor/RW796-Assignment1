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
            sh 'mvn clean package -Dskiptests'
        }

    }


}