void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/razeen-bahadoor/RW796-Assignment1"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

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
               post {
                  success {
                      setBuildStatus("Build succeeded", "SUCCESS");
                  }
                  failure {
                      setBuildStatus("Build failed", "FAILURE");
                  }
                }
        }

        stage('Deploy') {
            steps {

            sh 'mvn clean package -Dskiptests'
            }
        }

    }


}