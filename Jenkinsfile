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

                                                          step([pitmutation killRatioMustImprove: false, minimumKillRatio: 50.0, mutationStatsFile: '**/target/pit-reports/**/mutations.xml'])

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