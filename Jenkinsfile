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

                }
                  post {
                        always {
                                      junit 'target/surefire-reports/**/*.xml'

                                           step([$class: 'JacocoPublisher',
                                                                execPattern: 'target/*.exec',
                                                                classPattern: 'target/classes',
                                                                sourcePattern: 'src/main/java',
                                                                exclusionPattern: 'src/test*'
                                                          ])


                                                                       publishHTML([
                                                                                 allowMissing          : false,
                                                                                 alwaysLinkToLastBuild : false,
                                                                                 keepAll               : true,
                                                                                 reportDir             : '**/target/pit-reports/**/index.html',
                                                                                 reportFiles           : 'index.html',
                                                                                 reportTitles          : "API Documentation",
                                                                                 reportName            : "API Documentation"
                                                                             ])


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