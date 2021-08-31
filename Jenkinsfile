pipeline {

    agent {
        label 'agent007'
    }

    stages {

        stage('Build') {

                steps {
                    sh 'mvn clean compile -DskipTests'
                }

        }

        stage('Test') {
                steps {
                    sh 'mvn test'
                    step([$class: 'JacocoPublisher',
                          execPattern: 'target/*.exec',
                          classPattern: 'target/classes',
                          sourcePattern: 'src/main/java',
                          exclusionPattern: 'src/test*'
                    ])
                }
                  post {
                        always {
                                      junit 'target/surefire-reports/**/*.xml'
                      }
                  }


        }
        stage('Deploy') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
                  post {
                    always {
                        archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
                    }
                }
        }

    }


}